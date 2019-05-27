package xiroc.deltahard.common.loot;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LootFunctionSaplings extends LootFunction {

	public LootFunctionSaplings(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		stack.setItemDamage(rand.nextInt(6));
		stack.setCount(1 + rand.nextInt(5));
		return stack;
	}

}
