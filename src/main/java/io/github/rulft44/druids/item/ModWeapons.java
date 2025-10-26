package io.github.rulft44.druids.item;

import io.github.rulft44.druids.Druids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.api.item.weapon.Weapon;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class ModWeapons {
	public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

	private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType category) {
		var entry = new Weapon.Entry(Druids.ID, name, material, factory, defaults, category);
		if (entry.isRequiredModInstalled()) {
			entries.add(entry);
		}
		return entry;
	}

	private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
		var id = Identifier.of(idString);
		if (requirement) {
			return () -> {
				return Ingredient.ofItems(fallback);
			};
		} else {
			return () -> {
				var item = Registries.ITEM.get(id);
				var ingredient = item != null ? item : fallback;
				return Ingredient.ofItems(ingredient);
			};
		}
	}

	// MARK: Wands

	private static final float wandAttackDamage = 2;
	private static final float wandAttackSpeed = -2.4F;
	private static Weapon.Entry wand(String name, Weapon.CustomMaterial material) {
		return entry(name, material, StaffItem::new, new WeaponConfig(wandAttackDamage, wandAttackSpeed), Equipment.WeaponType.DAMAGE_WAND);
	}

	public static final Weapon.Entry natureWand = wand("wand_nature",
		Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.STICK)))
		.attribute(AttributeModifier.bonus(MoreSpellSchools.NATURE.id, 3F))
		.loot(Equipment.LootProperties.of(2));

	public static final Weapon.Entry netheriteNatureWand = wand("wand_netherite_nature",
		Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
		.attribute(AttributeModifier.bonus(MoreSpellSchools.NATURE.id, 3.5F))
		.loot(Equipment.LootProperties.of(3));

	// MARK: Staves

	private static final float staffAttackDamage = 4;
	private static final float staffAttackSpeed = -3F;

	private static Weapon.Entry staff(String name, Weapon.CustomMaterial material) {
		return entry(name, material, StaffItem::new, new WeaponConfig(staffAttackDamage, staffAttackSpeed), Equipment.WeaponType.DAMAGE_STAFF);
	}

	public static final Weapon.Entry natureStaff = staff("staff_nature",
		Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.EMERALD)))
		.attribute(AttributeModifier.bonus(MoreSpellSchools.NATURE.id, 3.5f))
		.loot(Equipment.LootProperties.of(2));

	public static final Weapon.Entry netheriteNatureStaff = staff("staff_netherite_nature",
		Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
		.attribute(AttributeModifier.bonus(MoreSpellSchools.NATURE.id, 4.5F))
		.loot(Equipment.LootProperties.of(3));

	// MARK: Register

	public static void register(Map<String, WeaponConfig> configs) {
		Weapon.register(configs, entries, Group.KEY);
	}
}
