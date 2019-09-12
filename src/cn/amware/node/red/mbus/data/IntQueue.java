package cn.amware.node.red.mbus.data;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.LinkedList;

/**
 * <h1>整数序列</h1>
 * 可直接处理int类型，而不是{@link Integer}类型。
 */
@SuppressWarnings("WeakerAccess")
public class IntQueue {

	private final LinkedList<Integer> list = new LinkedList<>();

	public IntQueue() {
	}

	public IntQueue(int[] initInts) {
		write(initInts);
	}

	public void write(int... values) {
		for (int value : values) {
			list.add(value);
		}
	}

	public int read() throws IOException {
		try {
			return list.remove();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public void read(int[] ints) throws IOException {
		for (int i = 0; i < ints.length; i++) {
			ints[i] = read();
		}
	}

	public int[] getAll() {
		return ArrayUtils.toPrimitive(list.toArray(new Integer[0]));
	}

}
