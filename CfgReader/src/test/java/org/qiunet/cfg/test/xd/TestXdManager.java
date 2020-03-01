package org.qiunet.cfg.test.xd;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.cfg.manager.xd.NestListXdCfgManager;
import org.qiunet.cfg.manager.xd.NestMapXdCfgManager;
import org.qiunet.cfg.test.Init2Cfg;
import org.qiunet.cfg.test.Init3Cfg;
import org.qiunet.cfg.test.InitCfg;
import org.qiunet.cfg.test.InitWithRewardCfg;
import org.qiunet.utils.classScanner.ClassScanner;

import java.util.List;

public class TestXdManager {

	private NestListXdCfgManager<Integer, Init3Cfg> nestListInitManager = new NestListXdCfgManager<Integer, Init3Cfg>("config/init/init_data.xd"){};
	private NestMapXdCfgManager<Integer, String, Init2Cfg> nestMapXdCfgManager = new NestMapXdCfgManager<Integer, String, Init2Cfg>("config/init/init_data.xd"){};

	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void testSimpleMapInit() throws Exception {
		SimpleMapInitManager.getInstance().loadCfg();
		InitCfg initCfg = SimpleMapInitManager.getInstance().getCfgById(1111);

		Assert.assertTrue(SimpleMapInitManager.getInstance().getCfgs().size() == 3);

		Assert.assertTrue(initCfg.getId() == 1111);
		Assert.assertEquals(initCfg.getVal2(), 123456);
		Assert.assertEquals(initCfg.getVal1(), "1,2,3");
		Assert.assertTrue(initCfg.getVal3() > 1.09d && initCfg.getVal3() < 1.11d);
	}

	@Test
	public void testSimpleMapWithReward() throws Exception{
		SimpleMapWithRewardManager.getInstance().loadCfg();
		InitWithRewardCfg initCfg = SimpleMapWithRewardManager.getInstance().getCfgById(1111);

		Assert.assertTrue(SimpleMapWithRewardManager.getInstance().getCfgs().size() == 3);

		Assert.assertTrue(initCfg.getId() == 1111);
		Assert.assertEquals(initCfg.getVal2(), 123456);
		Assert.assertEquals(initCfg.getReward().size(), 3);
		Assert.assertTrue(initCfg.getVal3() > 1.09d && initCfg.getVal3() < 1.11d);
	}


	@Test
	public void testNestMapInit() throws Exception{
		nestMapXdCfgManager.loadCfg();
		Init2Cfg initCfg = nestMapXdCfgManager.getCfgByIdAndSubId(2222, "3,4,5");

		Assert.assertTrue(nestMapXdCfgManager.getCfgs().size() == 3);

		Assert.assertTrue(initCfg.getId() == 2222);
		Assert.assertEquals(initCfg.getVal2(), 123457);
		Assert.assertEquals(initCfg.getVal1(), "3,4,5");
		Assert.assertTrue(initCfg.getVal3() > 1.19d && initCfg.getVal3() < 1.21d);
	}

	@Test
	public void testNestListInit() throws Exception{
		nestListInitManager.loadCfg();
		List<Init3Cfg> initCfgs = nestListInitManager.getCfgListById(3333);

		Assert.assertTrue(initCfgs.size() == 1);
		Assert.assertTrue(nestListInitManager.getCfgs().size() == 3);

		Init3Cfg initCfg = initCfgs.get(0);

		Assert.assertTrue(initCfg.getId() == 3333);
		Assert.assertEquals(initCfg.getVal2(), 123458);
		Assert.assertEquals(initCfg.getVal1(), "5,6,7");
		Assert.assertTrue(initCfg.getVal3() > 1.29d && initCfg.getVal3() < 1.31d);
	}
}
