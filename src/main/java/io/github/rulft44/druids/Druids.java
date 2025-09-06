package io.github.rulft44.druids;

import io.github.rulft44.druids.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Druids implements ModInitializer {
	public static final String ID = "druids";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	// Creates school with the id of `spell_power:nature`
	public static final SpellSchool NATURE = SpellSchools.createMagic("nature", 0x43bf4b);

	@Override
	public void onInitialize() {
		LOGGER.info("[Druids] skibidi");

		ModItems.initialize();
	}
}
