package cn.amware.node.red.mbus;

import cn.amware.node.red.mbus.data.MeterData;
import cn.amware.node.red.mbus.data.MeterPacket;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Receiver {

	private int[] data;

	public Receiver(JSONArray dataArray) {
		data = new int[dataArray.size()];
		for (int i = 0; i < dataArray.size(); i++) {
			data[i] = dataArray.getInteger(i) & 0xFF;
		}
	}

	public void process() {
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
			public Object payload;
		}
		Result result = new Result();

		MeterPacket meterPacket = new MeterPacket();
		try {
			meterPacket.loadFromBinary(data);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		result.topic = "RX";
		result.payload = meterPacket.toString();

		String json = JSON.toJSONString(result, SerializerFeature.BrowserCompatible);
		System.out.println(json);

		MeterData meterData;
		try {
			meterData = MeterData.createFromBinary(meterPacket.data);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		result.topic = "Result";
		result.payload = meterData;

		json = JSON.toJSONString(result, SerializerFeature.BrowserCompatible);
		System.out.println(json);

		result.topic = "WebResult";
		json = JSON.toJSONString(result, SerializerFeature.BrowserCompatible);
		System.out.println(json);
	}

}
