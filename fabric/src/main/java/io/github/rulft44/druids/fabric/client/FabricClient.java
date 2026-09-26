package io.github.rulft44.druids.fabric.client;

import io.github.rulft44.druids.client.DruidsClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DruidsClient.init();
    }
}
