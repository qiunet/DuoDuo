package org.qiunet.flash.handler.gamecfg;

import java.io.DataInputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by qiunet.
 * 17/7/13
 */
public class MijingDataCfg implements INestMapConfig<Integer, Integer> {
	private int floor;
	private int type;
	private List<Integer> datas;
	private int weight;

	MijingDataCfg(DataInputStream dis) throws Exception {
		this.floor = dis.readInt();
		this.type = dis.readInt();
		int count = dis.readInt();
		Integer [] arr = new Integer[count];
		this.datas = Arrays.asList(arr);
		this.weight = dis.readInt();
	}

	public int getFloor() {
		return floor;
	}

	public int getType() {
		return type;
	}

	public List<Integer> getDatas() {
		return datas;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public Integer getKey() {
		return floor;
	}

	@Override
	public Integer getSubKey() {
		return type;
	}
}
