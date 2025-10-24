package io.github.rulft44.druids.spell;

import io.github.rulft44.druids.Druids;
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
		spell.cost.item.id = "more_rpg_classes:nature_stone";
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

	public static final Entry maul = add(maul());
	private static Entry maul() {
		var id = Identifier.of(Druids.ID, "maul");
		var title = "";
		var description = "";
		var debuffEffect = MRPGCEffects.GRIEVOUS_WOUNDS;
		var spell = SpellBuilder.createSpellActive();
		spell.group = "primary";
		spell.range = 0.5F;
		spell.range_mechanic = Spell.RangeMechanic.MELEE;
		spell.tier = 1;
		spell.school = MoreSpellSchools.NATURE;

		spell.release.animation = "spell_engine:one_handed_throw_release";

		var debuff = createEffectImpact(debuffEffect.id, 5);
		debuff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
		debuff.action.status_effect.show_particles = false;
		debuff.action.status_effect.amplifier = 1;
		debuff.action.status_effect.amplifier_cap = 5;
		debuff.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
		debuff.particles = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.dripping_blood.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
				10, 0.05F, 0.3F),
			/*new ParticleBatch("more_rpg_classes:blood_drop",
				ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
				10, 0.2F, 0.4F),*/
		};

		spell.target.type = Spell.Target.Type.AREA;
		spell.target.area = new Spell.Target.Area();
		spell.target.area.vertical_range_multiplier = 0.5F;
		spell.target.area.angle_degrees = 45;

		var damage = damageImpact(0.65F, 1.0F);
		damage.sound = new Sound(ModSounds.CRIPPLING_STRIKE_ID);

		spell.impacts = List.of(debuff, damage);

		configureCooldown(spell, 15);
		spell.cost.exhaust = 0.3F;
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
		spell.active.cast.duration = 15;
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

		spell.target.type = Spell.Target.Type.AREA;
		spell.target.area = new Spell.Target.Area();
		spell.target.area.vertical_range_multiplier = 1F;
		spell.target.area.angle_degrees = 360;

		spell.release.sound = new Sound(ModSounds.NATURE_IMPACT_1_ID);
		spell.release.particles = new ParticleBatch[]{
			new ParticleBatch(
				SpellEngineParticles.roots.id().toString(),
				ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
				3F, 0, 0)
		};
		spell.release.particles_scaled_with_ranged = new ParticleBatch[]{
			new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
				ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
				1, 0, 0).color(4283786581L)//.scale(0.8F)
		};

		var damage = damageImpact(0.65F, 0);

		spell.impacts = List.of(debuff, damage);

		configureCooldown(spell, 25);
		configureNatureRuneCost(spell);
		return new Entry(id, spell, title, description, null);
	}

}
