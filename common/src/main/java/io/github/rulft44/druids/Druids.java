package io.github.rulft44.druids;

import io.github.rulft44.druids.config.Default;
import io.github.rulft44.druids.config.TweaksConfig;
import io.github.rulft44.druids.effect.ModEffects;
import io.github.rulft44.druids.item.*;
import io.github.rulft44.druids.sounds.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Druids {
	/*
	TODO- Remove FFAPI - https://github.com/ZsoltMolnarrr/Wizards/commit/58f35d842b84426ed8fa86b2fefddad4c9effe36
	TODO- Move skill tree spells to separate class
	TODO- Armory compat (new armor)
	TODO- Spell expansion
	TODO- Barkskin player overlay
	TODO- Orb of Oblivion not working
	TODO- Fix tree unlocking issue - https://github.com/NuttiesNuts/druids/issues/12
	TODO- Better spore cloud visuals
	TODO- Armor trim support
	*/

	public static final String ID = "druids";
	public static final Logger LOGGER = LoggerFactory.getLogger("Druids");

	public static ConfigManager<ConfigFile.Equipment> equipmentConfig = new ConfigManager<>
		("equipment", Default.itemConfig)
		.builder()
		.setDirectory(ID)
		.sanitize(true)
		.build();

	public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
		("effects", new ConfigFile.Effects())
		.builder()
		.setDirectory(ID)
		.sanitize(true)
		.build();

	public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<>
		("tweaks", new TweaksConfig())
		.builder()
		.setDirectory(ID)
		.sanitize(true)
		.build();

	public static void init() {
		LOGGER.info("[Druids] skibidi");
		equipmentConfig.refresh();
		effectConfig.refresh();
		tweaksConfig.refresh();

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			tweaksConfig.value.ignore_items_required_mods = true;
		}

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

	public static void registerSounds(){
		ModSounds.register();
	}

	public static void registerItems(){
		Group.DRUIDS = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModArmors.druidArmorSet.head))
			.displayName(Text.translatable("itemGroup." + ID + ".general"))
			.build();
		Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.DRUIDS);

		ModBooks.register();
		ModWeapons.register(equipmentConfig.value.weapons);
		ModArmors.register(equipmentConfig.value.armor_sets);
		ModItems.register();

		equipmentConfig.save();
	}

	public static void registerEffects() {
		ModEffects.register(effectConfig.value);
		effectConfig.save();
	}
}
