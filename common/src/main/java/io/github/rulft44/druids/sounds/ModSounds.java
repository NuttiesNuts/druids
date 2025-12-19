package io.github.rulft44.druids.sounds;

import io.github.rulft44.druids.Druids;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
	public static final Identifier DART_IMPACT_ID = Identifier.of(Druids.ID, "dart_impact");
	public static SoundEvent DART_IMPACT_EVENT = SoundEvent.of(DART_IMPACT_ID);
	public static final Identifier DART_RELEASE_ID = Identifier.of(Druids.ID, "dart_release");
	public static SoundEvent DART_RELEASE_EVENT = SoundEvent.of(DART_RELEASE_ID);

	public static void register() {
		Registry.register(Registries.SOUND_EVENT, DART_IMPACT_ID, DART_IMPACT_EVENT);
		Registry.register(Registries.SOUND_EVENT, DART_RELEASE_ID, DART_RELEASE_EVENT);
	}
}
