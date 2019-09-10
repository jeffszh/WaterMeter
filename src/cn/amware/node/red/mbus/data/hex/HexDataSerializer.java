package cn.amware.node.red.mbus.data.hex;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class HexDataSerializer implements ObjectSerializer {

	@Override
	public void write(JSONSerializer serializer,
					  Object object, Object fieldName, Type fieldType, int features) throws IOException {
		if (object instanceof HexData) {
			serializer.write(((HexData) object).getAsHex());
		}
	}

}
