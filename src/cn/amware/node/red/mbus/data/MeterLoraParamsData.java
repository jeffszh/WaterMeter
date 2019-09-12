package cn.amware.node.red.mbus.data;

import cn.amware.node.red.mbus.data.hex.HexData16;
import cn.amware.node.red.mbus.data.hex.HexData8;

@SuppressWarnings("WeakerAccess")
public class MeterLoraParamsData extends MeterData {

	public HexData8 devEui = new HexData8();
	public HexData8 appEui = new HexData8();
	public HexData16 appKey = new HexData16();

	@SuppressWarnings("unused")
	public String getDevEuiAsAsc() {
		StringBuilder sb = new StringBuilder();
		for (int i : devEui.ints) {
			if (i < 0x20 || i > 0x7F) {
				sb.append(String.format("[%02X]", i));
			} else {
				sb.append((char) i);
			}
		}
		return sb.toString();
	}

	@Override
	protected void loadFromBinary(int[] binData) throws Exception {
		super.loadFromBinary(binData);
		IntQueue queue = new IntQueue(binData);

		// 跳过数据头
		queue.read(new int[3]);

		queue.read(devEui.ints);
		queue.read(appEui.ints);
		queue.read(appKey.ints);

		reverseArrayElements(devEui.ints);
		reverseArrayElements(appEui.ints);
		reverseArrayElements(appKey.ints);
	}

	@Override
	public int[] toBinary() {
		int[] dataHead = super.toBinary();
		IntQueue queue = new IntQueue();
		queue.write(dataHead);
		queue.write(DataUtils.reverseArray(devEui.ints));
		queue.write(DataUtils.reverseArray(appEui.ints));
		queue.write(DataUtils.reverseArray(appKey.ints));

		return queue.getAll();
	}

//	private static int[] checkAndReadParam(String key, JSONObject jo) throws Exception {
//		String str = jo.getString(key);
//		if (str == null || str.isEmpty()) {
//			throw new Exception(String.format("param \"%s\" is not present.", key));
//		}
//		return DataUtils.hexStrToArray(str);
//	}

//	@Override
//	public void loadFromJsonObject(JSONObject jo) throws Exception {
//		devEui = checkAndReadParam("devEui", jo);
//		appEui = checkAndReadParam("appEui", jo);
//		appKey = checkAndReadParam("appKey", jo);
//	}

}
