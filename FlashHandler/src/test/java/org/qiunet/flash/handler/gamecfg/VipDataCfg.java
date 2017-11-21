package org.qiunet.flash.handler.gamecfg;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by liujiehua on 2017/8/21.
 */
public class VipDataCfg implements ISimpleMapConfig<Integer> {
	private int vipLevel;
	private int needRechargeM2;
	private String rewardData;
	private double hangUpExtraExp;
	private double hangUpExtraM1;
	private int addChangeM1Time;
	private int addFubenTime;
	private boolean mijingAutoForward;
	private int addCBGRefreshTime;
	private int addYabiaoReceive;
	private int addYabiaoGuard;
	private int addYabiaoLoot;

	public int getVipLevel() {
		return vipLevel;
	}

	public int getNeedRechargeM2() {
		return needRechargeM2;
	}

	public String getRewardData() {
		return rewardData;
	}
	public double getHangUpExtraExp() {
		return hangUpExtraExp;
	}

	public double getHangUpExtraM1() {
		return hangUpExtraM1;
	}

	public int getAddChangeM1Time() {
		return addChangeM1Time;
	}

	public int getAddFubenTime() {
		return addFubenTime;
	}

	public int getAddYabiaoReceive() {
		return addYabiaoReceive;
	}

	public int getAddCBGRefreshTime() {
		return addCBGRefreshTime;
	}

	public boolean isMijingAutoForward() {
		return mijingAutoForward;
	}

	public int getAddYabiaoGuard() {
		return addYabiaoGuard;
	}

	public int getAddYabiaoLoot() {
		return addYabiaoLoot;
	}

	VipDataCfg(DataInputStream dis) throws IOException {
		this.vipLevel = dis.readInt();
		this.needRechargeM2 = dis.readInt();
		this.rewardData = dis.readUTF();
		this.hangUpExtraExp = (double) dis.readInt()/100;
		this.hangUpExtraM1 = (double) dis.readInt()/100;
		this.addChangeM1Time = dis.readInt();
		this.addFubenTime = dis.readInt();
		this.mijingAutoForward = dis.readInt() == 1?true:false;
		this.addCBGRefreshTime = dis.readInt();
		this.addYabiaoReceive = dis.readInt();
		this.addYabiaoGuard = dis.readInt();
		this.addYabiaoLoot = dis.readInt();
	}

	@Override
	public Integer getKey() {
		return vipLevel;
	}
}
