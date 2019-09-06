package cn.amware.node.red.mbus.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;

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

	@Override
	public int[] toBinary() {
		int[] dataHead = super.toBinary();
		Queue<Integer> outputQueue = new LinkedList<>();
		outputQueue.addAll(Arrays.asList(ArrayUtils.toObject(dataHead)));
		outputQueue.addAll(Arrays.asList(ArrayUtils.toObject(DataUtils.reverseArray(devEui))));
		outputQueue.addAll(Arrays.asList(ArrayUtils.toObject(DataUtils.reverseArray(appEui))));
		outputQueue.addAll(Arrays.asList(ArrayUtils.toObject(DataUtils.reverseArray(appKey))));

		return ArrayUtils.toPrimitive(outputQueue.toArray(new Integer[0]));
	}

	private static int[] checkAndReadParam(String key, JSONObject jo) throws Exception {
		String str = jo.getString(key);
		if (str == null || str.isEmpty()) {
			throw new Exception(String.format("param \"%s\" is not present.", key));
		}
		return DataUtils.hexStrToArray(str);
	}

	@Override
	public void loadFromJsonObject(JSONObject jo) throws Exception {
		devEui = checkAndReadParam("devEui", jo);
		appEui = checkAndReadParam("appEui", jo);
		appKey = checkAndReadParam("appKey", jo);
	}

}
