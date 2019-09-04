package cn.amware.node.red.mbus.data;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Consumer;

@SuppressWarnings({"WeakerAccess"})
public class MeterPacket {

	private static final int START_MARK = 0x68;
	private static final int END_MARK = 0x16;
	@SuppressWarnings("FieldCanBeLocal")
	private static boolean usingActualLen = false;

	private int actualDataLen;
	private int actualCheckSum;

	public int startMark;
	public int instrumentType;
	public int[] address = new int[7];
	public int ctrlCode;
	public int dataLen;
	public int[] data;
	public int checkSum;
	public int endMark;

	public MeterPacket() {
	}

	public MeterPacket(int[] address, int ctrlCode, int[] data) {
		this(0x10, address, ctrlCode, data);
	}

	public MeterPacket(int instrumentType, int[] address, int ctrlCode, int[] data) {
		if (address.length != 7) {
			throw new RuntimeException("Address length must be 7!");
		}
		startMark = START_MARK;
		this.instrumentType = instrumentType;
		this.address = address;
		this.ctrlCode = ctrlCode;
		dataLen = (byte) data.length;
		this.data = data;
		checkSum = 0;
		endMark = END_MARK;
	}

	public int[] createBinary() {
		LinkedList<Integer> outList = new LinkedList<>();
		Consumer<Integer> out = val -> {
			checkSum = (checkSum + val) & 0xFF;
			outList.add(val);
		};
		checkSum = 0;
		out.accept(startMark);
		out.accept(instrumentType);
		Arrays.stream(address).forEachOrdered(out::accept);
		out.accept(ctrlCode);
		out.accept(dataLen);
		Arrays.stream(data).forEachOrdered(out::accept);
		outList.add(checkSum);
		outList.add(endMark);
		Integer[] result = outList.toArray(new Integer[0]);
		return ArrayUtils.toPrimitive(result);
	}

	public void loadFromBinary(int[] binData) throws Exception {
		int start = searchStartMark(binData);
		int end = searchEndMark(binData);
		int[] packetBinData = Arrays.copyOfRange(binData, start, end + 1);
		LinkedList<Integer> input = new LinkedList<>(Arrays.asList(ArrayUtils.toObject(packetBinData)));

		actualDataLen = packetBinData.length - 13;
		actualCheckSum = 0;
		for (int i = 0; i < packetBinData.length - 2; i++) {
			actualCheckSum += packetBinData[i];
		}
		actualCheckSum &= 0xFF;

		startMark = input.remove();
		instrumentType = input.remove();
		Arrays.setAll(address, i -> input.remove());
		ctrlCode = input.remove();
		dataLen = input.remove();
		if (usingActualLen) {
			data = new int[actualDataLen];
		} else {
			data = new int[dataLen];
		}
		Arrays.setAll(data, i -> input.remove());
		checkSum = input.remove();
		endMark = input.remove();
	}

	private int searchStartMark(int[] bArr) {
		for (int i = 0; i < bArr.length; i++) {
			if (bArr[i] == START_MARK) {
				return i;
			}
		}
		throw new IndexOutOfBoundsException("no start mark!");
	}

	private int searchEndMark(int[] bArr) {
		for (int i = bArr.length - 1; i >= 0; i--) {
			if (bArr[i] == END_MARK) {
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
