package xiroc.deltahard.util;

import java.util.Random;

import net.minecraft.block.BlockCrops;

public class NotSoRandom extends Random {
	
	//BlockCrops

	@Override
	public int nextInt() {
		return 1;
	}

	@Override
	public int nextInt(int bound) {
		return 1;
	}

	@Override
	public double nextDouble() {
		return 1.0D;
	}

	@Override
	public int next(int bits) {
		return 1;
	}

	@Override
	public float nextFloat() {
		return 1.0F;
	}

	@Override
	public long nextLong() {
		return 1L;
	}

}
