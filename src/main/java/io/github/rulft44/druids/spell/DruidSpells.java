package io.github.rulft44.druids.spell;

import io.github.rulft44.druids.Druids;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.client.particle.MoreParticles;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.more_rpg_classes.item.MRPGCItems;
import net.more_rpg_classes.sounds.ModSounds;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DruidSpells {

	public record Entry(Identifier id, Spell spell, String title, String description,
						@Nullable SpellTooltip.DescriptionMutator mutator) {
	}

	public static final List<Entry> entries = new ArrayList<>();

	private static Entry add(Entry entry) {
		entries.add(entry);
		return entry;
	}


	private static Spell.Impact damageImpact(float coefficient, float knockback) {
		var damage = new Spell.Impact();
		damage.action = new Spell.Impact.Action();
		damage.action.type = Spell.Impact.Action.Type.DAMAGE;
		damage.action.damage = new Spell.Impact.Action.Damage();
		damage.action.damage.spell_power_coefficient = coefficient;
		damage.action.damage.knockback = knockback;
		return damage;
	}
	private static void configureCooldown(Spell spell, float duration) {
		if (spell.cost == null) {
			spell.cost = new Spell.Cost();
		}
		spell.cost.cooldown = new Spell.Cost.Cooldown();
		spell.cost.cooldown.duration = duration;
	}
	private static Spell.Impact createEffectImpact(Identifier effectId, float duration) {
		var buff = new Spell.Impact();
		buff.action = new Spell.Impact.Action();
		buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
		buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
		buff.action.status_effect.effect_id = effectId.toString();
		buff.action.status_effect.duration = duration;
		return buff;
	}

	private static void configureNatureRuneCost(Spell spell) {
		if (spell.cost == null) {
			spell.cost = new Spell.Cost();
		}
		spell.cost.item = new Spell.Cost.Item();
		spell.cost.item.id = Registries.ITEM.getId(MRPGCItems.NATURE_STONE).toString();
	}

	private static ParticleBatch[] natureCastingParticles() {
		return new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.MagicParticles.get(
				SpellEngineParticles.MagicParticles.Shape.SPARK,
				SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
				ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
				1.5F, 0.05F, 0.2F).color(4294954239L),
			new ParticleBatch("more_rpg_classes:leaf",
				ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
				0.5F, 0.05F, 0.1F).scale(0.5F).maxAge(0.25F)
		};
	}

	public static final Entry bramble_volley = add(bramble_volley());
	private static Entry bramble_volley() {
		var id = Identifier.of(Druids.ID, "bramble_volley");
		var title = "";
		var description = "";
		var spell = SpellBuilder.createSpellActive();
		spell.group = "primary";
		spell.range = 64F;
		spell.tier = 1;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.duration = 0.25F;
		spell.active.cast.channel_ticks = 2;

		spell.active.cast.animation = "spell_engine:one_handed_projectile_charge";
		spell.release.animation = "spell_engine:one_handed_projectile_release";

		var debuff1 = createEffectImpact(MRPGCEffects.GRIEVOUS_WOUNDS.id, 5);
		debuff1.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		debuff1.action.status_effect.show_particles = false;
		debuff1.action.status_effect.amplifier = 1;
		debuff1.action.status_effect.amplifier_cap = 5;
		debuff1.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
		debuff1.particles = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.dripping_blood.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
				10, 0.05F, 0.3F),
		};
		var debuff2 = createEffectImpact(Registries.STATUS_EFFECT.getId(StatusEffects.POISON.value()), 5);
		debuff2.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		debuff2.action.status_effect.show_particles = false;
		debuff2.action.status_effect.amplifier = 1;
		debuff2.action.status_effect.amplifier_cap = 5;
		debuff2.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
		debuff2.particles = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.dripping_blood.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
				10, 0.05F, 0.3F),
		};

		spell.target.type = Spell.Target.Type.AIM;
		spell.target.aim = new Spell.Target.Aim();

		spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
		spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
		spell.deliver.projectile.launch_properties.velocity = 1F;
		spell.deliver.projectile.direction_offsets = new Spell.Delivery.ShootProjectile.DirectionOffset[] {
			new Spell.Delivery.ShootProjectile.DirectionOffset(-10.0F, 0.0F), // left
			new Spell.Delivery.ShootProjectile.DirectionOffset(0.0F, 0.0F),   // center
			new Spell.Delivery.ShootProjectile.DirectionOffset(10.0F, 0.0F)   // right
		};
		spell.deliver.projectile.launch_properties.sound = new Sound(ModSounds.NATURE_RELEASE_1_ID.toString(), 0.75F, 1F, 0.1F);

			var projectile = new Spell.ProjectileData();
			projectile.homing_angle = 1;
			projectile.client_data = new Spell.ProjectileData.Client();
			projectile.client_data.light_level = 6;
			projectile.client_data.travel_particles = new ParticleBatch[]{
				new ParticleBatch(
					Registries.PARTICLE_TYPE.getId(ParticleTypes.SPORE_BLOSSOM_AIR).toString(),
					ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
					ParticleBatch.Rotation.LOOK, 1, 0, 0.1F, 0),
			};
			projectile.client_data.model = new Spell.ProjectileModel();
			projectile.client_data.model.model_id = "druids:projectile/bramble_shot";
			projectile.client_data.model.scale = 0.5F;
			spell.deliver.projectile.projectile = projectile;

		var damage = damageImpact(0.50F, 0.8F);
		damage.sound = new Sound(ModSounds.CRIPPLING_STRIKE_ID.toString(), 0.5F, 1F, 0.1F);

		spell.impacts = List.of(debuff1, debuff2, damage);

		configureCooldown(spell, 3);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description, null);
	}

	public static final Entry mass_entanglement = add(mass_entanglement());
	private static Entry mass_entanglement() {
		var id = Identifier.of(Druids.ID, "mass_entanglement");
		var title = "";
		var description = "";
		var debuffEffect = MRPGCEffects.STAGGER;
		var spell = SpellBuilder.createSpellActive();
		spell.range = 10F;
		spell.tier = 4;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.animation = "spell_engine:one_handed_area_charge";
		spell.active.cast.particles = natureCastingParticles();
		spell.active.cast.duration = 6;
		spell.active.cast.channel_ticks = 14;
		spell.active.cast.sound = new Sound(ModSounds.NATURE_CAST_1_ID);

		spell.release.animation = "spell_engine:one_handed_area_release";

		var debuff = createEffectImpact(debuffEffect.id, 15);
		debuff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		debuff.action.status_effect.show_particles = false;
		debuff.action.status_effect.amplifier = 1;
		debuff.action.status_effect.amplifier_cap = 5;
		debuff.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
		debuff.particles = new ParticleBatch[]{
			new ParticleBatch("falling_spore_blossom",
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
				2, 0.1F, 0.2F)
		};

		var debuff2 = createEffectImpact(Registries.STATUS_EFFECT.getId(StatusEffects.POISON.value()), 5);
		debuff2.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		debuff2.action.status_effect.show_particles = false;
		debuff2.action.status_effect.amplifier = 2;
		debuff2.action.status_effect.amplifier_cap_power_multiplier = 0.5F;
		debuff2.particles = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.dripping_blood.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
				10, 0.05F, 0.3F),
		};

		spell.target.type = Spell.Target.Type.AREA;
		spell.target.area = new Spell.Target.Area();
		spell.target.area.vertical_range_multiplier = 1F;
		spell.target.area.angle_degrees = 360;

		spell.release.sound = new Sound(ModSounds.NATURE_IMPACT_1_ID);
		spell.release.particles = new ParticleBatch[]{
			new ParticleBatch(
				SpellEngineParticles.roots.id().toString(),
				ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
				3F, 0, 0.5F)
		};
		spell.release.particles_scaled_with_ranged = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
				1, 0, 0).color(4283786581L)//.scale(0.8F)
		};
		spell.release.particles = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.area_swirl.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
				1, 0.5F, 0.5F).color(961665948L).scale(0.8F)
		};

		var damage = damageImpact(0.25F, 0.5F);

		spell.impacts = List.of(debuff, debuff2, damage);

		configureCooldown(spell, 25);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description, null);
	}

}
