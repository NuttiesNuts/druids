package io.github.rulft44.druids.client;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.particle.LeafParticle;
import io.github.rulft44.druids.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

public class DruidsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CustomModels.registerModelIds(List.of(
			Identifier.of(Druids.ID, "projectile/bramble_shot")
		));

		ParticleFactoryRegistry.getInstance().register(ModParticles.LEAF, LeafParticle.Factory::new);
	}
}
