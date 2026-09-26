package io.github.rulft44.druids.client.armor;

import io.github.rulft44.druids.Druids;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;
import net.minecraft.util.Identifier;

public final class DruidArmorRenderer {

	private DruidArmorRenderer() { }

	public static GeoArmorRenderer druid() {
		return make("druid_armor", "druid_armor", "druid_armor_generic");
	}

	public static GeoArmorRenderer netherite_druid() {
		return make("druid_armor", "netherite_druid_armor", "netherite_druid_armor_generic");
	}

	private static GeoArmorRenderer make(String modelName, String textureName, String trimTextureName) {
		return GeoArmorRenderer.of(
			Identifier.of(Druids.ID, "geo/" + modelName + ".geo.json"),
			Identifier.of(Druids.ID, "textures/armor/" + textureName + ".png"));
			//.trim(Identifier.of(Druids.ID, "armor/trim/" + trimTextureName), false);
	}
}
