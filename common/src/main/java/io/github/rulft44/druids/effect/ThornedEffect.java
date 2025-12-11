package io.github.rulft44.druids.effect;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.config.EffectsConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.tiny_config.ConfigManager;

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
		ConfigManager<EffectsConfig> config = Druids.effectsConfig;

		if (entity instanceof PlayerEntity player) {
			LivingEntity attacker = player.getAttacker();

			if (player.hurtTime > 0 && attacker != null && attacker.isAlive()) {
				World world = player.getWorld();
				if (world.random.nextFloat() < config.value.thorned_proc_chance) {
					DamageSource thorns = world.getDamageSources().thorns(player);
					float damage = config.value.thorned_damage + amplifier;
					attacker.damage(thorns, damage);
				}
			}
		}

		return super.applyUpdateEffect(entity, amplifier);
	}

}
