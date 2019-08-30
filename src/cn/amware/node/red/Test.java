package cn.amware.node.red;

import cn.amware.node.red.net.LoraPlatformClient;
import cn.amware.node.red.net.NetUtils;

import java.util.Date;

public class Test {

	public static void main(String[] args) {
		new Test().run();
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
		String result = client.createDevice("1234567812345678",
				"12345678123456781234567812345678",
				"1567050281");
		System.out.println(result);
	}

}
