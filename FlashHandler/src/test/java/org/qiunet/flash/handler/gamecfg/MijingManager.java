package org.qiunet.flash.handler.gamecfg;

import java.util.Map;

/**
 * Created by qiunet.
 * 17/7/13
 */
public class MijingManager extends BaseGameCfgManager {
	private Map<Integer, Map<Integer,MijingDataCfg>> dataListCfg;
	private volatile static MijingManager instance;

	private MijingManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static MijingManager getInstance() {
		if (instance == null) {
			synchronized (MijingManager.class) {
				if (instance == null)
				{
					new MijingManager();
				}
			}
		}
		return instance;
	}

	@Override
	protected void init() throws Exception {
		this.dataListCfg = getNestMapCfg("config/mijing_data.xd", MijingDataCfg.class);
	}

	public MijingDataCfg getMijingDataCfg(int floor , int type) {
		return dataListCfg.get(floor).get(type);
	}
}
