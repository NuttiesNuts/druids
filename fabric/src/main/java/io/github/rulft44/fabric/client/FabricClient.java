package io.github.rulft44.fabric.client;

import io.github.rulft44.druids.client.DruidsClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
		DruidsClient.init();
    }
}
