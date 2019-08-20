package cn.amware.node.red;

import cn.amware.node.red.mbus.Sender;
import cn.jeffszh.lib.node.red.java.NodeRedNode;
import com.alibaba.fastjson.JSONObject;

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
