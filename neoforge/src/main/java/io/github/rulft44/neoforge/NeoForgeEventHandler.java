package io.github.rulft44.neoforge;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.item.ModItems;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;

@EventBusSubscriber(modid = Druids.ID)
public class NeoForgeEventHandler {

	@SubscribeEvent	// Modify some loot tables to have a Heart Of The Forest
	public static void onLootTableLoad(LootTableLoadEvent event) {
		if (LootTables.JUNGLE_TEMPLE_CHEST == event.getKey()) {
			LootPool.Builder pool = LootPool.builder()
				.with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

			event.getTable().addPool(pool.build());
		}
		if (LootTables.SNIFFER_DIGGING_GAMEPLAY == event.getKey()) {
			LootPool.Builder pool = LootPool.builder()
				.with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

			event.getTable().addPool(pool.build());
		}
		if (LootTables.WOODLAND_MANSION_CHEST == event.getKey()) {
			LootPool.Builder pool = LootPool.builder()
				.with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

			event.getTable().addPool(pool.build());
		}
	}
}
