package cn.amware.node.red.mbus.data;

import java.util.Arrays;

/**
 * <h1>水表数据</h1>
 * 对应于{@link MeterPacket}中的{@link MeterPacket#data}部分。<br>
 * 这个是基类，空数据（只有数据头没有数据内容）也用这个类表示。
 * 带有完整内容的数据，用此类的派生类表示。
 */
@SuppressWarnings("WeakerAccess")
public class MeterData {

	public int[] dataId = new int[2];
	public int seq;

	/**
	 * 根据{@link #dataId}生成数据标识。
	 * @return 数据标识
	 */
	public String getDataTag() {
		return String.format("%02X%02X", dataId[0], dataId[1]);
	}

	public int[] toBinary() {
		return new int[]{dataId[0], dataId[1], seq};
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
		dataId[0] = binData[0];
		dataId[1] = binData[1];
		seq = binData[2];
	}

	public static MeterData createFromBinary(int[] binData) throws Exception {
		MeterData meterData = new MeterData();
		meterData.loadFromBinary(binData);
		if (binData.length == 3) {
			return meterData;
		}

		MeterDataType meterDataType = MeterDataType.getByTag(meterData.getDataTag());
		if (meterDataType != null) {
			meterData = meterDataType.creator.create();	// 创建子类
			meterData.loadFromBinary(binData);	// 调用子类的方法
		}
		return meterData;
	}

	public static class MeterDataException extends Exception {
		public MeterDataException(String message) {
			super(message);
		}
	}

	private interface MeterDataCreator {
		MeterData create();
	}

	private enum MeterDataType {
		METER_PARAM(	"D357",		MeterParamsData::new),
		LORA_PARAM(		"1EC7",		MeterLoraParamsData::new),
		;

		private final String tag;
		private final MeterDataCreator creator;

		MeterDataType(String tag, MeterDataCreator creator) {
			this.tag = tag;
			this.creator = creator;
		}

		public static MeterDataType getByTag(String tag) {
			return Arrays.stream(values()).filter(value -> value.tag.equals(tag)).findFirst().orElse(null);
		}
	}

}
