package org.qiunet.flash.handler.gamecfg;

import java.util.*;

/**
 * Created by qiunet.
 * 17/6/19
 */
public class GiftManager extends BaseGameCfgManager {
	private volatile static GiftManager instance;
	private Map<String, List<GiftCfg>> map;
	private GiftManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static GiftManager getInstance() {
		if (instance == null) {
			synchronized (GiftManager.class) {
				if (instance == null)
				{
					new GiftManager();
				}
			}
		}
		return instance;
	}

	@Override
	protected void init() throws Exception {
		this.map = getNestListCfg("config/gift_data.xd", GiftCfg.class);
	}

	public void clear(){
		this.map.clear();
	}

	public void add(){
		this.map.put("", null);
	}

	public List<GiftCfg> getGiftcfgList(String boxId) {
		return map.get(boxId);
	}
}
