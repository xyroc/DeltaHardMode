package xiroc.deltahard.common.loot;

import java.util.Locale;
import java.util.Random;

import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import xiroc.deltahard.DeltaHard;

public class LootFunctionTreasureMap extends LootFunction {

	public LootFunctionTreasureMap(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		if (context.getKillerPlayer() == null)
			return ItemStack.EMPTY;
		String destination = rand.nextFloat() >= 0.5F ? "Monument" : "Mansion";
		MapDecoration.Type destinationType = destination.equals("Monument") ? MapDecoration.Type.MONUMENT : MapDecoration.Type.MANSION;
		World world = context.getWorld();
		BlockPos blockpos = world.findNearestStructure(destination, context.getKillerPlayer().getPosition(), true);
		if (blockpos != null) {
			ItemStack itemstack = ItemMap.setupNewMap(world, (double) blockpos.getX(), (double) blockpos.getZ(), (byte) 2, true, true);
			ItemMap.renderBiomePreviewMap(world, itemstack);
			MapData.addTargetDecoration(itemstack, blockpos, "+", destinationType);
			itemstack.setTranslatableName("filled_map." + destination.toLowerCase(Locale.ROOT));
			return itemstack;
		}
		return ItemStack.EMPTY;
	}

}
