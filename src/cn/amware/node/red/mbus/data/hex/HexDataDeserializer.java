package cn.amware.node.red.mbus.data.hex;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class HexDataDeserializer implements ObjectDeserializer {

	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		System.out.println("--- in HexDataDeserializer ---");
		try {
			System.out.println("parser.getClass()=" + parser.getClass());
			System.out.println("parser.lexer.getClass()=" + parser.lexer.getClass());
			String className = type.getTypeName();
			System.out.println("className = " + className);
			Class<?> clazz = Class.forName(className);
			Object obj = clazz.newInstance();
			System.out.println("obj = " + obj + ", obj.class = " + obj.getClass());
			if (!(obj instanceof HexData)) {
				return null;
			}
			System.out.println("input = " + parser.input);
			System.out.println("info = " + parser.lexer.info());
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
/*
input = {"appKey":"7bbb375cfe425c832216aaca99ecfa6f","appEui":"0000000000000000","devEui":"41 4D 5A 48 00 00 00 02"}
 */
