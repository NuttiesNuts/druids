package io.github.rulft44.neoforge;

import io.github.rulft44.druids.Druids;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Druids.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        Druids.init();
		modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
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
