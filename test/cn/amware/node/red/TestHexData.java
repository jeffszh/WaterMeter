package cn.amware.node.red;

import cn.amware.node.red.mbus.data.MeterData;
import cn.amware.node.red.mbus.data.MeterLoraParamsData;
import cn.amware.node.red.mbus.data.hex.HexData;
import cn.amware.node.red.mbus.data.hex.HexData2;
import cn.amware.node.red.mbus.data.hex.HexData4;
import cn.amware.node.red.mbus.data.hex.HexData8;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.stream.IntStream;

public class TestHexData {

	public static void main(String[] args) {
		new TestHexData().run5();
	}

	@SuppressWarnings("WeakerAccess")
	private static class ClassA {
		public HexData fieldOne = new HexData();
		public HexData4 fieldTwo = new HexData4();
		public HexData2 fieldThree = new HexData2();
		public HexData8 fieldFour = new HexData8();
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	private static class ClassB extends MeterData {
		public HexData4 fieldOne = new HexData4();
		public HexData fieldTwo = new HexData();
		public HexData2 fieldThree = new HexData2();
		public HexData8 fieldFour = new HexData8();
	}

	private void run0() {
		System.out.println("开始");

		ClassA a = new ClassA();
		a.fieldTwo.ints[0] = 0xAB;
		a.fieldTwo.ints[1] = 0xcd;
		a.fieldTwo.ints[2] = 0xEF;
		a.fieldTwo.ints[3] = 3;
		a.fieldOne = null;
		a.fieldThree = null;
		IntStream.range(0, a.fieldFour.ints.length).forEach(i -> a.fieldFour.ints[i] = i * 3);

		String txt = JSON.toJSONString(a, SerializerFeature.BrowserCompatible);
		System.out.println(txt);
		ClassB b = JSON.parseObject(txt, ClassB.class);
		for (int datum : b.fieldTwo.ints) {
			System.out.print(datum + " ");
		}
		System.out.println();
		System.out.println(JSON.toJSONString(b, SerializerFeature.BrowserCompatible));

		System.out.println("结束");
	}

	private void run1() {
		String jsonStr = "{\"appKey\":\"7bbb375cfe425c832216aaca99ecfa6f\"," +
				"\"appEui\":\"0000000000000000\"," +
				"\"devEui\":\"41 4D 5A 48 00 00 00 02\"}";
		MeterLoraParamsData data = JSON.parseObject(jsonStr, MeterLoraParamsData.class);
		System.out.println(data);
	}

	private void run2() {
		String jsonStr = "{\"fieldOne\":\"7bbb375cfe425c832216aaca99ecfa6f\"," +
				"\"fieldThree\":\"0000000000000000\"," +
				"\"fieldTwo\":\"41 4D 5A 48 00 00 00 02\"}";
		ClassB b = JSON.parseObject(jsonStr, ClassB.class);
		System.out.println(JSON.toJSONString(b, SerializerFeature.BrowserCompatible));
	}

	public static class ClassX {
		public HexData2 f1 = new HexData2();
		public HexData2 f2 = new HexData2();
		public HexData2 f3 = new HexData2();
	}

	private void run3() {
		String jsonStr = "{\"f1\":\"11 11\"," +
				"\"f2\":\"22 22\"," +
				"\"f3\":\"33 33\"}";
		ClassX x = JSON.parseObject(jsonStr, ClassX.class);
		System.out.println(x);
	}

	//	@JSONType(deserializer = JavaBeanDeserializer.class)
	public static class MyA {
		public HexData2 f1 = new HexData2();
		public HexData2 f2 = new HexData2();
		public HexData2 f3 = new HexData2();
	}

	private void run4() {
		ParserConfig config = ParserConfig.getGlobalInstance();
//		config.putDeserializer(MyA.class, new JavaBeanDeserializer(config, MyA.class));
//		config.setAsmEnable(false);
		System.out.println(config.getDeserializer(MyA.class));

		MyA a = new MyA();
		a.f1.ints[0] = 1;
		a.f1.ints[1] = 2;
		a.f2.ints[0] = 3;
		a.f2.ints[1] = 4;
		a.f3.ints[0] = 5;
		a.f3.ints[1] = 6;
		String jsonStr = JSON.toJSONString(a, SerializerFeature.BrowserCompatible);
		System.out.println(jsonStr);
		System.out.println(JSON.toJSONString(JSON.parseObject(jsonStr, MyA.class),
				SerializerFeature.BrowserCompatible));
	}

	private void run5() {
		ParserConfig config = ParserConfig.getGlobalInstance();
//		config.putDeserializer(MeterLoraParamsData.class, new JavaBeanDeserializer(config, MeterLoraParamsData.class));
//		config.compatibleWithJavaBean = true;
//		config.setAsmEnable(false);

		MeterLoraParamsData data = new MeterLoraParamsData();
		String jsonStr = JSON.toJSONString(data);
		System.out.println(jsonStr);
		MeterLoraParamsData data1 = JSON.parseObject(jsonStr, MeterLoraParamsData.class);
		System.out.println(data1);
	}

}
