package cn.amware.node.red.mbus.data.hex;

import cn.amware.node.red.mbus.data.DataUtils;
import cn.amware.node.red.net.NetUtils;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * <h1>16进制数据类</h1>
 * 用于存储定长16进制序列，提供方便的序列化和反序列化方法。<br>
 * 子类可用{@link HexLen}指定长度，若不指定，默认为8个字节。
 */
@JSONType(serializer = HexDataSerializer.class, deserializer = HexDataDeserializer.class)
public class HexData {

	public final int[] ints;

	public HexData() {
		HexLen hexLen = getClass().getAnnotation(HexLen.class);
		if (hexLen != null) {
			ints = new int[hexLen.value()];
		} else {
			ints = new int[8];
		}
	}

	@SuppressWarnings("WeakerAccess")
	public String getAsHex() {
		return DataUtils.arrayToHexStr(ints);
	}

	@SuppressWarnings("WeakerAccess")
	public void setAsHex(String asHex) {
		int[] ints = DataUtils.hexStrToArray(asHex);
		System.arraycopy(ints, 0, this.ints, 0, Math.min(this.ints.length, ints.length));
	}

	public String getAsPackedHex() {
		return NetUtils.noSpaceHexStr(getAsHex()).toLowerCase();
	}

}
