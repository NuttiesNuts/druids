package io.github.rulft44.druids.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ThornedEffect extends StatusEffect {
	protected ThornedEffect() {
		super(StatusEffectCategory.BENEFICIAL, 0x56211a);
	}

	// Called every tick to check if the effect can be applied or not
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	// Called when the effect is applied.
	@Override
	public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity instanceof PlayerEntity player) {
			LivingEntity attacker = player.getAttacker();

			if (player.hurtTime > 0 && attacker != null && attacker.isAlive()) {
				World world = player.getWorld();
				if (world.random.nextFloat() < 2) {
					DamageSource thorns = world.getDamageSources().thorns(player);
					float damage = 2 + amplifier;
					attacker.damage(thorns, damage);
				}
			}
		}

		return super.applyUpdateEffect(entity, amplifier);
	}

}
