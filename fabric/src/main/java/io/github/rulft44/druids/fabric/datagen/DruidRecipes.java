package io.github.rulft44.druids.fabric.datagen;

import io.github.rulft44.druids.item.ModArmors;
import io.github.rulft44.druids.item.ModWeapons;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.spell_engine.rpg_series.item.Armor;

import java.util.function.Consumer;

public class DruidRecipes extends FabricRecipeProvider {

    public DruidRecipes(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

    }

    private void generateWandRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModWeapons.natureWand.item())
                .pattern(" C")
                .pattern("S ")
                .input('C', Items.EMERALD)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
                .offerTo(exporter);
    }

    private void generateStaffRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModWeapons.natureStaff.item())
                .pattern(" AP")
                .pattern(" GA")
                .pattern("S  ")
                .input('P', ItemTags.SAPLINGS)
                .input('A', Items.EMERALD)
                .input('G', Items.WHEAT_SEEDS)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
                .offerTo(exporter);
    }

    private void generateArmorRecipes(Consumer<RecipeJsonProvider> exporter) {
        generateArmorSet(exporter, ModArmors.druidArmorSet, Items.WHEAT_SEEDS);
    }

    private void generateArmorSet(Consumer<RecipeJsonProvider> exporter, Armor.Set set, Item specialIngredient) {
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
                .input('W', Items.LEATHER)
                .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                .offerTo(exporter);

        // Boots
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, set.feet)
                .pattern("W W")
                .pattern("G G")
                .input('G', Items.GOLD_INGOT)
                .input('W', Items.LEATHER)
                .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                .offerTo(exporter);
    }

    private void generateNetheriteUpgrades(Consumer<RecipeJsonProvider> exporter) {
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

    @Override
    public String getName() {
        return "Druid Crafting Recipes";
    }
}
