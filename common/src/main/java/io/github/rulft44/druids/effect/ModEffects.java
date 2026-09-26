package io.github.rulft44.druids.effect;

import io.github.rulft44.druids.Druids;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.rpg_series.config.EffectConfig;
import net.spell_engine.api.effect.Effects;
import net.spell_engine.api.effect.Synchronized;

import java.util.ArrayList;
import java.util.List;

public class ModEffects {

	public static final List<Effects.Entry> entries = new ArrayList<>();
	private static Effects.Entry add(Effects.Entry entry) {
		entries.add(entry);
		return entry;
	}

	public static Effects.Entry THORNED = add(new Effects.Entry(Identifier.of(Druids.ID, "thorned"),
		"Thorned",
		"Gives the user thorns and armor.",
		new ThornedEffect(),
		new EffectConfig(
			List.of(
				new AttributeModifier(
					EntityAttributes.GENERIC_ARMOR.getIdAsString(),
					2F,
					EntityAttributeModifier.Operation.ADD_VALUE
				)
			)
		)
	));

	public static Effects.Entry POISON_RITUAL = add(new Effects.Entry(Identifier.of(Druids.ID, "poison_ritual"),
		"Poison Ritual",
		"Poisons you but increases nature spell power.",
		new PoisonRitualEffect(StatusEffectCategory.NEUTRAL, 6107020).interval(3),
		new EffectConfig(
			List.of(
				new AttributeModifier(
					MoreSpellSchools.NATURE.id,
					0.3F,
					EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
				)
			)
		)
	));

	public static void register(ConfigFile.Effects config) {
		for (var entry : entries) {
			Synchronized.configure(entry.effect, true);
		}
		Effects.register(entries, config.effects);
	}
}

