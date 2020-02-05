package org.qiunet.cfg.test.json;

import org.qiunet.cfg.manager.json.NestListJsonCfgManager;
import org.qiunet.cfg.manager.json.NestMapJsonCfgManager;
import org.qiunet.cfg.manager.json.SimpleMapJsonCfgManager;
import org.qiunet.cfg.test.Init2Cfg;
import org.qiunet.cfg.test.Init3Cfg;
import org.qiunet.cfg.test.InitCfg;

/**
 * Created by zhengj
 * Date: 2019/6/6.
 * Time: 12:06.
 * To change this template use File | Settings | File Templates.
 */
public class InitJsonManager {

	private static InitJsonManager instance;
	private SimpleMapJsonCfgManager<Integer, InitCfg> simpleMapJsonCfgManager;
	private NestListJsonCfgManager<Integer, Init3Cfg> nestListJsonCfgManager;
	private NestMapJsonCfgManager<Integer, String, Init2Cfg> nestMapJsonCfgManager;

	private InitJsonManager() {
		load();
	}

	public static InitJsonManager getInstance() {
		if (instance == null) {
			instance = new InitJsonManager();
		}
		return instance;
	}


	private void load() {
		try {
			simpleMapJsonCfgManager = new SimpleMapJsonCfgManager<Integer, InitCfg>("config/init/init_data.json"){};
			nestListJsonCfgManager = new NestListJsonCfgManager<Integer, Init3Cfg>("config/init/init_data.json"){};
			nestMapJsonCfgManager = new NestMapJsonCfgManager<Integer, String, Init2Cfg>("config/init/init_data.json"){};
			simpleMapJsonCfgManager.loadCfg();
			nestListJsonCfgManager.loadCfg();
			nestMapJsonCfgManager.loadCfg();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SimpleMapJsonCfgManager<Integer, InitCfg> getSimpleMapJsonCfgManager() {
		return simpleMapJsonCfgManager;
	}

	public NestListJsonCfgManager<Integer, Init3Cfg> getNestListJsonCfgManager() {
		return nestListJsonCfgManager;
	}

	public NestMapJsonCfgManager<Integer, String, Init2Cfg> getNestMapJsonCfgManager() {
		return nestMapJsonCfgManager;
	}
}
