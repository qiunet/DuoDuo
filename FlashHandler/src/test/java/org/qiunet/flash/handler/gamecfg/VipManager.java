package org.qiunet.flash.handler.gamecfg;

import java.util.Map;

/**
 * Created by liujiehua on 2017/8/21.
 */
public class VipManager extends BaseGameCfgManager {
	private Map<Integer,VipDataCfg> vipDataCfgMap;
	private volatile static VipManager instance;

	private VipManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static VipManager getInstance() {
		if (instance == null) {
			synchronized (VipManager.class) {
				if (instance == null)
				{
					new VipManager();
				}
			}
		}
		return instance;
	}
	@Override
	protected void init() throws Exception {
		this.vipDataCfgMap = getSimpleMapCfg("config/vip_data.xd", VipDataCfg.class);
	}
	public VipDataCfg getVipDataCfg(int vipLevel){
		return vipDataCfgMap.get(vipLevel);
	}
}
