package io.github.rulft44.druids.effect;

import io.github.rulft44.druids.Druids;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {
	public static final RegistryEntry<StatusEffect> THORNED =
		Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Druids.ID, "thorned"), new ThornedEffect());

	public static void register() {
	}
}

