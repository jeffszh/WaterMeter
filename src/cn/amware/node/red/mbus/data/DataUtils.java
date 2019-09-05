package cn.amware.node.red.mbus.data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataUtils {

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

	static String arrayToHexStr(int[] ints) {
		StringBuilder result = new StringBuilder();
		for (int i : ints) {
			result.append(String.format("%02X ", i));
		}
		return result.toString().trim();
	}

	public static int[] hexStrToArray(String hexString) {
		ArrayList<Integer> result = new ArrayList<>();
		Scanner scanner = new Scanner(hexString);
		while (scanner.hasNext()) {
			result.add(scanner.nextInt(16));
		}
		return result.stream().mapToInt(Integer::intValue).toArray();
	}

	static int[] reverseArray(int[] ints) {
		int[] result = new int[ints.length];
		IntStream.range(0, ints.length).forEachOrdered(i -> result[result.length - i - 1] = ints[i]);
		return result;
	}

}
