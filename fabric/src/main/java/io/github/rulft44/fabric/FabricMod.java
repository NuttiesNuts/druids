package io.github.rulft44.fabric;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Druids.init();
		Druids.registerItems();
		Druids.registerSounds();
		Druids.registerEffects();

		// Modify some loot tables to have a Heart Of The Forest
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (LootTables.JUNGLE_TEMPLE_CHEST == key && source.isBuiltin()) {
				LootPool.Builder pool = LootPool.builder()
					.with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

				tableBuilder.pool(pool);
			}
		});
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (LootTables.SNIFFER_DIGGING_GAMEPLAY == key && source.isBuiltin()) {
				LootPool.Builder pool = LootPool.builder()
					.with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

				tableBuilder.pool(pool);
			}
		});
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (LootTables.WOODLAND_MANSION_CHEST == key && source.isBuiltin()) {
				LootPool.Builder pool = LootPool.builder()
					.with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

				tableBuilder.pool(pool);
			}
		});
    }
}
