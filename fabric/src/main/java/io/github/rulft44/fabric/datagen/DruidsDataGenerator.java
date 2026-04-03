package io.github.rulft44.fabric.datagen;

import io.github.rulft44.druids.Druids;
import io.github.rulft44.druids.effect.ModEffects;
import io.github.rulft44.druids.item.ModArmors;
import io.github.rulft44.druids.item.ModWeapons;
import io.github.rulft44.druids.spell.DruidSpells;
import io.github.rulft44.druids.spell.skill.DruidsSkillDefinitions;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.puffish.skillsmod.reward.builtin.AttributeReward;
import net.skill_tree_rpgs.data_gen.SkillDefinitionGenerator;
import net.skill_tree_rpgs.node.SpellContainerReward;
import net.skill_tree_rpgs.utils.ResolvableTextContent;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.datagen.WeaponAttributeGenerator;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.tags.SpellTags;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DruidsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(SpellGen::new);
		pack.addProvider(SpellTagGenerator::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(UnsmeltGenerator::new);
		pack.addProvider(SkillDefinitionGen::new);
		pack.addProvider(LangGenerator::new);
		pack.addProvider(DruidRecipes::new);
		pack.addProvider(WeaponGen::new);
	}

	public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
		public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			generateWeaponTags(ModWeapons.entries);
			generateArmorTags(ModArmors.entries, RPGSeriesItemTags.ArmorMetaType.MAGIC);
		}
	}

	public static class SpellGen extends SpellGenerator {
		public SpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateSpells(Builder builder) {
			for (var entry : DruidSpells.entries) {
				builder.add(entry.id(), entry.spell());
			}
		}
	}

	public static class SpellTagGenerator extends FabricTagProvider<Spell> {
		public SpellTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, SpellRegistry.KEY, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			var namespace = Druids.ID;
			var treasureTagBuilder = getOrCreateTagBuilder(SpellTags.TREASURE);
			var processedBooks = new HashSet<DruidSpells.Book>();
			DruidSpells.entries.forEach(entry -> {
				if (entry.book() != null) {
					var bookTagKey = SpellTags.spellBook(namespace, entry.book().toString().toLowerCase());
					var bookTag = getOrCreateTagBuilder(bookTagKey);
					bookTag.addOptional(entry.id());
					var scrollTagKey = SpellTags.spellScroll(namespace, entry.book().toString().toLowerCase());
					var scrollTag = getOrCreateTagBuilder(scrollTagKey);
					scrollTag.addOptional(entry.id());
					if (processedBooks.add(entry.book())) {
						treasureTagBuilder.addOptionalTag(scrollTagKey);
					}
				}
				for (var group : entry.weaponGroups()) {
					var weaponGroupTagKey = SpellTags.weapon(namespace, group.toString().toLowerCase());
					var weaponGroupTag = getOrCreateTagBuilder(weaponGroupTagKey);
					weaponGroupTag.addOptional(entry.id());
				}
			});
		}
	}

	public static class LangGenerator extends FabricLanguageProvider {
		protected LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}
		@Override
		public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
			try { // merge handwritten lang with data generated one
				translationBuilder.add(Path.of("../../../fabric/src/main/resources/assets/druids/lang/h_en_us.json"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			for (var skill: DruidsSkillDefinitions.ENTRIES) {
				if (skill.title() != null && !skill.title().isEmpty()) {
					translationBuilder.add(skill.titleTranslationKey(), skill.title());
				}
				if (skill.description() != null && !skill.description().isEmpty()) {
					translationBuilder.add(skill.descriptionTranslationKey(), skill.description());
				}
			}
			for (var entry: DruidSpells.entries) {
				translationBuilder.add(SpellTooltip.spellTranslationKey(entry.id()), entry.title());
				translationBuilder.add(SpellTooltip.spellDescriptionTranslationKey(entry.id()), entry.description());
			}
			ModEffects.entries.forEach(entry -> {
				translationBuilder.add(entry.effect.getTranslationKey(), entry.title);
				translationBuilder.add(entry.effect.getTranslationKey() + ".description", entry.description);
			});
		}
}

		public static class SkillDefinitionGen extends SkillDefinitionGenerator {
			public SkillDefinitionGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
				super(dataOutput, registryLookup);
			}

			@Override
			public void generate(Builder builder) {
				LinkedHashMap<String, Format> skillDefinitions = new LinkedHashMap<>();
				for (var skill : DruidsSkillDefinitions.ENTRIES) {
					Translatable title = null;
					if (skill.title() != null && !skill.title().isEmpty()) {
						title = new Translatable(skill.titleTranslationKey());
					}
					Text description;
					if (skill.description() != null && !skill.description().isEmpty()) {
						description = Text.translatable(skill.descriptionTranslationKey());
					} else {
						description = MutableText.of(new ResolvableTextContent(skill.id()));
					}

					Icon icon = null;
					switch (skill.icon().type()) {
						case TEXTURE -> icon = Icon.texture(skill.icon().value());
						case ITEM -> icon = skill.icon().modelId() != null
							? Icon.itemWithModel(skill.icon().value(), skill.icon().modelId())
							: Icon.item(skill.icon().value());
						case EFFECT -> icon = Icon.effect(skill.icon().value());
					}
					ArrayList<Reward> rewards = new ArrayList<>();
					if (skill.attributeReward() != null) {
						var attribute = skill.attributeReward();
						rewards.add(new Reward(AttributeReward.ID.toString(), RewardAttribute.from(attribute.attribute(),  attribute.modifier())));
					}
					if(skill.spellReward() != null) {
						rewards.add(new Reward(SpellContainerReward.ID.toString(), new SpellContainerReward.DataStructure(skill.spellReward())));
					}
					var format = new Format(title, description, icon, rewards, skill.required_mods());
					skillDefinitions.put(skill.id(), format);
				}
				builder.entries.add(new Entry(DruidsSkillDefinitions.CATEGORY_ID, skillDefinitions));
			}
	}

	
	public static class UnsmeltGenerator extends FabricRecipeProvider {
		public UnsmeltGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		public static int UNSMELT_TIME = 300;

		@Override
		public void generate(RecipeExporter exporter) {
			disassembleArmor(exporter, ModArmors.druidArmorSet, Items.WHEAT_SEEDS);
			disassembleArmor(exporter, ModArmors.netheriteDruidArmorSet, Items.NETHERITE_SCRAP);

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

	public static class WeaponGen extends WeaponAttributeGenerator {
		public WeaponGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateWeaponAttributes(Builder builder) {
			ModWeapons.entries.forEach(entry -> {
				if (entry.weaponAttributesPreset != null && !entry.weaponAttributesPreset.isEmpty()) {
					builder.entries.add(new Entry(entry.id(), entry.weaponAttributesPreset));
				}
			});
		}
	}
}
