package xiroc.deltahard.common.loot;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LootFunctionBase extends LootFunction {

	private int minStackSize;
	private int maxStackSize;

	public LootFunctionBase(LootCondition[] conditionsIn, int minStackSize, int maxStackSize) {
		super(conditionsIn);
		this.minStackSize = minStackSize;
		this.maxStackSize = maxStackSize;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		stack.setCount(minStackSize + rand.nextInt(1 + maxStackSize - minStackSize));
		return stack;
	}

}
