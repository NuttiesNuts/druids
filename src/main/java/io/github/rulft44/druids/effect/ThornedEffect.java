package io.github.rulft44.druids.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ThornedEffect extends StatusEffect {
	protected ThornedEffect() {
		super(StatusEffectCategory.NEUTRAL, 0x56211a);
	}

	// Called every tick to check if the effect can be applied or not
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		// In our case, we just make it return true so that it applies the effect every tick
		return true;
	}

	// Called when the effect is applied.
	@Override
	public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity instanceof PlayerEntity player) {
			LivingEntity attacker = player.getAttacker();

			if (player.hurtTime > 0 && attacker != null && attacker.isAlive()) {
				World world = player.getWorld();

				DamageSource thorns = world.getDamageSources().thorns(player);
				float damage = (float) (1 + world.random.nextInt(3) + amplifier); // 1–3 + amp
				attacker.damage(thorns, damage);
			}
		}

		return super.applyUpdateEffect(entity, amplifier);
	}

}
