package org.qiunet.cfg.test.json;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.wrapper.CfgType;
import org.qiunet.cfg.wrapper.SimpleMapCfgWrapper;
import org.qiunet.utils.classScanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 18:26
 ***/
public class TestJsonManager {
	@BeforeClass
	public static void preExec() throws Throwable {
		ClassScanner.getInstance().scanner();
		CfgManagers.getInstance().initSetting();
	}
	@Test
	public void testSimpleMap() {
		SimpleMapCfgWrapper<Integer, JsonInitCfg> cfgWrapper = CfgType.getCfgWrapper(JsonInitCfg.class);

		JsonInitCfg cfg = cfgWrapper.getCfgById(1111);
		Assert.assertEquals(cfg.getVal1(), "1,2,3");
	}
}
