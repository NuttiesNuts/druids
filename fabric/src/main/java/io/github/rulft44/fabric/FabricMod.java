package io.github.rulft44.fabric;

import io.github.rulft44.druids.Druids;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import static io.github.rulft44.druids.Druids.ID;

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
    }
}
