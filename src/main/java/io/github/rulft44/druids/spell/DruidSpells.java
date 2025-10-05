package io.github.rulft44.druids.spell;

import io.github.rulft44.druids.Druids;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.effect.MRPGCEffects;
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

}
