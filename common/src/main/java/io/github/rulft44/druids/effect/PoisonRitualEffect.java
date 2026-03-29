package io.github.rulft44.druids.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.effect.CustomStatusEffect;
import net.spell_engine.api.effect.TickingStatusEffect;

public class PoisonRitualEffect extends TickingStatusEffect {
	public PoisonRitualEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
		entity.addStatusEffect(new StatusEffectInstance(MRPGCEffects.FATAL_POISON.entry, 200));
		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 200 == 0;
	}
}
