package cn.amware.node.red.mbus.data;

import cn.amware.node.red.mbus.data.hex.HexData2;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

/**
 * <h1>水表数据</h1>
 * 对应于{@link MeterPacket}中的{@link MeterPacket#data}部分。<br>
 * 这个是基类，空数据（只有数据头没有数据内容）也用这个类表示。
 * 带有完整内容的数据，用此类的派生类表示。
 */
@SuppressWarnings("WeakerAccess")
public class MeterData {

	public HexData2 dataId = new HexData2();
	public int seq;

	/**
	 * 根据{@link #dataId}生成数据标识。
	 * @return 数据标识
	 */
	public String getDataTag() {
		return dataId.getAsPackedHex().toUpperCase();
	}

	public int[] toBinary() {
		IntQueue queue = new IntQueue();
		queue.write(dataId.ints);
		queue.write(seq);
		return queue.getAll();
	}

	/**
	 * <h1>从二进制流装载</h1>
	 * 此方法在这里只是读入数据头，子类会根据不同数据类型加载具体数据。
	 * @param binData 输入的二进制流
	 * @throws Exception 当解析失败时
	 */
	protected void loadFromBinary(int[] binData) throws Exception {
		if (binData.length < 3) {
			throw new MeterDataException("binData.length = " + binData.length + ", it must >=3.");
		}
		IntQueue queue = new IntQueue(binData);
		queue.read(dataId.ints);
		seq = queue.read();
	}

	public static MeterData createFromBinary(int[] binData) throws Exception {
		MeterData meterData = new MeterData();
		meterData.loadFromBinary(binData);
		if (binData.length == 3) {
			return meterData;
		}

		MeterData detailMeterData = createByTag(meterData.getDataTag());	// 创建子类
		if (detailMeterData != null) {
			detailMeterData.loadFromBinary(binData);	// 调用子类的方法
			return detailMeterData;
		} else {
			return meterData;
		}
	}

	/**
	 * 根据tag创建子类。
	 * @param tag 字符串形式的数据标识。
	 * @return 若找到tag对应的子类，返回该子类的示例，否则返回null。
	 */
	public static MeterData createByTag(String tag) {
		MeterDataType meterDataType = MeterDataType.getByTag(tag);
		if (meterDataType != null) {
			try {
				return meterDataType.clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 根据tag创建子类，同时将json的内容填入子类中。
	 * @param tag 字符串形式的数据标识。
	 * @param jo json格式的子类的具体内容。
	 * @return 返回具体的子类，并已填好内容，若出错，返回null。
	 */
	public static MeterData createByTagAndJson(String tag, JSONObject jo) {
		MeterDataType meterDataType = MeterDataType.getByTag(tag);
		if (meterDataType != null) {
			return JSON.parseObject(jo.toJSONString(), meterDataType.clazz);
		}
		return null;
	}

	public static class MeterDataException extends Exception {
		public MeterDataException(String message) {
			super(message);
		}
	}

	private enum MeterDataType {
		METER_PARAM(	"D357",		MeterParamsData.class),
		LORA_PARAM(		"1EC7",		MeterLoraParamsData.class),
		;

		private final String tag;
		private final Class<? extends MeterData> clazz;

		MeterDataType(String tag, Class<? extends MeterData> clazz) {
			this.tag = tag;
			this.clazz = clazz;
		}

		public static MeterDataType getByTag(String tag) {
			return Arrays.stream(values()).filter(value -> value.tag.equals(tag)).findFirst().orElse(null);
		}
	}

	protected static void reverseArrayElements(int[] arr) {
		System.arraycopy(DataUtils.reverseArray(arr), 0,
				arr, 0, arr.length);
	}

}
