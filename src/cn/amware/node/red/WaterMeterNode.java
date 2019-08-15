package cn.amware.node.red;

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

		OutMsg msg = new OutMsg();
		msg.topic = "Log";
		msg.payload = "输入payload=" + payload;
		writeOutput(msg);
	}

	@SuppressWarnings("WeakerAccess")
	private static class OutMsg {
		public String topic;
		public String payload;
	}

}
