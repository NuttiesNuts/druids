package io.github.rulft44.druids.item;

import io.github.rulft44.druids.Druids;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.SpellBooks;

import java.util.List;

public class ModBooks {
	public static void register() {
	var books = List.of("druid");
	for (var name: books) {
		SpellBooks.createAndRegister(Identifier.of(Druids.ID, name), Group.KEY);
	}
}
}
