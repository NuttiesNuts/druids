package io.github.rulft44.druids.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.rpg_series.item.Armor;

public class DruidArmor extends Armor.CustomItem{
	public DruidArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
		super(material, slot, settings);
	}

	public static DruidArmor druid(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
		var armor = new DruidArmor(material, slot, settings);
		return armor;
	}
}
