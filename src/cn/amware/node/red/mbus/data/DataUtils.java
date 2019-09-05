package cn.amware.node.red.mbus.data;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Queue;

class DataUtils {

	static void readInts(Queue<Integer> queue, int[] ints) throws NoSuchElementException {
		Arrays.setAll(ints, i -> queue.remove());
	}

	static int convertToBcd(int... ints) {
		int sum = 0;
		for (int i = ints.length - 1; i >= 0; i--) {
			int x = ints[i] & 0xFF;
			int l = x & 0x0F;
			int h = (x >> 4) & 0x0F;
			sum = sum * 10 + h;
			sum = sum * 10 + l;
		}
		return sum;
	}

}
