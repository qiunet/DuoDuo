package org.qiunet.cfg.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestManager {
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
	public void testNestMapInit(){
		NestMapInitManager.getInstance().loadCfg();
		Init2Cfg initCfg = NestMapInitManager.getInstance().getCfgByIdAndSubId(2222, "3,4,5");

		Assert.assertTrue(NestMapInitManager.getInstance().getCfgs().size() == 3);

		Assert.assertTrue(initCfg.getId() == 2222);
		Assert.assertEquals(initCfg.getVal2(), 123457);
		Assert.assertEquals(initCfg.getVal(), "3,4,5");
		Assert.assertTrue(initCfg.getVal3() > 1.19d && initCfg.getVal3() < 1.21d);
	}

	@Test
	public void testNestListInit(){
		NestListInitManager.getInstance().loadCfg();
		List<Init3Cfg> initCfgs = NestListInitManager.getInstance().getCfgListById(3333);

		Assert.assertTrue(initCfgs.size() == 1);
		Assert.assertTrue(NestListInitManager.getInstance().getCfgs().size() == 3);

		Init3Cfg initCfg = initCfgs.get(0);

		Assert.assertTrue(initCfg.getId() == 3333);
		Assert.assertEquals(initCfg.getVal2(), 123458);
		Assert.assertEquals(initCfg.getVal(), "5,6,7");
		Assert.assertTrue(initCfg.getVal3() > 1.29d && initCfg.getVal3() < 1.31d);
	}
}
