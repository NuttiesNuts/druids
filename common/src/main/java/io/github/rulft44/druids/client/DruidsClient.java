package io.github.rulft44.druids.client;

import io.github.rulft44.druids.client.armor.DruidArmorRenderer;
import io.github.rulft44.druids.item.ModArmors;
import io.github.rulft44.druids.spell.DruidSpells;
import io.github.rulft44.druids.spell.skill.DruidsSkillDefinitions;
import io.github.rulft44.druids.utils.DruidsTranslationUtils;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererRegistry;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.utils.TranslationUtil;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.rpg_series.item.Armor;

import java.util.function.Supplier;

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

		registerArmorRenderer(ModArmors.druidArmorSet, DruidArmorRenderer::druid);
		registerArmorRenderer(ModArmors.netheriteDruidArmorSet, DruidArmorRenderer::netherite_druid);
	}

	private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
		AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
	}
}
