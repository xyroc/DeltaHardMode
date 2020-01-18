package xiroc.deltahardmode.common.util;

import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import xiroc.deltahardmode.DeltaHardMode;
public class AnimalHelper {

	public static boolean interact(AnimalEntity entity, PlayerEntity player, ItemStack item) {
		if (item == null || item == ItemStack.EMPTY)
			return false;
		World world = player.world;
		DeltaHardMode.LOGGER.info("interacting with a(n) " + item + " (world.isRemote is " + world.isRemote + ")");
		if (item.getItem() == Items.WHEAT && (entity instanceof CowEntity || entity instanceof SheepEntity)) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (world.isRemote)
					return true;
				if (!player.isCreative())
					item.shrink(1);
				player.getCooldownTracker().setCooldown(item.getItem(), 20);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.CARROT && (entity instanceof PigEntity || entity instanceof RabbitEntity)) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (world.isRemote)
					return true;
				if (!player.isCreative())
					item.shrink(1);
				player.getCooldownTracker().setCooldown(item.getItem(), 20);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.WHEAT_SEEDS && entity instanceof ChickenEntity) {
			if (entity.isChild() && entity.isBreedingItem(item)) {
				if (world.isRemote)
					return true;
				if (!player.isCreative())
					item.shrink(1);
				player.getCooldownTracker().setCooldown(item.getItem(), 20);
				entity.ageUp((int) ((float) (-entity.getGrowingAge() / 20) * 0.1F), true);
				return true;
			}
			return true;
		}
		if (item.getItem() == Items.GOLDEN_APPLE && (entity instanceof CowEntity || entity instanceof SheepEntity) && item.getDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (!player.isCreative()) {
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
		if (item.getItem() == Items.GOLDEN_CARROT && (entity instanceof PigEntity || entity instanceof RabbitEntity) && item.getDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (world.isRemote)
				return true;
			if (!player.isCreative())
				item.shrink(1);
			player.getCooldownTracker().setCooldown(item.getItem(), 20);
			breed(entity, player);
			return true;
		}
		if (item.getItem() == Items.GLISTERING_MELON_SLICE && entity instanceof ChickenEntity && item.getDamage() == 0 && entity.getGrowingAge() == 0 && !entity.isInLove()) {
			if (world.isRemote)
				return true;
			if (!player.isCreative())
				item.shrink(1);
			player.getCooldownTracker().setCooldown(item.getItem(), 20);
			breed(entity, player);
			return true;
		}
		return false;
	}

	public static void initAnimalTasks(AnimalEntity animal) {
		// EntityCow EntitySheep EntityRabbit EntityChicken EntityPig
		// Rabbits do already follow golden carrots
		if (animal instanceof CowEntity) {
			animal.goalSelector.addGoal(3, new TemptGoal(animal, 1.25D, Ingredient.fromItems(Items.GOLDEN_APPLE), false));
			return;
		}
		if (animal instanceof PigEntity) {
			animal.goalSelector.addGoal(3, new TemptGoal(animal, 1.2D, Ingredient.fromItems(Items.GOLDEN_CARROT), false));
			return;
		}
		if (animal instanceof SheepEntity) {
			animal.goalSelector.addGoal(3, new TemptGoal(animal, 1.1D, Ingredient.fromItems(Items.GOLDEN_APPLE), false));
			return;
		}
		if (animal instanceof ChickenEntity) {
			animal.goalSelector.addGoal(3, new TemptGoal(animal, 1.0D, Ingredient.fromItems(Items.GLISTERING_MELON_SLICE), false));
			return;
		}
	}

	private static void breed(AnimalEntity entity, PlayerEntity player) {
		entity.setInLove(player);
	}

}
