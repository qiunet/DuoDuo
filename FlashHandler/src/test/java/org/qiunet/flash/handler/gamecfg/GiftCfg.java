package org.qiunet.flash.handler.gamecfg;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.string.StringUtil;

import java.io.DataInputStream;

/**
 * Created by qiunet.
 * 17/6/19
 */
public class GiftCfg implements INestListConfig<String> {
	private String boxid;
	private String reward;
	private boolean sureOut;
	private int weight;
	private int minLv;
	private int maxLv;
	private long startDt;
	private long endDt;
	private int show;


	GiftCfg(DataInputStream dis) throws Exception {
		dis.readInt();
		this.boxid = dis.readUTF();
		this.reward = dis.readUTF();
		this.sureOut = dis.readInt() == 1;
		this.weight = dis.readInt();
		this.minLv = dis.readInt();
		this.maxLv = dis.readInt();
		String str1 = dis.readUTF();
		String str2 = dis.readUTF();
		if (!StringUtil.isEmpty(str1)) {
			this.startDt = DateUtil.stringToDate(str1).getTime();
			this.endDt = DateUtil.stringToDate(str2).getTime();
		}
		this.show = dis.readInt();
	}

	public String getReward() {
		return reward;
	}

	public boolean isSureOut() {
		return sureOut;
	}

	public int getWeight() {
		return weight;
	}

	/***
	 * 是否有效
	 * @param lv
	 * @param time
	 * @return
	 */
	public boolean valid(int lv, long time) {
		if (lv < minLv || lv > maxLv) return false;
		if (startDt < endDt ) {
			if (time < startDt || time > endDt) return false;
		}
		return true;
	}

	public int getShow() {
		return show;
	}

	@Override
	public String getKey() {
		return boxid;
	}
}
