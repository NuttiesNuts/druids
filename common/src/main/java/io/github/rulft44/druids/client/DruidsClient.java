package io.github.rulft44.druids.client;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.client.armor.DruidArmorRenderer;
import io.github.rulft44.druids.item.ModArmors;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.rpg_foundation.armor_api.client.ArmorRenderers;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;
import net.spell_engine.rpg_series.item.Armor;

public class DruidsClient {
    public static void init() {

        registerArmorRenderer(ModArmors.druidArmorSet, DruidArmorRenderer.druid());
        registerArmorRenderer(ModArmors.netheriteDruidArmorSet, DruidArmorRenderer.netherite_druid());

        ClientGuiEvent.RENDER_HUD.register((context, tickDelta) -> {
            if (MinecraftClient.getInstance().player.hasStatusEffect(MRPGCEffects.FATAL_POISON.effect)) {
                context.drawTexture(
                        POISONED_OVERLAY_LOCATION, 0, 0, 0.0F, 0.0F,
                        context.getScaledWindowWidth(),
                        context.getScaledWindowHeight(),
                        context.getScaledWindowWidth(),
                        context.getScaledWindowHeight()
                );
            }
        });
    }

    private static final Identifier POISONED_OVERLAY_LOCATION = new Identifier(Druids.ID, "textures/gui/poisoned_overlay.png");

    private static void registerArmorRenderer(Armor.Set set, GeoArmorRenderer renderer) {
        ArmorRenderers.register(renderer, set.head, set.chest, set.legs, set.feet);
    }
}
