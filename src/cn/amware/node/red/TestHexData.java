package cn.amware.node.red;

import cn.amware.node.red.mbus.data.hex.HexData;
import cn.amware.node.red.mbus.data.hex.HexData4;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestHexData {

	public static void main(String[] args) {
		new TestHexData().run();
	}

	private static class ClassA {
		public HexData fieldOne = new HexData();
		public HexData4 fieldTwo = new HexData4();
	}

	private static class ClassB {
		public HexData4 fieldOne = new HexData4();
		public HexData fieldTwo = new HexData();
	}

	private void run() {
		System.out.println("开始");

		ClassA a = new ClassA();
		a.fieldTwo.data[0] = 0xAB;
		a.fieldTwo.data[1] = 0xcd;
		a.fieldTwo.data[2] = 0xEF;
		a.fieldTwo.data[3] = 3;

		String txt = JSON.toJSONString(a, SerializerFeature.BrowserCompatible);
		System.out.println(txt);
		ClassB b = JSON.parseObject(txt, ClassB.class);
		for (int datum : b.fieldTwo.data) {
			System.out.print(datum + " ");
		}
		System.out.println();
		System.out.println(JSON.toJSONString(b, SerializerFeature.BrowserCompatible));

		System.out.println("结束");
	}

}
