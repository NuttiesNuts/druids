package io.github.rulft44.druids;

import io.github.rulft44.druids.config.Default;
import io.github.rulft44.druids.config.TweaksConfig;
import io.github.rulft44.druids.effect.ModEffects;
import io.github.rulft44.druids.item.*;
import io.github.rulft44.druids.sounds.ModSounds;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Druids {
    public static final String ID = "druids";
    public static final Logger LOGGER = LoggerFactory.getLogger("Druids");

    public static ConfigManager<ConfigFile.Equipment> equipmentConfig = new ConfigManager<>
            ("equipment", Default.itemConfig)
            .builder()
            .setDirectory(ID)
            .sanitize(true)
            .build();

    public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
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

    public static void init() {
        LOGGER.info("[Druids] Initialising Druids...");
        equipmentConfig.refresh();
        effectConfig.refresh();
        tweaksConfig.refresh();

        boolean isDevEnvironment = true;
        if (isDevEnvironment) {
            tweaksConfig.value.ignore_items_required_mods = true;
        }
    }

    public static void registerSounds() {
        ModSounds.register();
    }

    public static void registerItems() {
        Group.DRUIDS = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                .icon(() -> new ItemStack(ModArmors.druidArmorSet.head))
                .displayName(Text.translatable("itemGroup." + ID + ".general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.DRUIDS);

        ModBooks.register();
        ModWeapons.register(equipmentConfig.value.weapons);
        ModArmors.register(equipmentConfig.value.armor_sets);
        ModItems.register();

        equipmentConfig.save();
    }

    public static void registerEffects() {
        ModEffects.register(effectConfig.value);
        effectConfig.save();
    }
}
