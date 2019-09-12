package cn.amware.node.red;

import cn.amware.node.red.net.LoraPlatformClient;
import cn.amware.node.red.net.NetUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class Test {

	@JSONType(serializer = MySer.class, deserializer = MyDeser.class)
	public static class HexData {
		public String data;
	}

	public static class MyFilter implements ValueFilter {
		@Override
		public Object process(Object object, String name, Object value) {
			if (value instanceof HexData) {
				return ((HexData) value).data;
			} else {
				return value;
			}
		}
	}

	public static class MySer implements ObjectSerializer {
		@Override
		public void write(JSONSerializer serializer,
						  Object object, Object fieldName, Type fieldType,
						  int features) throws IOException {
//			System.out.println("object=" + object);
//			System.out.println("fieldName=" + fieldName);
//			System.out.println("fieldType=" + fieldType);
			if (object instanceof HexData) {
				serializer.write(((HexData) object).data);
			}
		}
	}

	public static class MyDeser implements ObjectDeserializer {
		@Override
		public HexData deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
			System.out.println("type=" + type);
			System.out.println("fieldName=" + fieldName);
			String txt = parser.getLexer().stringVal();
//			System.out.println(txt);
			HexData hexData = new HexData();
			hexData.data = txt;
			return hexData;
		}

		@Override
		public int getFastMatchToken() {
			return 0;
		}
	}

	public static void main(String[] args) {
//		int[] ints = DataUtils.hexStrToArray("abcdef9");
//		for (int i : ints) {
//			System.out.println(String.format("%02X", i));
//		}
//		System.out.println(DataUtils.arrayToHexStr(ints));

		new Test().run();

//		MeterData meterData = new MeterData();
//		meterData.dataId[0] = 0x57;
//		meterData.dataId[1] = 0xD3;
//		meterData.seq = 123;
//		System.out.println(JSON.toJSONString(meterData, SerializerFeature.BrowserCompatible));
//		System.out.println(JSON.toJSONString(meterData));

//		class MyClass {
//			@JSONField(serializeUsing = MySer.class)
//			public HexData hexData1 = new HexData();
//			public HexData hexData2 = new HexData();
//
//			@Override
//			public String toString() {
//				return "hexData1=" + hexData1.ints + ", hexData2=" + hexData2.ints;
//			}
//		}
//		MyClass obj = new MyClass();
//		obj.hexData1.ints = "yes";
//		obj.hexData2.ints = "no";
//		String jsonStr = JSON.toJSONString(obj, new MyFilter());
//		System.out.println(jsonStr);
//		System.out.println(JSON.toJSONString(obj));
//		MyClass obj2 = JSON.parseObject(jsonStr, MyClass.class);
////		MyClass obj2 = JSON.parseObject(jsonStr, MyClass.class, new MyResolver());
//		System.out.println(obj2);
	}

	private void run() {
		System.out.println("开始");
		System.out.println(NetUtils.bytesToHexStr("ABC中文字123".getBytes()));

		String inStr = "dev_eui=1234567812345678&" +
				"application_key=12345678123456781234567812345678&" +
				"timestamp=1567050281&" +
				"secret=3611fa";
		System.out.println(NetUtils.makeMd5(inStr));

		String ts1 = NetUtils.getTimeStamp();
		String ts2 = NetUtils.getTimeStamp(new Date());
		System.out.println("ts1=" + ts1 + ", ts2=" + ts2);

		LoraPlatformClient client = new LoraPlatformClient();
//		String result = client.createDevice("1234567812345678",
//				"12345678123456781234567812345678",
//				"1567050281");

//		String txt = "AMZH";
//		for (byte b : txt.getBytes()) {
//			System.out.print(String.format("%02x", b));
//		}
//		System.out.println();

		String result = client.createDevice("414D5A4812345678",
				NetUtils.getTimeStamp());
		System.out.println(result);
	}

}
