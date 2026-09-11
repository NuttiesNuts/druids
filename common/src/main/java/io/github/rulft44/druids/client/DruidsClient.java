package io.github.rulft44.druids.client;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.client.armor.DruidArmorRenderer;
import io.github.rulft44.druids.item.ModArmors;
import io.github.rulft44.druids.spell.DruidSpells;
import io.github.rulft44.druids.spell.skill.DruidsSkillDefinitions;
import io.github.rulft44.druids.utils.DruidsTranslationUtils;
import net.rpg_foundation.armor_api.client.ArmorRenderers;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.skill_tree_rpgs.utils.TranslationUtil;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.rpg_series.item.Armor;

public class DruidsClient{
	public static void init() {

		for (var spell: DruidSpells.entries) {
			if (spell.mutator() != null) {
				SpellTooltip.addDescriptionMutator(spell.id(), spell.mutator());
			}
		}
		for (var entry : DruidsSkillDefinitions.ENTRIES) {
			var skillId = entry.id();
			if (entry.spellReward() != null) {
				var container = entry.spellReward().get(0);
				var id = Identifier.of(container.spell_ids().getFirst());
				TranslationUtil.resolvers.put(skillId, () -> TranslationUtil.resolveSpellDetails(id));
			} else if (entry.attributeReward() != null) {
				var attribute = entry.attributeReward();
				TranslationUtil.resolvers.put(skillId, () -> DruidsTranslationUtils.resolveAttributeModifierTooltip(attribute));
			}
		}

		registerArmorRenderer(ModArmors.druidArmorSet, DruidArmorRenderer.druid());
		registerArmorRenderer(ModArmors.netheriteDruidArmorSet, DruidArmorRenderer.netherite_druid());

		HudRenderCallback.EVENT.register((context, tickDeltaManager) -> {
			if (MinecraftClient.getInstance().player.hasStatusEffect(MRPGCEffects.FATAL_POISON.entry)) {
				context.drawTexture(POISONED_OVERLAY_LOCATION, 0, 0, 0.0F, 0.0F, context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
			}
		});
	}

	private static final Identifier POISONED_OVERLAY_LOCATION = Identifier.of(Druids.ID, "textures/gui/poisoned_overlay.png");

	private static void registerArmorRenderer(Armor.Set set, GeoArmorRenderer renderer) {
		ArmorRenderers.register(renderer, set.head, set.chest, set.legs, set.feet);
	}
}
