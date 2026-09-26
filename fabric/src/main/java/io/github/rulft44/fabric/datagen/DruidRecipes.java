package io.github.rulft44.fabric.datagen;

import io.github.rulft44.druids.item.ModArmors;
import io.github.rulft44.druids.item.ModWeapons;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.spell_engine.rpg_series.item.Armor;

import java.util.concurrent.CompletableFuture;

public class DruidRecipes extends FabricRecipeProvider {
	public DruidRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void generate(RecipeExporter exporter) {
		generateWandRecipes(exporter);
		generateStaffRecipes(exporter);
		generateArmorRecipes(exporter);
		generateNetheriteUpgrades(exporter);
	}

	// ========================================
	// WAND RECIPES
	// ========================================

	private void generateWandRecipes(RecipeExporter exporter) {
		// Nature Wand - emerald + stick
		ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModWeapons.natureWand.item())
			.pattern(" C")
			.pattern("S ")
			.input('C', Items.EMERALD)
			.input('S', ConventionalItemTags.WOODEN_RODS)
			.criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
			.offerTo(exporter);
	}

	// ========================================
	// STAFF RECIPES
	// ========================================

	private void generateStaffRecipes(RecipeExporter exporter) {
		// Nature Staff - emerald + sapling + seed + stick
		ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModWeapons.natureStaff.item())
			.pattern(" AP")
			.pattern(" GA")
			.pattern("S  ")
			.input('P', ItemTags.SAPLINGS)
			.input('A', Items.EMERALD)
			.input('G', ConventionalItemTags.SEEDS)
			.input('S', ConventionalItemTags.WOODEN_RODS)
			.criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
			.offerTo(exporter);
	}

	// ========================================
	// ARMOR RECIPES
	// ========================================

	private void generateArmorRecipes(RecipeExporter exporter) {
		// Nature Robes
		generateArmorSet(exporter, ModArmors.druidArmorSet, Items.WHEAT_SEEDS);
	}

	/**
	 * Generate all 4 druid armor pieces
	 */
	private void generateArmorSet(RecipeExporter exporter, Armor.Set set, Item specialIngredient) {
		// Helmet/Head
		ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, set.head)
			.pattern("  W")
			.pattern(" WW")
			.pattern("GLG")
			.input('L', specialIngredient)
			.input('G', Items.GOLD_INGOT)
			.input('W', ItemTags.WOOL)
			.criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
			.offerTo(exporter);

		// Chestplate
		ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, set.chest)
			.pattern("L L")
			.pattern("GFG")
			.pattern("WWW")
			.input('L', specialIngredient)
			.input('G', Items.GOLD_INGOT)
			.input('W', ItemTags.WOOL)
			.input('F', ItemTags.SMALL_FLOWERS)
			.criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
			.offerTo(exporter);

		// Leggings
		ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, set.legs)
			.pattern("GGG")
			.pattern("W W")
			.pattern("W W")
			.input('G', Items.GOLD_INGOT)
			.input('W', ConventionalItemTags.LEATHERS)
			.criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
			.offerTo(exporter);

		// Boots
		ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, set.feet)
			.pattern("W W")
			.pattern("G G")
			.input('G', Items.GOLD_INGOT)
			.input('W', ConventionalItemTags.LEATHERS)
			.criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
			.offerTo(exporter);
	}

	// ========================================
	// NETHERITE UPGRADE RECIPES
	// ========================================

	private void generateNetheriteUpgrades(RecipeExporter exporter) {
		// Wand upgrades
		offerNetheriteUpgradeRecipe(exporter, ModWeapons.natureWand.item(), RecipeCategory.COMBAT, ModWeapons.netheriteNatureWand.item());

		// Staff upgrades
		offerNetheriteUpgradeRecipe(exporter, ModWeapons.natureStaff.item(), RecipeCategory.COMBAT, ModWeapons.netheriteNatureStaff.item());

		// Armor upgrades - Nature set
		offerNetheriteUpgradeRecipe(exporter, ModArmors.druidArmorSet.head, RecipeCategory.COMBAT, ModArmors.netheriteDruidArmorSet.head);
		offerNetheriteUpgradeRecipe(exporter, ModArmors.druidArmorSet.chest, RecipeCategory.COMBAT, ModArmors.netheriteDruidArmorSet.chest);
		offerNetheriteUpgradeRecipe(exporter, ModArmors.druidArmorSet.legs, RecipeCategory.COMBAT, ModArmors.netheriteDruidArmorSet.legs);
		offerNetheriteUpgradeRecipe(exporter, ModArmors.druidArmorSet.feet, RecipeCategory.COMBAT, ModArmors.netheriteDruidArmorSet.feet);
	}

	// ========================================
	// HELPER METHODS
	// ========================================


	@Override
	public String getName() {
		return "Druid Crafting Recipes";
	}
}
