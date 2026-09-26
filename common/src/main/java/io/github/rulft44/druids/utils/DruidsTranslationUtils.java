package io.github.rulft44.druids.utils;

import io.github.rulft44.druids.spell.skill.DruidSkillDefinitions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.spell_engine.mixin.client.ItemRendererMixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DruidsTranslationUtils {
    public static final Map<String, Supplier<List<Text>>> resolvers = new HashMap<>();

    public static List<Text> resolve(String skillId) {
        var supplier = resolvers.get(skillId);
        if (supplier == null) {
            return List.of();
        }
        return supplier.get();
    }

    public static List<Text> resolveAttributeModifierTooltip(DruidSkillDefinitions.EntityAttributeReward attributeReward) {
        var player = MinecraftClient.getInstance().player;
        if (player == null) {
            return List.of();
        }

        var bonusLines = new ArrayList<Text>();
        return bonusLines;
    }
}
