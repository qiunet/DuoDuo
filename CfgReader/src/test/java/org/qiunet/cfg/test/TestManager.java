package org.qiunet.cfg.test;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.cfg.manager.xd.NestListXdCfgManager;
import org.qiunet.cfg.manager.xd.NestMapXdCfgManager;
import org.qiunet.utils.classScanner.ClassScanner;

import java.util.List;
import java.util.Map;

public class TestManager {

	private NestListXdCfgManager<Integer, Init3Cfg> nestListInitManager = new NestListXdCfgManager<Integer, Init3Cfg>("config/init/init_data.xd"){};
	private NestMapXdCfgManager<Integer, String, Init2Cfg> nestMapXdCfgManager = new NestMapXdCfgManager<Integer, String, Init2Cfg>("config/init/init_data.xd"){};


	@Test
	public void testSimpleMapInit(){
		SimpleMapInitManager.getInstance().loadCfg();
		InitCfg initCfg = SimpleMapInitManager.getInstance().getCfgById(1111);

		Assert.assertTrue(SimpleMapInitManager.getInstance().getCfgs().size() == 3);

		Assert.assertTrue(initCfg.getId() == 1111);
		Assert.assertEquals(initCfg.getVal2(), 123456);
		Assert.assertEquals(initCfg.getVal(), "1,2,3");
		Assert.assertTrue(initCfg.getVal3() > 1.09d && initCfg.getVal3() < 1.11d);
	}

	@Test
	public void testSimpleMapWithReward(){
		ClassScanner.getInstance().scanner();
		SimpleMapWithRewardManager.getInstance().loadCfg();
		InitWithRewardCfg initCfg = SimpleMapWithRewardManager.getInstance().getCfgById(1111);

		Assert.assertTrue(SimpleMapWithRewardManager.getInstance().getCfgs().size() == 3);

		Assert.assertTrue(initCfg.getId() == 1111);
		Assert.assertEquals(initCfg.getVal2(), 123456);
		Assert.assertEquals(initCfg.getReward().size(), 3);
		Assert.assertTrue(initCfg.getVal3() > 1.09d && initCfg.getVal3() < 1.11d);
	}


	@Test
	public void testNestMapInit(){
		nestMapXdCfgManager.loadCfg();
		Init2Cfg initCfg = nestMapXdCfgManager.getCfgByIdAndSubId(2222, "3,4,5");

		Assert.assertTrue(nestMapXdCfgManager.getCfgs().size() == 3);

		Assert.assertTrue(initCfg.getId() == 2222);
		Assert.assertEquals(initCfg.getVal2(), 123457);
		Assert.assertEquals(initCfg.getVal(), "3,4,5");
		Assert.assertTrue(initCfg.getVal3() > 1.19d && initCfg.getVal3() < 1.21d);
	}

	@Test
	public void testNestListInit(){
		nestListInitManager.loadCfg();
		List<Init3Cfg> initCfgs = nestListInitManager.getCfgListById(3333);

		Assert.assertTrue(initCfgs.size() == 1);
		Assert.assertTrue(nestListInitManager.getCfgs().size() == 3);

		Init3Cfg initCfg = initCfgs.get(0);

		Assert.assertTrue(initCfg.getId() == 3333);
		Assert.assertEquals(initCfg.getVal2(), 123458);
		Assert.assertEquals(initCfg.getVal(), "5,6,7");
		Assert.assertTrue(initCfg.getVal3() > 1.29d && initCfg.getVal3() < 1.31d);
	}


	@Test
	public void testJsonInit() throws Exception{
		InitCfg initCfg = InitJsonManager.getInstance().getSimpleMapJsonCfgManager().getCfgById(1111);
		Assert.assertTrue(initCfg.getId() == 1111);
		Assert.assertEquals(initCfg.getVal2(), 123456);
		Assert.assertEquals(initCfg.getVal(), "1,2,3");
		Assert.assertTrue(initCfg.getVal3() > 1.09d && initCfg.getVal3() < 1.11d);

		Map<Integer, InitCfg> map = InitJsonManager.getInstance().getSimpleMapJsonCfgManager().getCfgs();
		Assert.assertTrue(map.size() == 3);



		Init2Cfg init2Cfg = InitJsonManager.getInstance().getNestMapJsonCfgManager().getCfgByIdAndSubId(2222, "3,4,5");

		Assert.assertTrue(InitJsonManager.getInstance().getNestMapJsonCfgManager().getCfgs().size() == 3);

		Assert.assertTrue(init2Cfg.getId() == 2222);
		Assert.assertEquals(init2Cfg.getVal2(), 123457);
		Assert.assertEquals(init2Cfg.getVal(), "3,4,5");
		Assert.assertTrue(init2Cfg.getVal3() > 1.19d && init2Cfg.getVal3() < 1.21d);



		List<Init3Cfg> initCfgs = InitJsonManager.getInstance().getNestListJsonCfgManager().getCfgListById(3333);

		Assert.assertTrue(initCfgs.size() == 1);
		Assert.assertTrue(InitJsonManager.getInstance().getNestListJsonCfgManager().getCfgs().size() == 3);

		Init3Cfg init3Cfg = initCfgs.get(0);

		Assert.assertTrue(init3Cfg.getId() == 3333);
		Assert.assertEquals(init3Cfg.getVal2(), 123458);
		Assert.assertEquals(init3Cfg.getVal(), "5,6,7");
		Assert.assertTrue(init3Cfg.getVal3() > 1.29d && init3Cfg.getVal3() < 1.31d);
	}
}
