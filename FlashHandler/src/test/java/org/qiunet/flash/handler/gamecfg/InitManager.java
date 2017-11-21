package org.qiunet.flash.handler.gamecfg;

import org.qiunet.flash.handler.common.annotation.GameCfg;

/**
 * Created by qiunet.
 * 17/5/27
 */
@GameCfg(order = 10000)
public class InitManager extends KeyValManager {
	private volatile static InitManager instance;

	private InitManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static InitManager getInstance() {
		if (instance == null) {
			synchronized (InitManager.class) {
				if (instance == null)
				{
					new InitManager();
				}
			}
		}
		return instance;
	}
	@Override
	protected void init() throws Exception {
		this.loadVars("config/init_data.xd");
	}
}
