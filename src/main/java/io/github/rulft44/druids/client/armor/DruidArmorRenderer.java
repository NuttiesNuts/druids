package io.github.rulft44.druids.client.armor;

import io.github.rulft44.druids.Druids;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.util.Identifier;

public class DruidArmorRenderer extends AzArmorRenderer {
	public static DruidArmorRenderer druid() {
		return new DruidArmorRenderer("druid_armor", "druid_armor");
	}

	public DruidArmorRenderer(String modelName, String textureName) {
		super(AzArmorRendererConfig.builder(
			Identifier.of(Druids.ID, "geo/" + modelName + ".geo.json"),
			Identifier.of(Druids.ID, "textures/armor/" + textureName + ".png")
		).build());
	}
}
