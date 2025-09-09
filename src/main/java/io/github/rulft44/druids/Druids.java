package io.github.rulft44.druids;

import io.github.rulft44.druids.config.Default;
import io.github.rulft44.druids.config.TweaksConfig;
import io.github.rulft44.druids.item.ModItems;
import io.github.rulft44.druids.item.ModWeapons;
import io.github.rulft44.druids.particle.ModParticles;
import net.fabricmc.api.ModInitializer;
import net.spell_engine.api.config.ConfigFile;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.tinyconfig.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Druids implements ModInitializer {
	public static final String ID = "druids";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	// Creates school with the id of `spell_power:nature`
	public static final SpellSchool NATURE = SpellSchools.createMagic("nature", 0x43bf4b);

	public static ConfigManager<ConfigFile.Equipment> equipmentConfig = new ConfigManager<>
		("equipment", Default.itemConfig)
		.builder()
		.setDirectory(ID)
		.sanitize(true)
		.build();


	public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<>
		("tweaks", new TweaksConfig())
		.builder()
		.setDirectory(ID)
		.sanitize(true)
		.build();

	@Override
	public void onInitialize() {
		LOGGER.info("[Druids] skibidi");
		equipmentConfig.refresh();
		tweaksConfig.refresh();

		ModWeapons.register(equipmentConfig.value.weapons);
		ModItems.register();
		ModParticles.register();

		equipmentConfig.save();
	}
}
