package io.github.rulft44.druids.config;

import net.spell_engine.api.config.ConfigFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Default {
	public final static ConfigFile.Equipment itemConfig;
	static {
		itemConfig = new ConfigFile.Equipment();

	}

	@SafeVarargs
	private static <T> List<T> joinLists(List<T>... lists) {
		return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
	}
}
