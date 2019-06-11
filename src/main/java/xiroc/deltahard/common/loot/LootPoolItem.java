package xiroc.deltahard.common.loot;

import java.util.Collection;
import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootPoolItem extends LootPool {

	private Item item;
	private int min, max;

	public LootPoolItem(Item item, int min, int max, LootEntry[] lootEntriesIn, LootCondition[] poolConditionsIn, RandomValueRange rollsIn, RandomValueRange bonusRollsIn, String name) {
		super(lootEntriesIn, poolConditionsIn, rollsIn, bonusRollsIn, name);
		this.item = item;
		this.min = min;
		this.max = max;
	}

	@Override
	public void generateLoot(Collection<ItemStack> stacks, Random rand, LootContext context) {
		stacks.add(new ItemStack(item, min + rand.nextInt(max - min + 1)));
	}

}
