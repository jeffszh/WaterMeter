package cn.amware.node.red.mbus.data;

import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("WeakerAccess")
public class MeterLoraParamsData extends MeterData {

	public int[] devEui = new int[8];
	public int[] appEui = new int[8];
	public int[] appKey = new int[16];

	@SuppressWarnings("unused")
	public String getDevEuiAsHex() {
		return DataUtils.arrayToHexStr(devEui);
	}

	@SuppressWarnings("unused")
	public String getDevEuiAsAsc() {
		StringBuilder sb = new StringBuilder();
		for (int i : devEui) {
			if (i < 0x20 || i > 0x7F) {
				sb.append(String.format("[%02X]", i));
			} else {
				sb.append((char) i);
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unused")
	public String getAppEuiAsHex() {
		return DataUtils.arrayToHexStr(appEui);
	}

	@SuppressWarnings("unused")
	public String getAppKeyAsHex() {
		return DataUtils.arrayToHexStr(appKey);
	}

	@Override
	protected void loadFromBinary(int[] binData) throws Exception {
		super.loadFromBinary(binData);
		Queue<Integer> inputQueue = new LinkedList<>(Arrays.asList(ArrayUtils.toObject(binData)));

		// 跳过数据头
		DataUtils.readInts(inputQueue, new int[3]);

		DataUtils.readInts(inputQueue, devEui);
		DataUtils.readInts(inputQueue, appEui);
		DataUtils.readInts(inputQueue, appKey);

		devEui = DataUtils.reverseArray(devEui);
		appEui = DataUtils.reverseArray(appEui);
		appKey = DataUtils.reverseArray(appKey);
	}

}
