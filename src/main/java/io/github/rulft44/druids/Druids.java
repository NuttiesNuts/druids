package io.github.rulft44.druids;

import io.github.rulft44.druids.config.Default;
import io.github.rulft44.druids.config.TweaksConfig;
import io.github.rulft44.druids.effect.ModEffects;
import io.github.rulft44.druids.item.*;
import io.github.rulft44.druids.particle.ModParticles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.ConfigFile;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Druids implements ModInitializer {
	public static final String ID = "druids";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static ConfigManager<ConfigFile.Equipment> equipmentConfig = new ConfigManager<>
		("equipment", Default.itemConfig)
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

	@Override
	public void onInitialize() {
		LOGGER.info("[Druids] skibidi");
		equipmentConfig.refresh();
		tweaksConfig.refresh();

		Group.DRUIDS = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModArmors.druidArmorSet_T1.head))
			.displayName(Text.translatable("itemGroup." + ID + ".general"))
			.build();
		Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.DRUIDS);

		ModBooks.register();

		ModWeapons.register(equipmentConfig.value.weapons);
		ModArmors.register(equipmentConfig.value.armor_sets);
		ModItems.register();

		ModEffects.register();
		ModParticles.register();

		FabricLoader.getInstance().getModContainer(ID).ifPresent(modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
					Identifier.of(ID, "druid_book_variant"),
					modContainer,
					ResourcePackActivationType.NORMAL);
			});

		equipmentConfig.save();
	}
}

//TODO: Tags & recipes for armor

// TODO: More Spells MOTO MOTO MOTO

// TODO: Structures

// TODO: Loot Injection
