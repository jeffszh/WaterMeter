package cn.amware.node.red.mbus.data.hex;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class HexDataDeserializer implements ObjectDeserializer {

	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		try {
			String className = type.getTypeName();
			Class<?> clazz = Class.forName(className);
			Object obj = clazz.newInstance();
			if (!(obj instanceof HexData)) {
				return null;
			}
			HexData hexData = (HexData) obj;
			hexData.setAsHex(parser.getLexer().stringVal());

			//noinspection unchecked
			return (T) hexData;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getFastMatchToken() {
		return 0;
	}

}
