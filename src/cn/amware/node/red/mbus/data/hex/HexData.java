package cn.amware.node.red.mbus.data.hex;

import cn.amware.node.red.mbus.data.DataUtils;
import com.alibaba.fastjson.annotation.JSONType;

import java.util.Arrays;

@JSONType(serializer = HexDataSerializer.class, deserializer = HexDataDeserializer.class)
public class HexData {

	public final int[] data;

	public HexData() {
		HexLen hexLen = getClass().getAnnotation(HexLen.class);
		if (hexLen != null) {
			data = new int[hexLen.value()];
		} else {
			data = new int[8];
		}
	}

	public String getAsHex() {
		return DataUtils.arrayToHexStr(data);
	}

	public void setAsHex(String asHex) {
		int[] ints = DataUtils.hexStrToArray(asHex);
		System.arraycopy(ints, 0, data, 0, Math.min(data.length, ints.length));
	}

}
