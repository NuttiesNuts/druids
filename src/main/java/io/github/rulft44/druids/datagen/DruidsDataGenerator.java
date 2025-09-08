package io.github.rulft44.druids.datagen;

import io.github.rulft44.druids.item.ModItems;
import io.github.rulft44.druids.item.ModWeapons;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DruidsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(UnsmeltGenerator::new);
	}
	
	public static class UnsmeltGenerator extends FabricRecipeProvider {
		public UnsmeltGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		public static int UNSMELT_TIME = 300;

		@Override
		public void generate(RecipeExporter exporter) {
			disassemble(exporter,
				List.of(ModWeapons.natureWand.item()),
				ModItems.COPPER_NUGGET);

			disassemble(exporter,
				List.of(ModWeapons.natureStaff.item()),
				Items.EMERALD);

			disassemble(exporter,
				ModWeapons.entries.stream()
					.filter(entry -> entry.id().getPath().contains("netherite"))
					.map(entry -> (ItemConvertible) entry.item()).toList(),
				Items.NETHERITE_SCRAP);
		}

		private static void disassemble(RecipeExporter exporter, List<ItemConvertible> items, Item output) {
			FabricRecipeProvider.offerSmelting(exporter,
				items,
				RecipeCategory.MISC,
				output,
				0.1f,
				UNSMELT_TIME,
				"disassemble"
			);
			FabricRecipeProvider.offerBlasting(exporter,
				items,
				RecipeCategory.MISC,
				output,
				0.1f,
				UNSMELT_TIME / 2,
				"disassemble"
			);
		}
	}
}
