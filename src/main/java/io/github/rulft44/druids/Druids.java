package io.github.rulft44.druids;

import io.github.rulft44.druids.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Druids implements ModInitializer {
	public static final String ID = "druids";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[Druids] skibidi");

		ModItems.initialize();
	}
}
