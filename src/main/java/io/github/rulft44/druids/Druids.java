package io.github.rulft44.druids;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Druids implements ModInitializer {
	public static final String ID = "druids";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[Druids] pretty pink princess ponies prancing perpendicular");
	}
}
