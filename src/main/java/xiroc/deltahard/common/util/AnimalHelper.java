package xiroc.deltahard.common.util;

import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xiroc.deltahard.DeltaHard;

public class AnimalHelper {

	public static boolean interact(EntityAnimal entity, EntityPlayer player, ItemStack item) {
		if (item == null || item == ItemStack.EMPTY)
			return false;
		World world = player.world;
		DeltaHard.logger.info("interacting with a(n) " + item + " (world.isRemote is " + world.isRemote + ")");
		if (item.getItem() == Items.WHEAT && (entity instanceof EntityCow || entity instanceof EntitySheep)) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (world.isRemote)
					return true;
				if (!player.capabilities.isCreativeMode)
					item.shrink(1);
				player.getCooldownTracker().setCooldown(item.getItem(), 20);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.CARROT && (entity instanceof EntityPig || entity instanceof EntityRabbit)) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (world.isRemote)
					return true;
				if (!player.capabilities.isCreativeMode)
					item.shrink(1);
				player.getCooldownTracker().setCooldown(item.getItem(), 20);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.WHEAT_SEEDS && entity instanceof EntityChicken) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (world.isRemote)
					return true;
				if (!player.capabilities.isCreativeMode)
					item.shrink(1);
				player.getCooldownTracker().setCooldown(item.getItem(), 20);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.GOLDEN_APPLE && (entity instanceof EntityCow || entity instanceof EntitySheep) && item.getItemDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (!player.capabilities.isCreativeMode) {
				item.shrink(1);
				player.stopActiveHand();
				player.resetActiveHand();
				player.resetCooldown();
			}
			player.getCooldownTracker().setCooldown(item.getItem(), 20);
			if (!world.isRemote)
				breed(entity, player);
			return true;
		}
		if (item.getItem() == Items.GOLDEN_CARROT && (entity instanceof EntityPig || entity instanceof EntityRabbit) && item.getItemDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (world.isRemote)
				return true;
			if (!player.capabilities.isCreativeMode)
				item.shrink(1);
			player.getCooldownTracker().setCooldown(item.getItem(), 20);
			breed(entity, player);
			return true;
		}
		if (item.getItem() == Items.SPECKLED_MELON && entity instanceof EntityChicken && item.getItemDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (world.isRemote)
				return true;
			if (!player.capabilities.isCreativeMode)
				item.shrink(1);
			player.getCooldownTracker().setCooldown(item.getItem(), 20);
			breed(entity, player);
			return true;
		}
		return false;
	}

	public static void initAnimalTasks(EntityAnimal animal) {
		// EntityCow EntitySheep EntityRabbit EntityChicken EntityPig
		// Rabbits do already follow golden carrots
		if (animal instanceof EntityCow) {
			animal.tasks.addTask(3, new EntityAITempt(animal, 1.25D, Items.GOLDEN_APPLE, false));
			return;
		}
		if (animal instanceof EntityPig) {
			animal.tasks.addTask(4, new EntityAITempt(animal, 1.2D, Items.GOLDEN_CARROT, false));
			return;
		}
		if (animal instanceof EntitySheep) {
			animal.tasks.addTask(3, new EntityAITempt(animal, 1.1D, Items.GOLDEN_APPLE, false));
			return;
		}
		if (animal instanceof EntityChicken) {
			animal.tasks.addTask(3, new EntityAITempt(animal, 1.0D, Items.SPECKLED_MELON, false));
			return;
		}
	}

	private static void breed(EntityAnimal entity, EntityPlayer player) {
		entity.setInLove(player);
	}

}
