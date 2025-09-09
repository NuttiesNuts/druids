package io.github.rulft44.druids.particle;

import io.github.rulft44.druids.Druids;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
	public static final SimpleParticleType LEAF =
		registerParticle("leaf", FabricParticleTypes.simple());

	private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
		return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Druids.ID, name), particleType);
	}

	public static void register() {
	}
}

