package io.github.rulft44.druids.fabric;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;

public final class DruidsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Druids.init();
        Druids.registerItems();
        Druids.registerSounds();
        Druids.registerEffects();

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (LootTables.JUNGLE_TEMPLE_CHEST.equals(id) && source.isBuiltin()) {
                LootPool.Builder pool = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

                tableBuilder.pool(pool);
            }
        });
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (LootTables.SNIFFER_DIGGING_GAMEPLAY.equals(id) && source.isBuiltin()) {
                LootPool.Builder pool = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

                tableBuilder.pool(pool);
            }
        });
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (LootTables.WOODLAND_MANSION_CHEST.equals(id) && source.isBuiltin()) {
                LootPool.Builder pool = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

                tableBuilder.pool(pool);
            }
        });
    }
}
