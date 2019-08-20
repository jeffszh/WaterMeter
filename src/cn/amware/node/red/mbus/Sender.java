package cn.amware.node.red.mbus;

import cn.jeffszh.lib.node.red.java.NodeRedNode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Sender {

	private final NodeRedNode node;
	private final int[] address;
	private final int ctrlCode;
	private final int[] dataId;
	private static byte seq = 1;

	public Sender(String cmd, NodeRedNode node) throws Exception {
		this.node = node;
		CommandParam commandParam = JSON.parseObject(cmd, CommandParam.class);
		address = hexStringToArray(commandParam.address);
		ctrlCode = Integer.parseInt(commandParam.ctrlCode, 16);
		dataId = hexStringToArray(commandParam.dataId);
		if (address.length != 7 || dataId.length != 2) {
			throw new IOException("MBus command param error!");
		}
	}

	public void send() {
		new Thread(this::_send).start();
	}

	private void _send() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//68 10 AA AA AA AA AA AA AA 01 03 EF 90 01 A2 16
		//68 10 AA AA AA AA AA AA AA 01 03 5F 90 02 13 16
		//68 10 AA AA AA AA AA AA AA 01 03 AF 90 03 64 16
		int[] bArr = {0x68, 0x10, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0x01, 0x03, 0xFF, 0xFF, 0x01, 0xFF, 0x16};
		for (int i = 0; i < address.length; i++) {
			bArr[i + 2] = address[i] & 0xFF;
		}
		bArr[9] = ctrlCode & 0xFF;
		bArr[10] = dataId.length + 1;
		bArr[11] = dataId[0] & 0xFF;
		bArr[12] = dataId[1] & 0xFF;
		bArr[13] = seq++;
		int sum = 0;
		for (int i = 0; i < bArr.length - 2; i++) {
			sum += bArr[i];
		}
		sum &= 0xFF;
		bArr[14] = sum;

		JSONObject jo = new JSONObject();
		jo.put("topic", "TX");
//		JSONArray ja = new JSONArray();
//		for (int x : bArr) {
//			ja.add(x);
//		}
//		jo.put("payload", ja);
		jo.put("payload", bArr);
		node.writeOutput(jo);
	}

	private int[] hexStringToArray(String hexString) {
		ArrayList<Integer> result = new ArrayList<>();
		Scanner scanner = new Scanner(hexString);
		while (scanner.hasNext()) {
			result.add(scanner.nextInt(16));
		}
		return result.stream().mapToInt(Integer::intValue).toArray();
	}

	@SuppressWarnings("WeakerAccess")
	private static class CommandParam {
		public String address;
		public String ctrlCode;
		public String dataId;
	}

}
