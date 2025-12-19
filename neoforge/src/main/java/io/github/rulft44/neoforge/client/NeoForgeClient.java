package io.github.rulft44.neoforge.client;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.client.DruidsClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.spell_engine.client.gui.ConfigMenuScreen;

@EventBusSubscriber(modid = Druids.ID, value = Dist.CLIENT)
public class NeoForgeClient {
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		DruidsClient.init();
		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ((modContainer, parent) -> new ConfigMenuScreen(parent)));
	}
}
