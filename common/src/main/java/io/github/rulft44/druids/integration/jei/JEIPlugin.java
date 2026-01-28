package io.github.rulft44.druids.integration.jei;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.item.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


@JeiPlugin
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {
	private static final Identifier ID = Identifier.of(Druids.ID, "jei_plugin");

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addIngredientInfo(new ItemStack(ModItems.HEART_OF_THE_FOREST), VanillaTypes.ITEM_STACK, Text.translatable("druids.jei.info.heart_of_the_forest"));
		registration.addIngredientInfo(new ItemStack(ModItems.FATAL_POISON_POTION), VanillaTypes.ITEM_STACK, Text.translatable("druids.jei.info.poison_potion"));
	}

	@Override
	public Identifier getPluginUid() {
		return ID;
	}
}
