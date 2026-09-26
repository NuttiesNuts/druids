package io.github.rulft44.druids.item;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.item.armor.DruidArmor;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.rpg_series.config.ArmorSetConfig;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Armor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmors {
	public static RegistryEntry<ArmorMaterial> material(String name, int protectionHead, int protectionChest, int protectionLegs, int protectionFeet, int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient) {
		var material = new ArmorMaterial(
			Map.of(
				ArmorItem.Type.HELMET, protectionHead,
				ArmorItem.Type.CHESTPLATE, protectionChest,
				ArmorItem.Type.LEGGINGS, protectionLegs,
				ArmorItem.Type.BOOTS, protectionFeet),
			enchantability, equipSound, repairIngredient,
			List.of(new ArmorMaterial.Layer(Identifier.of(Druids.ID, name))),
			0,0
		);
		return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(Druids.ID, name), material);
	}

	private static final Supplier<Ingredient> WOOL_INGREDIENTS = () -> { return Ingredient.ofItems(
		Items.WHITE_WOOL,
		Items.ORANGE_WOOL,
		Items.MAGENTA_WOOL,
		Items.LIGHT_BLUE_WOOL,
		Items.YELLOW_WOOL,
		Items.LIME_WOOL,
		Items.PINK_WOOL,
		Items.GRAY_WOOL,
		Items.LIGHT_GRAY_WOOL,
		Items.CYAN_WOOL,
		Items.PURPLE_WOOL,
		Items.BLUE_WOOL,
		Items.BROWN_WOOL,
		Items.GREEN_WOOL,
		Items.RED_WOOL,
		Items.BLACK_WOOL);
	};

	public static RegistryEntry<ArmorMaterial> material_t2 = material(
		"druid_armor",
		1, 3, 2, 1,
		10,
		SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, WOOL_INGREDIENTS);

	public static RegistryEntry<ArmorMaterial> material_t3 = material(
		"netherite_druid_armor",
		1, 3, 2, 1,
		15,
		SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, () -> { return Ingredient.ofItems(Items.NETHERITE_INGOT); });

	public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
	private static Armor.Entry create(RegistryEntry<ArmorMaterial> material, Identifier id, int durability, Armor.Set.ItemFactory factory, ArmorSetConfig defaults, int tier) {
		var entry = Armor.Entry.create(
			material,
			id,
			durability,
			factory,
			defaults,
			Equipment.LootProperties.of(tier)
		);
		entries.add(entry);
		return entry;
	}
	
	private static final float spell_power_t2 = 0.25F;
	private static final float spell_power_t3 = 0.3F;
	
	public static final Armor.Set druidArmorSet = create(
		material_t2,
		Identifier.of(Druids.ID, "druid_armor"),
		20,
		DruidArmor::druid,
		ArmorSetConfig.with(
			new ArmorSetConfig.Piece(2)
				.addAll(List.of(
					AttributeModifier.multiply(MoreSpellSchools.NATURE.id, spell_power_t2)
				)),
			new ArmorSetConfig.Piece(4)
				.addAll(List.of(
					AttributeModifier.multiply(MoreSpellSchools.NATURE.id, spell_power_t2)
				)),
			new ArmorSetConfig.Piece(3)
				.addAll(List.of(
					AttributeModifier.multiply(MoreSpellSchools.NATURE.id, spell_power_t2)
				)),
			new ArmorSetConfig.Piece(2)
				.addAll(List.of(
					AttributeModifier.multiply(MoreSpellSchools.NATURE.id, spell_power_t2)
				))
		),
		2)
		.armorSet();
	public static final Armor.Set netheriteDruidArmorSet = create(
		material_t3,
		Identifier.of(Druids.ID, "netherite_druid_armor"),
		30,
		DruidArmor::druid,
		ArmorSetConfig.with(
			new ArmorSetConfig.Piece(2)
				.addAll(List.of(
					AttributeModifier.multiply(MoreSpellSchools.NATURE.id, spell_power_t3)
				)),
			new ArmorSetConfig.Piece(4)
				.addAll(List.of(
					AttributeModifier.multiply(MoreSpellSchools.NATURE.id, spell_power_t3)
				)),
			new ArmorSetConfig.Piece(3)
				.addAll(List.of(
					AttributeModifier.multiply(MoreSpellSchools.NATURE.id, spell_power_t3)
				)),
			new ArmorSetConfig.Piece(2)
				.addAll(List.of(
					AttributeModifier.multiply(MoreSpellSchools.NATURE.id, spell_power_t3)
				))
		),
		3)
		.armorSet();

	public static void register(Map<String, ArmorSetConfig> configs) {
		Armor.register(configs, entries, Group.KEY);
	}
}
