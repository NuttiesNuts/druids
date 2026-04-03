package io.github.rulft44.druids.spell;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.effect.ModEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.client.particle.MoreParticles;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.more_rpg_classes.item.MRPGCItems;
import net.more_rpg_classes.sounds.MRPGLibSounds;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_engine.internals.target.SpellTarget;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DruidSpells {
	public enum WeaponGroup { NATURE_STAFF }
	public enum Book { NATURE }
	public record Entry(Identifier id, Spell spell, String title, String description,
						@Nullable SpellTooltip.DescriptionMutator mutator,
						@Nullable List<WeaponGroup> weaponGroups,
						@Nullable Book book) {
		public Entry(Identifier id, Spell spell, String title, String description) {
			this(id, spell, title, description, null, List.of(), null);
		}
		public Entry mutator(SpellTooltip.DescriptionMutator mutator) {
			return new Entry(id, spell, title, description, mutator, weaponGroups, book);
		}
		public Entry weaponGroup(WeaponGroup weaponGroup) {
			var newGroups = new ArrayList<>(weaponGroups != null ? weaponGroups : List.of());
			newGroups.add(weaponGroup);
			return new Entry(id, spell, title, description, mutator, newGroups, book);
		}
		public Entry book(Book book) {
			return new Entry(id, spell, title, description, mutator, weaponGroups, book);
		}
	}


	public static final List<Entry> entries = new ArrayList<>();

	private static Entry add(Entry entry) {
		entries.add(entry);
		return entry;
	}

	private static Spell modifierSpellBase() {
		var spell = new Spell();
		spell.range = 0;
		spell.tier = 1;

		spell.type = Spell.Type.MODIFIER;

		spell.tooltip = new Spell.Tooltip();
		spell.tooltip.name = new Spell.Tooltip.LineOptions(false, true);
		spell.tooltip.description.color = Formatting.GRAY.asString();
		spell.tooltip.description.show_in_compact = true;
		spell.tooltip.name.show_in_compact = false;
		spell.tooltip.name.show_in_details = false;
		spell.tooltip.show_header = false;

		return spell;
	}
	private static Spell.Impact.TargetModifier createImpactModifier(String entityType) {
		var condition = new Spell.TargetCondition();
		condition.entity_type = entityType;
		var modifier = new Spell.Impact.TargetModifier();
		modifier.conditions = List.of(condition);
		return modifier;
	}
	private static Spell createModifierAlikePassiveSpell() {
		var spell = SpellBuilder.createSpellPassive();
		spell.range = 0;
		spell.tooltip = new Spell.Tooltip();
		spell.tooltip.show_activation = false;
		return spell;
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
	private static Spell.Impact createHeal(float coefficient) {
		var buff = new Spell.Impact();
		buff.action = new Spell.Impact.Action();
		buff.action.type = Spell.Impact.Action.Type.HEAL;
		buff.action.heal = new Spell.Impact.Action.Heal();
		buff.action.heal.spell_power_coefficient = coefficient;
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
	private static Spell.Delivery.Cloud makeRootCluster(String model, float scale, int delay, float distance_from_caster, int placement_angle, int rotation
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

	private static final Identifier HEALING_PARTICLES = SpellEngineParticles.MagicParticles.get(
		SpellEngineParticles.MagicParticles.Shape.HEAL,
		SpellEngineParticles.MagicParticles.Motion.ASCEND).id();

	// region Weapon Skills
	public static final Entry weapon_nature_root = add(weapon_nature_root());
	private static Entry weapon_nature_root() {
		var id = Identifier.of(Druids.ID, "weapon_nature_root");
		var title = "Nature Mastery";
		var description = "Bramble Shot deals {knockback_multiply_base} increased knockback.";
		var spell = SpellBuilder.createSpellModifier();
		spell.school = MoreSpellSchools.NATURE;

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = bramble_shot().id.toString();
		modifier.power_modifier = new Spell.Impact.Modifier();
		modifier.knockback_multiply_base = 0.5f;
		spell.modifiers = List.of(modifier);

		return new Entry(id, spell, title, description);
	}

	public static Entry weapon_bramble_volley_modifier_1 = add(weapon_bramble_volley_modifier_1());
	private static Entry weapon_bramble_volley_modifier_1() {
		var id = Identifier.of(Druids.ID, "weapon_bramble_volley_modifier_1");
		var title = "Mass Bramble Volley";
		var description = "Increases the maximum number of bramble projectiles by 1.";
		var spell = modifierSpellBase();
		spell.school = MoreSpellSchools.NATURE;

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = bramble_shot().id.toString();
		var modifier2 = new Spell.Modifier();
		modifier2.spell_pattern = bramble_volley().id.toString();

		modifier.power_modifier = new Spell.Impact.Modifier();
		modifier2.power_modifier = new Spell.Impact.Modifier();

		modifier.channel_ticks_add = 1;
		modifier2.channel_ticks_add = 1;

		spell.modifiers = List.of(modifier, modifier2);

		return new Entry(id, spell, title, description);
	}

	public static Entry weapon_bramble_volley_modifier_2 = add(weapon_bramble_volley_modifier_2());
	private static Entry weapon_bramble_volley_modifier_2() {
		var id = Identifier.of(Druids.ID, "weapon_bramble_volley_modifier_2");
		var title = "Poison Tip";
		var description = "Increases the poison duration of Bramble Volley by {effect_duration_add} seconds.";
		var spell = modifierSpellBase();
		spell.school = MoreSpellSchools.NATURE;

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = bramble_volley().id.toString();

		modifier.power_modifier = new Spell.Impact.Modifier();

		modifier.effect_duration_add = 2;

		spell.modifiers = List.of(modifier);

		return new Entry(id, spell, title, description);
	}
	// endregion

	// region Main Spells

	public static final Entry bramble_volley = add(bramble_volley());
	private static Entry bramble_volley() {
		var id = Identifier.of(Druids.ID, "bramble_volley");
		var title = "Bramble Volley";
		var description = "Launches 3 woods projectiles, dealing up to {damage} nature spell damage applying Fatal Poison and Grievous Wounds.";
		var spell = SpellBuilder.createWeaponSpell();
		spell.group = "primary";
		spell.range = 64F;
		spell.tier = 1;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.duration = 0.3F;
		spell.active.cast.channel_ticks = 3;

		spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_charge");
		spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_release");

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
			new Spell.Delivery.ShootProjectile.DirectionOffset(0.0F, 2.0F),   // center
			new Spell.Delivery.ShootProjectile.DirectionOffset(10.0F, 0.0F),   // right
			new Spell.Delivery.ShootProjectile.DirectionOffset(0.0F, -2.0F),   // center
		};
		spell.deliver.projectile.launch_properties.sound = new Sound(MRPGLibSounds.NATURE_RELEASE_1.id().toString(), 0.75F, 1F, 0.1F);

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
			projectile.client_data.model.model_id = "druids:spell_projectile/bramble_shot";
			projectile.client_data.model.scale = 0.5F;
			spell.deliver.projectile.projectile = projectile;

		var damage = damageImpact(0.50F, 0.8F);
		damage.sound = new Sound(MRPGLibSounds.CRIPPLING_STRIKE.id().toString(), 0.5F, 1F, 0.1F);

		spell.impacts = List.of(debuff1, debuff2, damage);

		configureCooldown(spell, 3);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description).weaponGroup(WeaponGroup.NATURE_STAFF);
	}

	public static final Entry bramble_shot = add(bramble_shot());
	private static Entry bramble_shot() {
		var id = Identifier.of(Druids.ID, "bramble_shot");
		var title = "Bramble Shot";
		var description = "Launches a wood projectile, dealing up to {damage} nature spell damage and applying Fatal Poison";
		var spell = SpellBuilder.createWeaponSpell();
		spell.group = "primary";
		spell.range = 28F;
		spell.tier = 0;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.duration = 0.2F;
		spell.active.cast.channel_ticks = 1;

		spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_charge");
		spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_release");
		spell.active.cast.sound = new Sound(MRPGLibSounds.NATURE_CAST_1.id());

		var poison = createEffectImpact(MRPGCEffects.FATAL_POISON.id, 5);
		poison.action.status_effect.amplifier = 1;
		poison.action.status_effect.show_particles = false;
		poison.action.status_effect.amplifier_cap = 5;
		poison.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
		poison.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;

		spell.target.type = Spell.Target.Type.AIM;
		spell.target.aim = new Spell.Target.Aim();

		spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
		spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
		spell.deliver.projectile.launch_properties.velocity = 1F;

		spell.deliver.projectile.launch_properties.sound = new Sound(MRPGLibSounds.NATURE_RELEASE_1.id().toString(), 0.75F, 1F, 0.1F);

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
			projectile.client_data.model.model_id = "druids:spell_projectile/bramble_shot";
			projectile.client_data.model.scale = 0.5F;
			spell.deliver.projectile.projectile = projectile;

		var damage = damageImpact(0.5F, 0.8F);
		damage.sound = new Sound(MRPGLibSounds.CRIPPLING_STRIKE.id().toString(), 0.5F, 1F, 0.1F);
		damage.particles =
			new ParticleBatch[]{new ParticleBatch(SpellEngineParticles.dripping_blood.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
				10, 0.05F, 0.3F)};

		spell.impacts = List.of(poison, damage);

		configureCooldown(spell, 2);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description).weaponGroup(WeaponGroup.NATURE_STAFF);
	}

	public static final Entry barkskin = add(barkskin());
	private static Entry barkskin() {
		var id = Identifier.of(Druids.ID, "barkskin");
		var title = "Barkskin";
		var description = "Gives the caster some defense and thorns.";
		var spell = SpellBuilder.createSpellActive();
		spell.range = 0F;
		spell.tier = 2;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.duration = 0.75F;
		spell.active.cast.particles = natureCastingParticles();
		spell.active.cast.sound = new Sound(MRPGLibSounds.NATURE_CAST_1.id());
		spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_area_charge");

		spell.release.sound = Sound.withVolume(MRPGLibSounds.NATURE_RELEASE_1.id(), 0.5F);
		spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_ground_release");

		spell.target.type = Spell.Target.Type.AREA;
		spell.target.area = new Spell.Target.Area();
		spell.target.area.vertical_range_multiplier = 1;
		spell.target.area.include_caster = true;

		var effect = createEffectImpact(ModEffects.THORNED.id, 5);
		effect.action.status_effect.duration = 17;
		effect.action.status_effect.amplifier = 1;
		effect.action.status_effect.amplifier_cap_power_multiplier = 0.10F;

		spell.release.particles_scaled_with_ranged = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
				1, 0, 0).color(0x56211a)
		};

		spell.impacts = List.of(effect);

		spell.cost.exhaust = 0.5F;
		configureCooldown(spell, 20);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description).book(Book.NATURE);
	}

	public static final Entry vine_whip = add(vine_whip());
	private static Entry vine_whip() {
		var id = Identifier.of(Druids.ID, "vine_whip");
		var title = "Vine Whip";
		var description = "Send forth vines that pull and poison an enemy.";
		var spell = SpellBuilder.createSpellActive();
		spell.range = 16F;
		spell.tier = 3;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.duration = 0.75F;
		spell.active.cast.particles = natureCastingParticles();
		spell.active.cast.sound = new Sound(MRPGLibSounds.NATURE_CAST_1.id());
		spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_healing_charge");

		spell.release.sound = new Sound(MRPGLibSounds.NATURE_RELEASE_2.id());
		spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");

		spell.target.type = Spell.Target.Type.AIM;
		spell.target.aim = new Spell.Target.Aim();
		spell.target.aim.sticky = true;

		var poison = createEffectImpact(MRPGCEffects.FATAL_POISON.id, 5);
		poison.action.status_effect.amplifier = 2;
		poison.action.status_effect.amplifier_cap_power_multiplier = 0.5F;
		poison.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;

		var pull = customImpact("more_rpg_classes:pull_to_caster_direct", SpellTarget.Intent.HARMFUL);
		pull.sound = new Sound(MRPGLibSounds.NATURE_IMPACT_1.id());

		spell.impacts = List.of(poison, pull);

		configureCooldown(spell, 8);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description).book(Book.NATURE);
	}

	public static final Entry mass_entanglement = add(mass_entanglement());
	private static Entry mass_entanglement() {
		var id = Identifier.of(Druids.ID, "mass_entanglement");
		var title = "Mass Entanglement";
		var description = "Roots, damages and poisons all enemies in range.";
		var debuffEffect = SpellEngineEffects.STUN;
		var spell = SpellBuilder.createSpellActive();
		spell.range = 10F;
		spell.tier = 4;
		spell.school = MoreSpellSchools.NATURE;

		spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_area_charge");
		spell.active.cast.particles = natureCastingParticles();
		spell.active.cast.duration = 10;
		spell.active.cast.sound = new Sound(MRPGLibSounds.NATURE_CAST_1.id());

		spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");

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

		var outer1 = makeRootCluster("druids:spell_effect/entanglement1", 1.2F, 8, 3F, 0, 0);
		var outer2 = makeRootCluster("druids:spell_effect/entanglement11", 1.2F, 9, 3F, 90, 0);
		var outer3 = makeRootCluster("druids:spell_effect/entanglement1", 1.2F, 10, 3F, 180, 0);
		var outer4 = makeRootCluster("druids:spell_effect/entanglement11", 1.2F, 11, 3F, 270, 0);

		var inner1 = makeRootCluster("druids:spell_effect/entanglement2", 1F, 8, 2F, 0, 0);
		var inner2 = makeRootCluster("druids:spell_effect/entanglement2", 1F, 9, 2.5F, 110, 0);
		var inner3 = makeRootCluster("druids:spell_effect/entanglement2", 1F, 10, 2F, 180, 0);
		var inner4 = makeRootCluster("druids:spell_effect/entanglement2", 1F, 11, 1.75F, 300, 0);

		var flower1 = makeRootCluster("druids:spell_effect/poppy", 1F, 14, 1F, 0, 0);
		var flower2 = makeRootCluster("druids:spell_effect/dandelion", 1F, 15, 1F, 180, 0);


		spell.deliver.clouds = List.of(outer1,outer2,outer3,outer4,inner1,inner2,inner3,inner4,flower1,flower2);

		spell.release.sound = new Sound(MRPGLibSounds.NATURE_IMPACT_3.id());

		spell.release.particles_scaled_with_ranged = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
				1, 0, 0).color(Color.NATURE.toRGBA())
		};

		var damage = damageImpact(0.15F, 0F);

		spell.impacts = List.of(debuff, debuff2, damage);

		configureCooldown(spell, 25);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description).book(Book.NATURE);
	}

	public static Entry dart_shot = add(dart_shot());
	private static Entry dart_shot() {
		var id = Identifier.of(Druids.ID, "dart_shot");
		var spell = SpellBuilder.createSpellActive();
		var title = "Dart Shot";
		var description = "Shoots a dart that deals {damage} damage.";
		spell.school = MoreSpellSchools.NATURE;
		spell.group = "primary";
		spell.tier = 0;
		spell.range = 64;
		spell.active.cast.duration = 0;
		//spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_charge");

		spell.release = new Spell.Release();
		//spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_release");
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
		return new Entry(id, spell, title, description);
	}
	// endregion

	// region Modifiers
	public static Entry druid_tier_2_spell_1_modifier_1 = add(druid_tier_2_spell_1_modifier_1());
	private static Entry druid_tier_2_spell_1_modifier_1() {
		var id = Identifier.of(Druids.ID, "druid_tier_2_spell_1_modifier_1");
		var title = "Friend of Nature";
		var description = "Barkskin can be casted on allies";
		var spell = modifierSpellBase();
		spell.school = MoreSpellSchools.NATURE;

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = barkskin().id.toString();

		modifier.power_modifier = new Spell.Impact.Modifier();

		modifier.range_add = 16;

		spell.modifiers = List.of(modifier);

		return new Entry(id, spell, title, description);
	}

	public static Entry druid_tier_2_spell_1_modifier_2 = add(druid_tier_2_spell_1_modifier_2());
	private static Entry druid_tier_2_spell_1_modifier_2() {
		var id = Identifier.of(Druids.ID, "druid_tier_2_spell_1_modifier_2");
		var title = "Poisonous Bark";
		var description = "Barkskin has {impact_chance} chance to fatally poison you but increase nature spell power by {bonus} for {effect_duration} seconds.";
		var spell = modifierSpellBase();
		var effect = ModEffects.POISON_RITUAL;
		spell.school = MoreSpellSchools.NATURE;
		SpellTooltip.DescriptionMutator mutator = (args) -> {
			var modifier = effect.config().firstModifier();
			var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
			return args.description()
				.replace("{bonus}", bonus);
		};

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = barkskin().id.toString();

		modifier.power_modifier = new Spell.Impact.Modifier();

		var impact = createEffectImpact(effect.id, 10);
		impact.action.status_effect.amplifier_cap = 0;
		impact.action.apply_to_caster = true;
		impact.chance = 0.15F;

		modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
		modifier.impacts = List.of(impact);

		spell.modifiers = List.of(modifier);

		return new Entry(id, spell, title, description).mutator(mutator);
	}

	public static Entry druid_tier_3_spell_1_modifier_1 = add(druid_tier_3_spell_1_modifier_1());
	private static Entry druid_tier_3_spell_1_modifier_1() {
		var id = Identifier.of(Druids.ID, "druid_tier_3_spell_1_modifier_1");
		var title = "Frequent Vines";
		var description = "Reduces the cooldown of Vine Whip by {cooldown_duration_deduct} sec.";
		var spell = modifierSpellBase();
		spell.school = MoreSpellSchools.NATURE;

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = vine_whip().id.toString();

		modifier.power_modifier = new Spell.Impact.Modifier();

		modifier.cooldown_duration_deduct = 0.25F;

		spell.modifiers = List.of(modifier);

		return new Entry(id, spell, title, description);
	}

	public static Entry druid_tier_3_spell_1_modifier_2 = add(druid_tier_3_spell_1_modifier_2());
	private static Entry druid_tier_3_spell_1_modifier_2() {
		var id = Identifier.of(Druids.ID, "druid_tier_3_spell_1_modifier_2");
		var title = "Whip of Life";
		var description = "Vine Whip heals you for {heal} health.";
		var spell = modifierSpellBase();
		spell.school = MoreSpellSchools.NATURE;

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = vine_whip().id.toString();

		modifier.power_modifier = new Spell.Impact.Modifier();

		var impact = createHeal(0.1F);
		impact.action.apply_to_caster = true;
		impact.particles = new ParticleBatch[]{
			new ParticleBatch(
				HEALING_PARTICLES.toString(),
				ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
				20, 0.02F, 0.15F)
				.color(Color.NATURE.toRGBA()),
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
				1, 0, 0).color(Color.NATURE.toRGBA()).scale(2F)
		};
		impact.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());

		modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
		modifier.impacts = List.of(impact);

		spell.modifiers = List.of(modifier);

		return new Entry(id, spell, title, description);
	}

	public static Entry druid_tier_4_spell_1_modifier_1 = add(druid_tier_4_spell_1_modifier_1());
	private static Entry druid_tier_4_spell_1_modifier_1() {
		var id = Identifier.of(Druids.ID, "druid_tier_4_spell_1_modifier_1");
		var title = "Font of Life";
		var description = "Mass Entanglement heals you and allies for {heal} health.";
		var spell = modifierSpellBase();
		spell.school = MoreSpellSchools.NATURE;

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = mass_entanglement().id.toString();

		modifier.power_modifier = new Spell.Impact.Modifier();

		var impact = createHeal(0.25F);
		impact.action.apply_to_caster = true;
		impact.particles = new ParticleBatch[]{
			new ParticleBatch(
				HEALING_PARTICLES.toString(),
				ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
				20, 0.02F, 0.15F)
				.color(Color.NATURE.toRGBA()),
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
				1, 0, 0).color(Color.NATURE.toRGBA()).scale(mass_entanglement().spell.range)
		};
		impact.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());

		modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
		modifier.impacts = List.of(impact);

		spell.modifiers = List.of(modifier);

		return new Entry(id, spell, title, description);
	}

	public static Entry druid_tier_4_spell_1_modifier_2 = add(druid_tier_4_spell_1_modifier_2());
	private static Entry druid_tier_4_spell_1_modifier_2() {
		var id = Identifier.of(Druids.ID, "druid_tier_4_spell_1_modifier_2");
		var title = "More Entanglement";
		var description = "Reduces the cooldown of Mass Entanglement by {cooldown_duration_deduct} sec.";
		var spell = modifierSpellBase();
		spell.school = MoreSpellSchools.NATURE;

		var modifier = new Spell.Modifier();
		modifier.spell_pattern = mass_entanglement().id.toString();

		modifier.power_modifier = new Spell.Impact.Modifier();

		modifier.cooldown_duration_deduct = 4;

		spell.modifiers = List.of(modifier);

		return new Entry(id, spell, title, description);
	}
    // endregion

	// region Passives

	public static final Entry druid_tier_1_passive_1 = add(druid_tier_1_passive_1());
	private static Entry druid_tier_1_passive_1() {
		var id = Identifier.of(Druids.ID, "druid_tier_1_passive_1");
		var title = "Blessed Roots";
		var description = "Nature spell impacts have {trigger_chance} chance to heal you and targets around you for {heal} health.";
		var spell = SpellBuilder.createSpellPassive();
		spell.school = MoreSpellSchools.NATURE;
		spell.range = 4;

		spell.target.type = Spell.Target.Type.AREA;
		spell.target.area = new Spell.Target.Area();
		spell.target.area.vertical_range_multiplier = 1;
		spell.target.area.include_caster = true;

		var trigger = SpellBuilder.Triggers.activeSpellHit(0.15F, "nature");
		trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
		spell.passive.triggers = List.of(trigger);

		var impact = createHeal(0.1F);
		impact.action.apply_to_caster = true;
		impact.particles = new ParticleBatch[]{
			new ParticleBatch(
				HEALING_PARTICLES.toString(),
				ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
				20, 0.02F, 0.15F)
				.color(Color.NATURE.toRGBA()),
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
				1, 0, 0).color(Color.NATURE.toRGBA()).scale(2F)
		};
		impact.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());
		spell.impacts = List.of(impact);

		SpellBuilder.Cost.cooldown(spell, 1F);

		return new Entry(id, spell, title, description);
	}

	public static final Entry druid_tier_1_passive_2 = add(druid_tier_1_passive_2());
	private static Entry druid_tier_1_passive_2() {
		var id = Identifier.of(Druids.ID, "druid_tier_1_passive_2");
		var title = "Toxic Bloom";
		var description = "Nature spell hits have {trigger_chance} chance to spawn a spore cloud, spreading Fatal Poison to nearby enemies.";
		var spell = SpellBuilder.createSpellPassive();
		spell.school = MoreSpellSchools.NATURE;
		spell.range = 20;

		var trigger = SpellBuilder.Triggers.activeSpellHit(0.15F, "nature");
		spell.passive.triggers = List.of(trigger);

		spell.deliver.type = Spell.Delivery.Type.CLOUD;
		var cloud = new Spell.Delivery.Cloud();
		cloud.volume.radius = 2.5F;
		cloud.volume.area.vertical_range_multiplier = 1F;
		cloud.impact_tick_interval = 20;
		cloud.time_to_live_seconds = 4;
		cloud.client_data = new Spell.Delivery.Cloud.ClientData();
		cloud.client_data.particles = new ParticleBatch[]{
			new ParticleBatch(
				SpellEngineParticles.ground_glow.toString(),
				ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
				1, 0.1F, 0.3F).color(Color.from(6107020).toRGBA()),
			new ParticleBatch(
				SpellEngineParticles.MagicParticles.get(
					SpellEngineParticles.MagicParticles.Shape.SKULL,
					SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
				ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
				2, 0.1F, 0.15F).color(Color.from(6107020).toRGBA())
		};
		spell.deliver.clouds = List.of(cloud);

		var poison = createEffectImpact(MRPGCEffects.FATAL_POISON.id, 4);
		poison.action.status_effect.amplifier = 1;
		poison.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		poison.action.status_effect.show_particles = false;

		spell.impacts = List.of(poison);

		SpellBuilder.Cost.cooldown(spell, 3F);

		return new Entry(id, spell, title, description);
	}

	public static final Entry druid_tier_2_passive_1 = add(druid_tier_2_passive_1());
	private static Entry druid_tier_2_passive_1() {
		var id = Identifier.of(Druids.ID, "druid_tier_2_passive_1");
		var title = "Rootstride";
		var description = "{trigger_chance} chance upon rolling to leave roots behind for {cloud_duration} sec.";

		var spell = SpellBuilder.createSpellPassive();
		spell.school = MoreSpellSchools.NATURE;
		spell.range = 0;

		var trigger = SpellBuilder.Triggers.roll();
		trigger.chance = 0.5F;
		spell.passive.triggers = List.of(trigger);

		spell.deliver.type = Spell.Delivery.Type.CLOUD;
		var cloud = new Spell.Delivery.Cloud();
		cloud.volume.radius = 1F;
		cloud.volume.area.vertical_range_multiplier = 0.3F;
		cloud.volume.sound = new Sound(SpellEngineSounds.POISON_CLOUD_TICK.id());
		cloud.impact_tick_interval = 15;
		cloud.time_to_live_seconds = 8;
		cloud.client_data = new Spell.Delivery.Cloud.ClientData();
		cloud.client_data.particles = new ParticleBatch[]{
			new ParticleBatch(
				SpellEngineParticles.roots.id().toString(),
				ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
				2, 0, 0)
		};
		spell.deliver.clouds = List.of(cloud);

		var debuff = createEffectImpact(SkillEffects.NATURES_GRASP.id, 1);
		debuff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
		debuff.action.status_effect.apply_limit = new Spell.Impact.Action.StatusEffect.ApplyLimit();
		debuff.action.status_effect.apply_limit.health_base = 50;
		debuff.action.status_effect.apply_limit.spell_power_multiplier = 5;
		debuff.particles = new ParticleBatch[]{
			new ParticleBatch("falling_spore_blossom",
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
				2, 0.1F, 0.2F)
		};
		spell.impacts = List.of(debuff);

		return new Entry(id, spell, title, description);
	}

	public static final Entry druid_tier_2_passive_2 = add(druid_tier_2_passive_2());
	private static Entry druid_tier_2_passive_2() {
		var id = Identifier.of(Druids.ID, "druid_tier_2_passive_2");
		var title = "Thorn Rush";
		var description = "{trigger_chance} chance upon rolling to give the caster thorns for {effect_duration} sec.";

		var spell = SpellBuilder.createSpellPassive();
		spell.school = MoreSpellSchools.NATURE;
		spell.range = 0;

		var trigger = SpellBuilder.Triggers.roll();
		trigger.chance = 0.5F;
		spell.passive.triggers = List.of(trigger);

		spell.target.type = Spell.Target.Type.AREA;
		spell.target.area = new Spell.Target.Area();
		spell.target.area.vertical_range_multiplier = 1;
		spell.target.area.include_caster = true;

		var effect = createEffectImpact(ModEffects.THORNED.id, 5);
		effect.sound = Sound.withVolume(MRPGLibSounds.NATURE_RELEASE_1.id(), 0.5F);
		effect.action.status_effect.duration = 17;
		effect.action.status_effect.amplifier = 1;
		effect.action.status_effect.amplifier_cap_power_multiplier = 0.10F;

		spell.release.particles_scaled_with_ranged = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
				1, 0, 0).color(0x56211a)
		};

		spell.impacts = List.of(effect);

		return new Entry(id, spell, title, description);
	}

	public static final Entry druid_tier_3_passive_1 = add(druid_tier_3_passive_1());
	private static Entry druid_tier_3_passive_1() {
		var id = Identifier.of(Druids.ID, "druid_tier_3_passive_1");
		var title = "Guiding Thorn";
		var description = "Upon taking damage, {trigger_chance} chance to shoot a guiding thorn that applies fatal poison for {effect_duration} sec.";

		var spell = SpellBuilder.createSpellPassive();
		spell.school = MoreSpellSchools.NATURE;
		spell.range = 20;

		var trigger = SpellBuilder.Triggers.damageTaken();
		trigger.chance = 0.5F;
		spell.passive.triggers = List.of(trigger);

		spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
		spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
		spell.deliver.projectile.launch_properties.sound = new Sound(MRPGLibSounds.NATURE_RELEASE_1.id());
		spell.deliver.projectile.launch_properties.velocity = 0.5F;
		spell.deliver.projectile.projectile = new Spell.ProjectileData();
		spell.deliver.projectile.projectile.homing_angle = 1;
		spell.deliver.projectile.projectile.perks = new Spell.ProjectileData.Perks();
		spell.deliver.projectile.projectile.perks.pierce = 999;
		spell.deliver.projectile.projectile.perks.ricochet_range = 0F;
		spell.deliver.projectile.projectile.perks.ricochet = 0;
		spell.deliver.projectile.projectile.perks.bounce = 0;

		var model = new Spell.ProjectileModel();
		model.light_emission = LightEmission.NONE;
		model.model_id = "druids:spell_projectile/thorn";
		model.scale = 1.0F;
		model.rotate_degrees_per_tick = 0;

		spell.deliver.projectile.projectile.client_data = new Spell.ProjectileData.Client();
		spell.deliver.projectile.projectile.client_data.model = model;


		var poison = createEffectImpact(MRPGCEffects.FATAL_POISON.id, 5);
		poison.action.status_effect.amplifier = 1;
		poison.action.status_effect.show_particles = false;
		poison.action.status_effect.amplifier_cap = 5;
		poison.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
		poison.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;

		spell.impacts = List.of(poison);

		SpellBuilder.Cost.cooldown(spell, 2F);

		return new Entry(id, spell, title, description);
	}

	public static final Entry druid_tier_3_passive_2 = add(druid_tier_3_passive_2());
	private static Entry druid_tier_3_passive_2() {
		var id = Identifier.of(Druids.ID, "druid_tier_3_passive_2");
		var title = "Surge of the Wild";
		var description = "Hitting with a nature spell has {trigger_chance} chance to immobilize targets for {effect_duration}.";
		var spell = SpellBuilder.createSpellPassive();
		spell.school = MoreSpellSchools.NATURE;
		spell.range = 0;

		spell.target.type = Spell.Target.Type.FROM_TRIGGER;

		var trigger = SpellBuilder.Triggers.activeSpellHit(0.5F, "nature");
		trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
		spell.passive.triggers = List.of(trigger);

		var effect = createEffectImpact(SpellEngineEffects.IMMOBILIZE.id, 2);
		effect.action.status_effect.amplifier = 0;
		effect.action.apply_to_caster = false;

		spell.impacts = List.of(effect);

		SpellBuilder.Cost.cooldown(spell, 0.5F);
		return new Entry(id, spell, title, description);
	}
	// endregion
}
