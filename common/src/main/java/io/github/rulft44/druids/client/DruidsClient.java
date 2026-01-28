package io.github.rulft44.druids.client;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.client.armor.DruidArmorRenderer;
import io.github.rulft44.druids.item.ModArmors;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererRegistry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.render.CustomModels;

import java.util.List;
import java.util.function.Supplier;

public class DruidsClient{
	public static void init() {
		CustomModels.registerModelIds(List.of(
			Identifier.of(Druids.ID, "projectile/bramble_shot"),
			Identifier.of(Druids.ID, "effect/entanglement1"),
			Identifier.of(Druids.ID, "effect/entanglement11"),
			Identifier.of(Druids.ID, "effect/entanglement2"),
			Identifier.of(Druids.ID, "effect/dandelion"),
			Identifier.of(Druids.ID, "effect/poppy")
		));

		registerArmorRenderer(ModArmors.druidArmorSet, DruidArmorRenderer::druid);
		registerArmorRenderer(ModArmors.netheriteDruidArmorSet, DruidArmorRenderer::netherite_druid);
	}

	private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
		AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
	}
}
