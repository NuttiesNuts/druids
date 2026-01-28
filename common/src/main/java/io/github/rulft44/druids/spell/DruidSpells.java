package io.github.rulft44.druids.spell;

import io.github.rulft44.druids.Druids;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.client.particle.MoreParticles;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.custom.spell_impacts.PullInToCasterDirectSpellImpact;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.more_rpg_classes.item.MRPGCItems;
import net.more_rpg_classes.sounds.ModSounds;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.internals.target.SpellTarget;
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
	private static Spell.Impact customImpact(String impactId, SpellTarget.Intent intent) {
		var effect = new Spell.Impact();
		effect.action = new Spell.Impact.Action();
		effect.action.type = Spell.Impact.Action.Type.CUSTOM;
		effect.action.custom = new Spell.Impact.Action.Custom();
		effect.action.custom.handler = impactId;
		effect.action.custom.intent = intent;
		return effect;
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

	private static Spell.Delivery.Cloud makeRootCluster(
		String model,
		float scale,
		int delay,
		float distance_from_caster,
		int placement_angle,
		int rotation
	) {
		var c = new Spell.Delivery.Cloud();
		c.volume.radius = 1F;
		c.volume.area.vertical_range_multiplier = 2F;
		c.delay_ticks = delay;
		c.impact_tick_interval = 20;
		c.time_to_live_seconds = 10;
		c.spawn = new Spell.Delivery.Cloud.Spawn();

		c.client_data = new Spell.Delivery.Cloud.ClientData();
		c.client_data.model = new Spell.ProjectileModel();
		c.client_data.model.model_id = model;
		c.client_data.model.scale = scale;
		c.client_data.model.rotate_degrees_per_tick = 0;
		//c.client_data.model.orientation = Spell.ProjectileModel.Orientation.TOWARDS_MOTION;
		c.client_data.model.light_emission = LightEmission.NONE;

		c.placement = SpellBuilder.Deliver.placementByLook(distance_from_caster, placement_angle, 0);
		return c;
	}


	public static final Entry vine_whip = add(vine_whip());
	private static Entry vine_whip() {
		var id = Identifier.of(Druids.ID, "vine_whip");
		var title = "";
		var description = "";
		var spell = SpellBuilder.createSpellActive();
		spell.range = 16F;
		spell.tier = 3;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.duration = 0.75F;
		spell.active.cast.particles = natureCastingParticles();
		spell.active.cast.sound = new Sound(ModSounds.NATURE_CAST_1_ID);
		spell.active.cast.animation = "spell_engine:one_handed_healing_charge";

		spell.release.sound = new Sound(ModSounds.NATURE_RELEASE_2_ID);
		spell.release.animation = "spell_engine:one_handed_healing_release";

		spell.target.type = Spell.Target.Type.AIM;
		spell.target.aim = new Spell.Target.Aim();
		spell.target.aim.sticky = true;

		var poison = createEffectImpact(MRPGCEffects.FATAL_POISON.id, 5);
		poison.action.status_effect.amplifier = 2;
		poison.action.status_effect.amplifier_cap_power_multiplier = 0.5F;
		poison.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;

		var pull = customImpact("more_rpg_classes:pull_to_caster_direct", SpellTarget.Intent.HARMFUL);
		pull.sound = new Sound(ModSounds.NATURE_IMPACT_1_ID);

		spell.impacts = List.of(poison, pull);

		configureCooldown(spell, 4);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description, null);
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
		var debuff2 = createEffectImpact(MRPGCEffects.FATAL_POISON.id, 5);
		debuff2.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		debuff2.action.status_effect.show_particles = false;
		debuff2.action.status_effect.amplifier = 1;
		debuff2.action.status_effect.amplifier_cap = 5;
		debuff2.action.status_effect.amplifier_cap_power_multiplier = 0.2F;

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
		var debuffEffect = SpellEngineEffects.STUN;
		var spell = SpellBuilder.createSpellActive();
		spell.range = 10F;
		spell.tier = 4;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.animation = "spell_engine:one_handed_area_charge";
		spell.active.cast.particles = natureCastingParticles();
		spell.active.cast.duration = 10;
		//spell.active.cast.channel_ticks = 14;
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

		var debuff2 = createEffectImpact(MRPGCEffects.FATAL_POISON.id, 5);
		debuff2.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		debuff2.action.status_effect.show_particles = false;
		debuff2.action.status_effect.amplifier = 2;
		debuff2.action.status_effect.amplifier_cap_power_multiplier = 0.5F;


		spell.target.type = Spell.Target.Type.AIM;
		spell.target.aim = new Spell.Target.Aim();
		spell.target.aim.use_caster_as_fallback = true;
		spell.target.aim.sticky = true;

		spell.deliver.type = Spell.Delivery.Type.CLOUD;

		var outer1 = makeRootCluster("druids:effect/entanglement1", 1.2F, 8, 3F, 0, 0);
		var outer2 = makeRootCluster("druids:effect/entanglement11", 1.2F, 9, 3F, 90, 0);
		var outer3 = makeRootCluster("druids:effect/entanglement1", 1.2F, 10, 3F, 180, 0);
		var outer4 = makeRootCluster("druids:effect/entanglement11", 1.2F, 11, 3F, 270, 0);

		var inner1 = makeRootCluster("druids:effect/entanglement2", 1F, 8, 2F, 0, 0);
		var inner2 = makeRootCluster("druids:effect/entanglement2", 1F, 9, 2.5F, 110, 0);
		var inner3 = makeRootCluster("druids:effect/entanglement2", 1F, 10, 2F, 180, 0);
		var inner4 = makeRootCluster("druids:effect/entanglement2", 1F, 11, 1.75F, 300, 0);

		var flower1 = makeRootCluster("druids:effect/poppy", 1F, 14, 1F, 0, 0);
		var flower2 = makeRootCluster("druids:effect/dandelion", 1F, 15, 1F, 180, 0);


		spell.deliver.clouds = List.of(outer1,outer2,outer3,outer4,inner1,inner2,inner3,inner4,flower1,flower2);

		spell.release.sound = new Sound(ModSounds.NATURE_IMPACT_1_ID);

		spell.release.particles_scaled_with_ranged = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
				1, 0, 0).color(4283786581L)//.scale(0.8F)
		};

		var damage = damageImpact(0.15F, 0F);

		spell.impacts = List.of(debuff, debuff2, damage);

		configureCooldown(spell, 25);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description, null);
	}

	public static Entry dart_shot = add(dart_shot());
	private static Entry dart_shot() {
		var id = Identifier.of(Druids.ID, "dart_shot");
		var spell = SpellBuilder.createSpellActive();
		var title = "";
		var description = "";
		spell.school = MoreSpellSchools.NATURE;
		spell.group = "primary";
		spell.tier = 0;
		spell.range = 64;
		spell.active.cast.duration = 0;
		//spell.active.cast.animation = "spell_engine:one_handed_projectile_charge";

		spell.release = new Spell.Release();
		//spell.release.animation = "spell_engine:one_handed_projectile_release";
		spell.release.sound = new Sound(io.github.rulft44.druids.sounds.ModSounds.DART_RELEASE_ID);

		spell.target.type = Spell.Target.Type.AIM;
		spell.target.aim = new Spell.Target.Aim();

		var debuff2 = createEffectImpact(MRPGCEffects.FATAL_POISON.id, 5);
		debuff2.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		debuff2.action.status_effect.show_particles = false;
		debuff2.action.status_effect.amplifier = 1;
		debuff2.action.status_effect.amplifier_cap_power_multiplier = 0.2F;

		var damage = SpellBuilder.Impacts.damage(0.7F, 0F);
		damage.particles = new ParticleBatch[] {
			new ParticleBatch(
				SpellEngineParticles.MagicParticles.get(
					SpellEngineParticles.MagicParticles.Shape.ARCANE,
					SpellEngineParticles.MagicParticles.Motion.BURST
				).id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
				null, 20, 0.2F, 0.7F, 0.0F, 0F)
		};
		damage.sound = new Sound(io.github.rulft44.druids.sounds.ModSounds.DART_IMPACT_ID.toString(), 1.5F, 1F, 0.1F);
		spell.impacts = List.of(damage, debuff2);

		configureCooldown(spell, 0.5F);
		return new Entry(id, spell, title, description, null);
	}

}
