package io.github.rulft44.druids.datagen;

import io.github.rulft44.druids.item.ModArmors;
import io.github.rulft44.druids.item.ModItems;
import io.github.rulft44.druids.item.ModWeapons;
import io.github.rulft44.druids.spell.DruidSpells;
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
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DruidsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(UnsmeltGenerator::new);
		pack.addProvider(SpellGen::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(DruidRecipes::new);
	}
	public static class SpellGen extends SpellGenerator {
		public SpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateSpells(Builder builder) {
			for (var entry: DruidSpells.entries) {
				builder.add(entry.id(), entry.spell());
			}
		}
	}


	public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
		public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			generateArmorTags(ModArmors.entries, RPGSeriesItemTags.ArmorMetaType.MAGIC);
		}
	}
	
	public static class UnsmeltGenerator extends FabricRecipeProvider {
		public UnsmeltGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		public static int UNSMELT_TIME = 300;

		@Override
		public void generate(RecipeExporter exporter) {
			disassembleArmor(exporter, ModArmors.druidArmorSet_T2, Items.WHEAT_SEEDS);
			disassembleArmor(exporter, ModArmors.druidArmorSet_T3, Items.NETHERITE_SCRAP);

			disassemble(exporter,
				List.of(ModWeapons.natureWand.item()),
				Items.CHARCOAL);

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

		private static void disassembleArmor(RecipeExporter exporter, Armor.Set armorSet, Item output) {
			FabricRecipeProvider.offerSmelting(exporter,
				armorSet.pieces(),
				RecipeCategory.MISC,
				output,
				0.1f,
				UNSMELT_TIME,
				"disassemble"
			);
			FabricRecipeProvider.offerBlasting(exporter,
				armorSet.pieces(),
				RecipeCategory.MISC,
				output,
				0.1f,
				UNSMELT_TIME / 2,
				"disassemble"
			);
		}
	}
}
