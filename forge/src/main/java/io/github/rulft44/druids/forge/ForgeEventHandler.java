package io.github.rulft44.druids.forge;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.item.ModItems;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Druids.ID)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (LootTables.JUNGLE_TEMPLE_CHEST.equals(event.getName())) {
            LootPool.Builder pool = LootPool.builder().with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

            event.getTable().addPool(pool.build());
        }

        if (LootTables.SNIFFER_DIGGING_GAMEPLAY.equals(event.getName())) {
            LootPool.Builder pool = LootPool.builder().with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

            event.getTable().addPool(pool.build());
        }

        if (LootTables.WOODLAND_MANSION_CHEST.equals(event.getName())) {
            LootPool.Builder pool = LootPool.builder().with(ItemEntry.builder(ModItems.HEART_OF_THE_FOREST));

            event.getTable().addPool(pool.build());
        }
    }
}
