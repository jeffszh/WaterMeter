package cn.amware.node.red;

import cn.amware.node.red.mbus.Receiver;
import cn.amware.node.red.mbus.Sender;
import cn.amware.node.red.mbus.data.MeterData;
import cn.amware.node.red.mbus.data.MeterLoraParamsData;
import cn.amware.node.red.mbus.data.MeterParamsData;
import cn.amware.node.red.net.LoraPlatformClient;
import cn.amware.node.red.net.NetUtils;
import cn.jeffszh.lib.node.red.java.NodeRedNode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;

public class WaterMeterNode extends NodeRedNode {

	public static void main(String[] args) {
		new WaterMeterNode().run();
	}

	private static void setupJsonSupport(Class<? extends MeterData> clazz) {
		ParserConfig config = ParserConfig.getGlobalInstance();
		config.putDeserializer(clazz, new JavaBeanDeserializer(config, clazz));
	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("WaterMeterNode started.");

		setupJsonSupport(MeterData.class);
		setupJsonSupport(MeterLoraParamsData.class);
		setupJsonSupport(MeterParamsData.class);
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
//				case "WebRequest":
//					@SuppressWarnings({"WeakerAccess", "unused"})
//					class WebResult {
//						public String topic = "WebResult";
//						public Object payload;
//					}
//					WebResult webResult = new WebResult();
//					String jsonStr = JSON.toJSONString(jsonObject, SerializerFeature.BrowserCompatible);
//					System.out.println("jsonObject = " + jsonStr);
//					webResult.payload = "输入的请求为：" + jsonStr;
//					writeOutput(webResult);
//					return;
				case "WebRequest":
					String reqUrl = jsonObject.getString("url");
					if (reqUrl.endsWith("readLoraParam")) {
						processCommand(payload);
					} else if (reqUrl.endsWith("writeLoraParam")) {
						String devEui = jsonObject.getJSONObject("payload")
								.getJSONObject("data").getString("devEui").trim().toLowerCase();
						new Thread(()->{
							LoraPlatformClient client = new LoraPlatformClient();
							String result = client.createDevice(
									NetUtils.noSpaceHexStr(devEui),
									NetUtils.getTimeStamp());
							@SuppressWarnings({"WeakerAccess", "unused"})
							class WebResult {
								public String topic = "WebResult";
								public Object payload;
							}
							WebResult webResult = new WebResult();
							JSONObject resultObj = JSON.parseObject(result);
							webResult.payload = resultObj;
							writeOutput(webResult);
							if (resultObj.getInteger("code") == 200) {
								String dev_eui = resultObj.getString("dev_eui").trim().toLowerCase();
								if (!NetUtils.noSpaceHexStr(devEui).equals(dev_eui)) {
									new Exception("dev_eui is different: " + dev_eui + " != " + devEui)
											.printStackTrace();
									return;
								}
								String appKey = resultObj.getString("application_key");
								String appEui = resultObj.getString("application_eui");
								jsonObject.getJSONObject("payload").getJSONObject("data").put("appKey", appKey);
								jsonObject.getJSONObject("payload").getJSONObject("data").put("appEui", appEui);
								processCommand(jsonObject.get("payload").toString());
							}
						}).start();

//						if ("auto".equals(inputAppKey)) {
//							String devEui = jsonObject.getJSONObject("payload")
//									.getJSONObject("data").getString("devEui").trim().toLowerCase();
//							String appEui = jsonObject.getJSONObject("payload")
//									.getJSONObject("data").getString("appEui").trim().toLowerCase();
//							//System.out.println("devEui=" + devEui);
//							//System.out.println("appEui=" + appEui);
//							new Thread(()->{
//								LoraPlatformClient client = new LoraPlatformClient();
//								String result = client.createDevice(devEui, appEui,
//										NetUtils.getTimeStamp());
//								jsonObject.getJSONObject("payload")
//										.getJSONObject("data").put("appKey",
//										"1D A6 AE E7 D4 12 83 CA AF 70 90 1B 68 D0 2E BC");
//								processCommand(jsonObject.get("payload").toString());
//							}).start();
//						} else {
//							processCommand(payload);
//						}
					}
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
		System.out.println("RX = " + rxString);
		JSONObject rxObject = JSON.parseObject(rxString);
		JSONArray ja = rxObject.getJSONArray("data");
		new Receiver(ja).process();
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	private static class LogMsg {
		public String topic = "Log";
		public String payload;
	}

}
