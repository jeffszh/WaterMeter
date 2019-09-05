package cn.amware.node.red;

import cn.amware.node.red.net.LoraPlatformClient;
import cn.amware.node.red.net.NetUtils;

import java.util.Date;

public class Test {

	public static void main(String[] args) {
		new Test().run();
//		MeterData meterData = new MeterData();
//		meterData.dataId[0] = 0x57;
//		meterData.dataId[1] = 0xD3;
//		meterData.seq = 123;
//		System.out.println(JSON.toJSONString(meterData, SerializerFeature.BrowserCompatible));
//		System.out.println(JSON.toJSONString(meterData));
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
				"414d5a48202020202020202020202020",
				NetUtils.getTimeStamp());
		System.out.println(result);
	}

}
