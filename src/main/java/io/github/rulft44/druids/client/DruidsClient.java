package io.github.rulft44.druids.client;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.client.armor.DruidArmorRenderer;
import io.github.rulft44.druids.item.ModArmors;
import io.github.rulft44.druids.particle.LeafParticle;
import io.github.rulft44.druids.particle.ModParticles;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.render.CustomModels;

import java.util.List;
import java.util.function.Supplier;

public class DruidsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CustomModels.registerModelIds(List.of(
			Identifier.of(Druids.ID, "projectile/bramble_shot")
		));

		ParticleFactoryRegistry.getInstance().register(ModParticles.LEAF, LeafParticle.Factory::new);

		registerArmorRenderer(ModArmors.druidArmorSet_T1, DruidArmorRenderer::druid);

	}

	private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
		AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
	}
}
