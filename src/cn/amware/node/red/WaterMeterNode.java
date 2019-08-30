package cn.amware.node.red;

import cn.amware.node.red.mbus.Sender;
import cn.jeffszh.lib.node.red.java.NodeRedNode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WaterMeterNode extends NodeRedNode {

	public static void main(String[] args) {
		new WaterMeterNode().run();
	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("WaterMeterNode started.");
	}

	@Override
	protected void onInput(JSONObject jsonObject) {
		String payload;
		if (jsonObject.containsKey("payload")) {
			payload = jsonObject.get("payload").toString();
		} else {
			payload = "没有";
		}

		if (jsonObject.containsKey("topic")) {
			String topic = jsonObject.get("topic").toString();
			switch (topic) {
				case "Command":
					processCommand(payload);
					return;
				case "RX":
					processRx(payload);
					return;
				case "WebRequest":
					@SuppressWarnings({"WeakerAccess", "unused"})
					class WebResult {
						public String topic = "WebResult";
						public Object payload;
					}
					WebResult webResult = new WebResult();
					String jsonStr = JSON.toJSONString(jsonObject, SerializerFeature.BrowserCompatible);
					System.out.println("jsonObject = " + jsonStr);
					webResult.payload = "输入的请求为：" + jsonStr;
					writeOutput(webResult);
					return;
			}
		}

		LogMsg logMsg = new LogMsg();
		logMsg.payload = "输入payload=" + payload;
		writeOutput(logMsg);
//		System.out.println("{\"payload\":{\"data\":[65,66,67],\"type\":\"Buffer\"}}");
	}

	private void processCommand(String command) {
		try {
			new Sender(command, this).send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processRx(String rxString) {
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	private static class LogMsg {
		public String topic = "Log";
		public String payload;
	}

}
