package io.github.rulft44.neoforge;

import io.github.rulft44.druids.Druids;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Druids.ID)
public final class NeoForgeMod {
	private static boolean datapackRegistered = false;
	private static boolean warningShown = false;

    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        Druids.init();

		modBus.addListener(EventPriority.LOWEST, this::onCommonSetup);
		modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
		NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onServerStarted);

		Druids.tweaksConfig.save();
	}

	private void onCommonSetup(FMLCommonSetupEvent event) {
		// Register early so it's available
		event.enqueueWork(() -> {
			if (!datapackRegistered && !Druids.tweaksConfig.value.disable_druids_skilltree_changes) {
				FabricLoader.getInstance().getModContainer(Druids.ID).ifPresent(modContainer -> {
					ResourceManagerHelper.registerBuiltinResourcePack(
						Identifier.of(Druids.ID, "druids_skill_tree_changes"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
					);
					datapackRegistered = true;
					Druids.LOGGER.info("Registered Druids Skill Tree Changes datapack");
				});
			}
		});
	}

	private void onServerStarted(ServerStartedEvent event) {
		if (!warningShown && !Druids.tweaksConfig.value.disable_druids_skilltree_changes) {
			warningShown = true;
			Druids.LOGGER.info("==================================================");
			Druids.LOGGER.info("Druids Skill Tree Add-On loaded successfully!");
			Druids.LOGGER.info("");
			Druids.LOGGER.info("IMPORTANT: If skill tree changes are NOT working:");
			Druids.LOGGER.info("The datapack may need to be reordered. Run these commands:");
			Druids.LOGGER.info("  1. /datapack disable \"druids_skill_tree:druids_skill_tree_changes\"");
			Druids.LOGGER.info("  2. /datapack enable \"druids_skill_tree:druids_skill_tree_changes\" last");
			Druids.LOGGER.info("  3. /reload");
			Druids.LOGGER.info("");
			Druids.LOGGER.info("This only needs to be done once - the order will persist.");
			Druids.LOGGER.info("See DATAPACK_LOAD_ORDER.md for more information.");
			Druids.LOGGER.info("==================================================");
		}
	}

	public static void register(RegisterEvent event){
		event.register(RegistryKeys.ITEM, reg -> {
			Druids.registerItems();
		});
		event.register(RegistryKeys.SOUND_EVENT, reg -> {
			Druids.registerSounds();
		});
		event.register(RegistryKeys.STATUS_EFFECT, reg -> {
			Druids.registerEffects();
		});
	}
}
