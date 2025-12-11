package io.github.rulft44.druids.item;

import io.github.rulft44.druids.Druids;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class Group {
	public static Identifier ID = Identifier.of(Druids.ID, "generic");
	public static RegistryKey<ItemGroup> KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), ID);
	public static ItemGroup DRUIDS;
}
