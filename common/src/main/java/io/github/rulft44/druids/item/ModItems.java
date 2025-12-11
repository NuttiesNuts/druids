package io.github.rulft44.druids.item;

import io.github.rulft44.druids.Druids;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.HashMap;

public class ModItems {
	// Misc
	public static final Item COPPER_NUGGET = registerItem("copper_nugget", new Item(new Item.Settings()));
	public static final Item HEART_OF_THE_FOREST = registerItem("heart_of_the_forest", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));

	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(Druids.ID, name), item);
	}

	public static void register(){
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
			entries.addAfter(Items.IRON_NUGGET, ModItems.COPPER_NUGGET);
			entries.addAfter(Items.HEART_OF_THE_SEA, ModItems.HEART_OF_THE_FOREST);
		});
		ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(entries -> {
			entries.addBefore(ModWeapons.natureWand.item(), ModItems.HEART_OF_THE_FOREST);
		});
	}

	public static final HashMap<String, Item> entries;
	static {
		entries = new HashMap<>();
		for(var weaponEntry: ModWeapons.entries) {
			entries.put(weaponEntry.id().toString(), weaponEntry.item());
		}
		for(var entry: ModArmors.entries) {
			var set = entry.armorSet();
			for (var piece: set.pieces()) {
				var armorItem = (ArmorItem) piece;
				entries.put(set.idOf(armorItem).toString(), armorItem);
			}
		}
	}
}
