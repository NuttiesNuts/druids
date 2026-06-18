package io.github.rulft44.druids.item;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.spell.DruidSpells;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.rpg_series.item.Weapon;
import net.spell_engine.rpg_series.item.Weapons;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModWeapons {
	public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();
	private static Weapon.Entry add(Weapon.Entry entry) {
		entries.add(entry);
		return entry;
	}
	private static Supplier<Ingredient> ingredient(String idString, boolean requirement, @Nullable Item fallback) {
		var id = Identifier.of(idString);
		if (requirement) {
			return () -> {
				if (fallback == null) {
					return Ingredient.ofItems(Items.DIRT);
				}
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

	private static final String ARSENAL = "arsenal";

	// MARK: Wands

	public static final Weapon.Entry natureWand = add(Weapons.damageWand(
			Druids.ID, "wand_nature",
			Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.EMERALD),
			List.of(MoreSpellSchools.NATURE.id))
		.spellContainer(SpellContainers.forMagicWeapon().withSpellId(Identifier.of(Druids.ID, "bramble_shot")))
	);

	public static final Weapon.Entry netheriteNatureWand = add(Weapons.damageWand(
			Druids.ID, "wand_netherite_nature",
			Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT),
			List.of(MoreSpellSchools.NATURE.id))
		.spellContainer(SpellContainers.forMagicWeapon().withSpellId(Identifier.of(Druids.ID, "bramble_shot")))
	);

	// MARK: Staves

	public static final Weapon.Entry natureStaff = add(Weapons.damageStaff(
			Druids.ID, "staff_nature",
			Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.EMERALD),
			List.of(MoreSpellSchools.NATURE.id))
		.spellContainer(SpellContainers.forMagicWeapon().withSpellId(DruidSpells.bramble_volley.id()))
	);

	public static final Weapon.Entry netheriteNatureStaff = add(Weapons.damageStaff(
			Druids.ID, "staff_netherite_nature",
			Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT),
			List.of(MoreSpellSchools.NATURE.id))
		.spellContainer(SpellContainers.forMagicWeapon().withSpellId(DruidSpells.bramble_volley.id()))
	);


	// MARK: Register

	public static void register(Map<String, WeaponConfig> configs) {
		if (Druids.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(ARSENAL)) {
			var repair = ingredient(Registries.ITEM.getId(Items.EMERALD).toString(), FabricLoader.getInstance().isModLoaded(ARSENAL), null);
			add(Weapons.damageStaff(Druids.ID, "staff_moon", Equipment.Tier.TIER_5, repair, List.of(MoreSpellSchools.NATURE.id, SpellSchools.HEALING.id))
				.spellContainer(SpellContainers.forMagicWeapon().withSpell(DruidSpells.bramble_volley.id().toString()))
				.withAdditionalSpell(DruidSpells.druid_weapon_tier_5.id().toString())
			);
		}

		Weapon.register(configs, entries, Group.KEY);
	}
}
