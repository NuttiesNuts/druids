package io.github.rulft44.druids.mixin;

import io.github.rulft44.druids.Druids;
import net.spell_power.api.SpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpellSchools.class)
public class SpellSchoolsMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void static_tail_NatureMagic(CallbackInfo ci) {
		SpellSchools.register(Druids.NATURE); // Trigger registration
	}
}
