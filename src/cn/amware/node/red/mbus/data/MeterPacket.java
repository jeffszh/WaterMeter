package cn.amware.node.red.mbus.data;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.util.Arrays;

@SuppressWarnings({"WeakerAccess", "unused"})
public class MeterPacket {

	private int actualDataLen;
	private byte actualCheckSum;

	public byte startMark;
	public byte instrumentType;
	public byte[] address = new byte[7];
	public byte ctrlCode;
	public byte dataLen;
	public byte[] data;
	public byte checkSum;
	public byte endMark;

	public void loadFromBytes(byte[] bArr) throws Exception {
		int start = searchStartMark(bArr);
		int end = searchEndMark(bArr);
		byte[] packetBytes = Arrays.copyOfRange(bArr, start, end + 1);
		actualDataLen = packetBytes.length - 13;
		actualCheckSum = 0;
		for (int i = 0; i < packetBytes.length - 2; i++) {
			actualCheckSum += packetBytes[i];
		}

		ByteInputStream inputStream = new ByteInputStream(packetBytes, packetBytes.length);
		startMark = (byte) inputStream.read();
		instrumentType = (byte) inputStream.read();
		//noinspection ResultOfMethodCallIgnored
		inputStream.read(address);
		ctrlCode = (byte) inputStream.read();
		dataLen = (byte) inputStream.read();
		data = new byte[actualDataLen];
		//noinspection ResultOfMethodCallIgnored
		inputStream.read(data);
		checkSum = (byte) inputStream.read();
		endMark = (byte) inputStream.read();
	}

	private int searchStartMark(byte[] bArr) {
		for (int i = 0; i < bArr.length; i++) {
			if (bArr[i] == 0x68) {
				return i;
			}
		}
		throw new IndexOutOfBoundsException("no start mark!");
	}

	private int searchEndMark(byte[] bArr) {
		for (int i = bArr.length - 1; i >= 0; i--) {
			if (bArr[i] == 0x16) {
				return i;
			}
		}
		throw new IndexOutOfBoundsException("no end mark!");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("长度：").append(String.format("%02X", dataLen & 0xFF))
				.append("，真长度：").append(String.format("%02X", actualDataLen & 0xFF)).append("\n");
		sb.append("数据：");
		for (int x : data) {
			sb.append(String.format("%02X ", x & 0xFF));
		}
		sb.append("\n");
		sb.append("地址：");
		for (int x : address) {
			sb.append(String.format("%02X ", x & 0xFF));
		}
		sb.append("\n");
		sb.append("校验和：").append(String.format("%02X", checkSum)).append("/")
				.append(String.format("%02X", actualCheckSum)).append("\n");
		sb.append("st: ").append(String.format("%02X", startMark)).append("\n");
		sb.append("end:").append(String.format("%02X", endMark));
		return sb.toString();
	}

}
