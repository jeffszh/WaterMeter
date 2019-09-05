package cn.amware.node.red.mbus;

import cn.amware.node.red.mbus.data.DataUtils;
import cn.amware.node.red.mbus.data.MeterData;
import cn.amware.node.red.mbus.data.MeterPacket;
import cn.jeffszh.lib.node.red.java.NodeRedNode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class Sender {

	private final NodeRedNode node;
	private final int[] address;
	private final int ctrlCode;
	private final int[] dataId;
	private static byte seq = 1;

	public Sender(String cmd, NodeRedNode node) throws Exception {
		this.node = node;
		CommandParam commandParam = JSON.parseObject(cmd, CommandParam.class);
		address = DataUtils.hexStrToArray(commandParam.address);
		ctrlCode = Integer.parseInt(commandParam.ctrlCode, 16);
		dataId = DataUtils.hexStrToArray(commandParam.dataId);
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

		MeterData meterData = new MeterData();
		meterData.dataId[0] = dataId[0] & 0xFF;
		meterData.dataId[1] = dataId[1] & 0xFF;
		meterData.seq = seq++;
		MeterPacket packet = new MeterPacket(address, (byte) ctrlCode, meterData.toBinary());
		int[] packetBinary = packet.createBinary();

		JSONObject jo = new JSONObject();
		jo.put("topic", "TX");
		int[] outBuf = new int[packetBinary.length + 6];
		Arrays.fill(outBuf, 0xFE);
		System.arraycopy(packetBinary, 0, outBuf, 6, packetBinary.length);
		jo.put("payload", outBuf);
		node.writeOutput(jo);
	}

	@SuppressWarnings("WeakerAccess")
	private static class CommandParam {
		public String address;
		public String ctrlCode;
		public String dataId;
	}

}
