package xiroc.deltahard.common.util;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AnimalHelper {

	public static boolean interact(EntityAnimal entity, EntityPlayer player, ItemStack item) {
		if (item == null || item == ItemStack.EMPTY)
			return false;
		if (item.getItem() == Items.WHEAT && (entity instanceof EntityCow || entity instanceof EntitySheep)) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (!player.capabilities.isCreativeMode)
					item.shrink(1);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.CARROT && (entity instanceof EntityPig || entity instanceof EntityRabbit)) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (!player.capabilities.isCreativeMode)
					item.shrink(1);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.WHEAT_SEEDS && entity instanceof EntityChicken) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (!player.capabilities.isCreativeMode)
					item.shrink(1);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.GOLDEN_APPLE && (entity instanceof EntityCow || entity instanceof EntitySheep) && item.getItemDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (!player.capabilities.isCreativeMode)
				item.shrink(1);
			breed(entity, player);
			return true;
		}
		if (item.getItem() == Items.GOLDEN_CARROT && (entity instanceof EntityPig || entity instanceof EntityRabbit) && item.getItemDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (!player.capabilities.isCreativeMode)
				item.shrink(1);
			breed(entity, player);
			return true;
		}
		if (item.getItem() == Items.SPECKLED_MELON && entity instanceof EntityChicken && item.getItemDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (!player.capabilities.isCreativeMode)
				item.shrink(1);
			breed(entity, player);
			return true;
		}
		return false;
	}

	private static void breed(EntityAnimal entity, EntityPlayer player) {
		entity.setInLove(player);
	}

}
