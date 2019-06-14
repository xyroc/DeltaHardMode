package xiroc.deltahardmode.common.util;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;

public final class EntityHelper {

	public static boolean containsEntityItem(Iterator<ItemEntity> iterator, Item item) {
		while (iterator.hasNext()) {
			if (iterator.next().getItem().getItem() == item)
				return true;
		}
		return false;
	}

	public static void tagEntity(Entity entity) {
		entity.getEntityData().putBoolean("deltahardmode_tagged", true);
	}

	public static boolean hasTag(Entity entity) {
		return entity.getEntityData().contains("deltahardmode_tagged");
	}
	
	public static final class AnimalHelper {
		
	}

}
