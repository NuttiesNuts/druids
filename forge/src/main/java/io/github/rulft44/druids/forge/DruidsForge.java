package io.github.rulft44.druids.forge;

import io.github.rulft44.druids.Druids;
import dev.architectury.platform.forge.EventBuses;
import io.github.rulft44.druids.client.DruidsClient;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Druids.ID)
public final class DruidsForge {

    @SuppressWarnings("removal")
    public DruidsForge() {
        Druids.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, DruidsForge::register);

        Druids.tweaksConfig.save();
    }

    public static void register(RegisterEvent event) {
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
