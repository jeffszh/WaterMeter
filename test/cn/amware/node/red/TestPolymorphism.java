package cn.amware.node.red;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

@SuppressWarnings("WeakerAccess")
public class TestPolymorphism {

	public static void main(String[] args) {
		System.out.println("开始");
		new TestPolymorphism().run();
		System.out.println("结束");
	}

	@JSONType(deserializer = PolyDeserializer.class)
	public static class BaseShape {
		public double centerX, centerY;
		public String shapeType;
	}

	public static class Circle extends BaseShape {
		public double radius;

		public Circle() {
			shapeType = getClass().getName();
		}
	}

	public static class Rectangle extends BaseShape {
		public double width, height;

		public Rectangle() {
			shapeType = getClass().getName();
		}
	}

	public static class MyMsg {
		public String info;
		public BaseShape shape;
	}

	public static class PolyDeserializer implements ObjectDeserializer {

		@Override
		public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
//			System.out.println("---1---");
////			parser.parseObject(Circle.class);
////			System.out.println("input=" + parser.getLexer().tokenName());
//			System.out.println(parser.lexer.info());
//			System.out.println(parser.lexer.getClass());
//			System.out.println(parser.lexer.pos());
//			System.out.println(parser.lexer.token() + " - " + parser.lexer.tokenName());
//			System.out.println("---2---");

			String shapeType = parseShapeType(parser.getInput());
			if (shapeType == null) {
				return null;
			}
			try {
				//noinspection unchecked
				return (T) parser.parseObject(Class.forName(shapeType));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public int getFastMatchToken() {
			return 0;
		}

		private static String parseShapeType(String jsonStr) {
			String[] ts = jsonStr.split("\"shapeType\":");
			if (ts.length >= 2) {
				return ts[1].split("\"")[1];
			} else {
				return null;
			}
		}

	}

	private void run() {
		MyMsg msg01 = new MyMsg();
		msg01.info = "msg01";
		Circle circle = new Circle();
		circle.centerX = 2;
		circle.centerY = 7;
		circle.radius = 5;
		msg01.shape = circle;

		MyMsg msg02 = new MyMsg();
		msg02.info = "msg02";
		Rectangle rectangle = new Rectangle();
		rectangle.width = 6;
		rectangle.height = 4;
		msg02.shape = rectangle;

		String msg01Json = JSON.toJSONString(msg01);
		String msg02Json = JSON.toJSONString(msg02);
		System.out.println(msg01Json);
		System.out.println(msg02Json);

		MyMsg msg11 = JSON.parseObject(msg01Json, MyMsg.class);
		MyMsg msg12 = JSON.parseObject(msg02Json, MyMsg.class);
		System.out.println(JSON.toJSONString(msg11));
		System.out.println(JSON.toJSONString(msg12));
	}

}
