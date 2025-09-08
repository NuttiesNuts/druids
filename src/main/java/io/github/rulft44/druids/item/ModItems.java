package io.github.rulft44.druids.item;

import io.github.rulft44.druids.Druids;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
	// Rune
	public static final Item NATURE_STONE = registerItem("nature_stone", new Item(new Item.Settings()));

	// Misc
	public static final Item COPPER_NUGGET = registerItem("copper_nugget", new Item(new Item.Settings()));

	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(Druids.ID, name), item);
	}

	public static void register(){
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			entries.add(ModItems.NATURE_STONE);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
			entries.addAfter(Items.IRON_NUGGET, ModItems.COPPER_NUGGET);
		});
	}
}
