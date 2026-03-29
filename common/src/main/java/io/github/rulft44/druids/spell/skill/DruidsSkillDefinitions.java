package io.github.rulft44.druids.spell.skill;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.spell.DruidSpells;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.puffish.skillsmod.common.IconType;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainers;

import java.util.ArrayList;
import java.util.List;

public class DruidsSkillDefinitions {
	public static final Identifier CATEGORY_ID = Identifier.of(Druids.ID, "druids");
	public record Icon(IconType type, String value, String modelId) {
		public static Icon texture(String texture) {
			return new Icon(IconType.TEXTURE, texture, null);
		}
		public static Icon item(String item) {
			return new Icon(IconType.ITEM, item, null);
		}
		public static Icon itemWithModel(String item, String modelId) {
			return new Icon(IconType.ITEM, item, modelId);
		}
		public static Icon effect(String effect) {
			return new Icon(IconType.EFFECT, effect, null);
		}
		public static Icon spell(Identifier spellId) {
			return texture(spellId.getNamespace() + ":textures/spell/" + spellId.getPath() + ".png");
		}
	}
	public record EntityAttributeReward(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
		public static EntityAttributeReward of(RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation) {
			return new EntityAttributeReward(attribute, new EntityAttributeModifier(Identifier.of(Druids.ID + ":attribute_reward"), value, operation));
		}
	}
	public record Entry(String id, String title, String description, Icon icon, List<SpellContainer> spellReward, EntityAttributeReward attributeReward, List<String> required_mods) {
		public static Entry spell(String id, String title, String description, Icon icon, List<SpellContainer> spellReward) {
			return new Entry(id, title, description, icon, spellReward, null, null);
		}
		public static Entry attribute(String id, String title, String description, Icon icon,
									  RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation) {
			return attribute(id, title, description, icon, EntityAttributeReward.of(attribute, value, operation));
		}
		public static Entry attribute(String id, String title, String description, Icon icon, EntityAttributeReward attributeReward) {
			return new Entry(id, title, description, icon, null, attributeReward, null);
		}
		public String titleTranslationKey() {
			return "skill." + Druids.ID + "." + id + ".title";
		}
		public String descriptionTranslationKey() {
			return "skill." + Druids.ID + "." + id + ".description";
		}
		public Entry withIcon(Icon icon) {
			return new Entry(id, title, description, icon, spellReward, attributeReward, required_mods);
		}
		public Entry require(String modId) {
			return new Entry(id, title, description, icon, spellReward, attributeReward, List.of(modId));
		}
	}
	public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
	private static Entry add(Entry entry) {
		ENTRIES.add(entry);
		return entry;
	}

	public static final String DRUIDS = "druids";

	public static final float ROOT_MULTIPLIER = 0.01f;
	public static final float BOOST_MULTIPLIER = 0.01f;

	private static List<SpellContainer> dummyContainer() {
		return List.of(SpellContainers.forModifier(Identifier.of("wizards:fireball")));
	}

	private static Entry modifierSpell(DruidSpells.Entry entry) {
		var modifiedSpellId = Identifier.of(entry.spell().modifiers.getFirst().spell_pattern);
		return Entry.spell(entry.id().getPath(),
			entry.title(),
			null,
			Icon.spell(modifiedSpellId),
			List.of(SpellContainers.forModifier(entry.id()))
		);
	}

	private static Entry passiveSpell(DruidSpells.Entry entry) {
		return Entry.spell(entry.id().getPath(),
			entry.title(),
			null,
			Icon.spell(entry.id()),
			List.of(SpellContainers.forModifier(entry.id()))
		);
	}
	
	///DRUID
	public static final Entry NATURE_ROOT = add(
		Entry.attribute("nature_root",
			"Path of Nature",
			null,
			Icon.itemWithModel("spell_engine:spell_book", "druids:item/spell_book/nature"),
			MoreSpellSchools.NATURE.attributeEntry,
			0.01,
			EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
		).require(DRUIDS)
	);
	public static final Entry NATURE_BOOST = add(
		Entry.attribute("nature_boost",
			"Nature Attunement",
			null,
			Icon.item("druids:wand_nature"),
			NATURE_ROOT.attributeReward()).require(DRUIDS)
	);
	public static final Entry NATURE_SPEC_A_MODIFIER_1 = add(modifierSpell(DruidSpells.nature_spec_a_modifier_1).require(DRUIDS));
	public static final Entry NATURE_SPEC_B_MODIFIER_1 = add(modifierSpell(DruidSpells.nature_spec_b_modifier_1).require(DRUIDS));
	public static final Entry NATURE_SPEC_A_MODIFIER_2 = add(modifierSpell(DruidSpells.nature_spec_a_modifier_2).require(DRUIDS));
	public static final Entry NATURE_SPEC_B_MODIFIER_2 = add(modifierSpell(DruidSpells.nature_spec_b_modifier_2).require(DRUIDS));
	public static final Entry NATURE_SPEC_A_MODIFIER_3 = add(modifierSpell(DruidSpells.nature_spec_a_modifier_3).require(DRUIDS));
	public static final Entry NATURE_SPEC_B_MODIFIER_3 = add(modifierSpell(DruidSpells.nature_spec_b_modifier_3).require(DRUIDS));
	public static final Entry NATURE_SPEC_A_MODIFIER_4 = add(modifierSpell(DruidSpells.nature_spec_a_modifier_4).require(DRUIDS));
	public static final Entry NATURE_SPEC_B_MODIFIER_4 = add(modifierSpell(DruidSpells.nature_spec_b_modifier_4).require(DRUIDS));
	public static final Entry NATURE_SPEC_A_PASSIVE_1 = add(passiveSpell(DruidSpells.nature_spec_a_passive_1).require(DRUIDS));
	public static final Entry NATURE_SPEC_B_PASSIVE_1 = add(passiveSpell(DruidSpells.nature_spec_b_passive_1).require(DRUIDS));
	public static final Entry NATURE_SPEC_A_PASSIVE_2 = add(passiveSpell(DruidSpells.nature_spec_a_passive_2).require(DRUIDS));
	public static final Entry NATURE_SPEC_B_PASSIVE_2 = add(passiveSpell(DruidSpells.nature_spec_b_passive_2).require(DRUIDS));
//	public static final Entry NATURE_SPEC_A_PASSIVE_3 = add(passiveSpell(DruidSpells.nature_spec_a_passive_3).require(DRUIDS));
//	public static final Entry NATURE_SPEC_B_PASSIVE_3 = add(passiveSpell(DruidSpells.nature_spec_b_passive_3).require(DRUIDS));
}
