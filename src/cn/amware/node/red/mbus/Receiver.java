package cn.amware.node.red.mbus;

import cn.amware.node.red.mbus.data.MeterPacket;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

class Receiver {

	private byte[] data;

	Receiver(JSONArray dataArray) {
		data = new byte[dataArray.size()];
		for (int i = 0; i < dataArray.size(); i++) {
			data[i] = (byte) dataArray.getInteger(i).intValue();
		}
	}

	void process() {
		new Thread(this::_process).start();
	}

	private void _process() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		@SuppressWarnings("WeakerAccess")
		class Result {
			public String topic;
			public String payload;
		}
		Result result = new Result();

		MeterPacket meterPacket = new MeterPacket();
		try {
			meterPacket.loadFromBytes(data);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		result.topic = "RX";
		result.payload = meterPacket.toString();

		String json = JSON.toJSONString(result, SerializerFeature.BrowserCompatible);
		System.out.println(json);
	}

}
