package io.github.rulft44.druids.item;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.item.armor.DruidArmor;
import net.archers.content.ArcherSounds;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.ArmorSetConfig;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.armor.Armor;

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

	public static RegistryEntry<ArmorMaterial> material_t1 = material(
		"druid_armor",
		1, 3, 2, 1,
		10,
		ArcherSounds.ARCHER_ARMOR_EQUIP.entry(), WOOL_INGREDIENTS);

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

	private static AttributeModifier damageMultiplier(float value) {
		return null; /*new AttributeModifier(
			EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
			value,
			EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);*/
	}

	public static final float damage_T1 = 0.05F;

	public static final Armor.Set druidArmorSet_T1 = create(
		material_t1,
		Identifier.of(Druids.ID, "druid_armor"),
		15,
		DruidArmor::druid,
		ArmorSetConfig.with(
			new ArmorSetConfig.Piece(2)
				.add(damageMultiplier(damage_T1)),
			new ArmorSetConfig.Piece(3)
				.add(damageMultiplier(damage_T1)),
			new ArmorSetConfig.Piece(3)
				.add(damageMultiplier(damage_T1)),
			new ArmorSetConfig.Piece(2)
				.add(damageMultiplier(damage_T1))
		),
		1)
		.armorSet();

	public static void register(Map<String, ArmorSetConfig> configs) {
		Armor.register(configs, entries, Group.KEY);
	}
}
