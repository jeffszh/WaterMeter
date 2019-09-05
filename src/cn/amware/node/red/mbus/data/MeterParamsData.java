package cn.amware.node.red.mbus.data;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <h1>水表参数数据</h1>
 */
@SuppressWarnings("WeakerAccess")
public class MeterParamsData extends MeterData {

	public int sampleTime;
	public int zeroDrift;
	public int unit1;
	public int pw1stThreshold;
	public int tofUpperBound;
	public int tofLowerBound;
	public int tofMax;
	public int initialFlow;
	public int unit2;
	public int pipeHorizontalLength;
	public int pipeVerticalLength;
	public int pipeRadius;

	@Override
	protected void loadFromBinary(int[] binData) throws Exception {
		super.loadFromBinary(binData);
		Queue<Integer> inputQueue = new LinkedList<>(Arrays.asList(ArrayUtils.toObject(binData)));

		// 跳过数据头
		DataUtils.readInts(inputQueue, new int[3]);

		int[] buffer = new int[3];
		DataUtils.readInts(inputQueue, buffer);
		sampleTime = DataUtils.convertToBcd(buffer);

		buffer = new int[4];
		DataUtils.readInts(inputQueue, buffer);
		zeroDrift = DataUtils.convertToBcd(buffer);

		unit1 = DataUtils.convertToBcd(inputQueue.remove());
		pw1stThreshold = DataUtils.convertToBcd(inputQueue.remove());

		buffer = new int[4];
		DataUtils.readInts(inputQueue, buffer);
		tofUpperBound = DataUtils.convertToBcd(buffer);

		buffer = new int[4];
		DataUtils.readInts(inputQueue, buffer);
		tofLowerBound = DataUtils.convertToBcd(buffer);

		buffer = new int[4];
		DataUtils.readInts(inputQueue, buffer);
		tofMax = DataUtils.convertToBcd(buffer);

		buffer = new int[4];
		DataUtils.readInts(inputQueue, buffer);
		initialFlow = DataUtils.convertToBcd(buffer);

		unit2 = DataUtils.convertToBcd(inputQueue.remove());

		buffer = new int[4];
		DataUtils.readInts(inputQueue, buffer);
		pipeHorizontalLength = DataUtils.convertToBcd(buffer);

		buffer = new int[4];
		DataUtils.readInts(inputQueue, buffer);
		pipeVerticalLength = DataUtils.convertToBcd(buffer);

		buffer = new int[4];
		DataUtils.readInts(inputQueue, buffer);
		pipeRadius = DataUtils.convertToBcd(buffer);
	}

}
