package io.github.rulft44.druids.utils;

import io.github.rulft44.druids.spell.skill.DruidsSkillDefinitions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.mixin.client.ItemStackTooltipAccessor;

import java.util.*;
import java.util.function.Supplier;

public class DruidsTranslationUtils{
	public static final Map<String, Supplier<List<Text>>> resolvers = new HashMap<>();

	public static List<Text> resolve(String skillId) {
		var supplier = resolvers.get(skillId);
		if (supplier == null) {
			return List.of();
		}
		return supplier.get();
	}

	public static List<Text> resolveAttributeModifierTooltip(DruidsSkillDefinitions.EntityAttributeReward attributeReward) {
		var player = MinecraftClient.getInstance().player;
		if (player == null) {
			return List.of();
		}
		var tooltipUtil = (ItemStackTooltipAccessor) (Object) ItemStack.EMPTY;
		var bonusLines = new ArrayList<Text>();
		var modifier = attributeReward.modifier();
		tooltipUtil
			.spellEngine_appendAttributeModifierTooltip(
				bonusLines::add,
				player,
				attributeReward.attribute(),
				modifier
			);
		return bonusLines;
	}
}
