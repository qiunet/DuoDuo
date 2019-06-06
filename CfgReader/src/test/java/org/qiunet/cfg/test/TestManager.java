package org.qiunet.cfg.test;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.cfg.base.ISimpleMapConfig;

import java.util.List;
import java.util.Map;

public class TestManager {
	/*@Test
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
	}*/


	@Test
	public void testJsonInit() throws Exception{
		InitCfg initCfg = (InitCfg) InitJsonManager.getInstance().getSimpleMapJsonCfgManager().getCfgById(1111);
		Assert.assertTrue(initCfg.getId() == 1111);
		Assert.assertEquals(initCfg.getVal2(), 123456);
		Assert.assertEquals(initCfg.getVal(), "1,2,3");
		Assert.assertTrue(initCfg.getVal3() > 1.09d && initCfg.getVal3() < 1.11d);

		Map<Integer, InitCfg> map = InitJsonManager.getInstance().getSimpleMapJsonCfgManager().getCfgs();
		Assert.assertTrue(map.size() == 3);




		Init2Cfg init2Cfg = (Init2Cfg)InitJsonManager.getInstance().getNestMapJsonCfgManager().getCfgByIdAndSubId(2222, "3,4,5");

		Assert.assertTrue(InitJsonManager.getInstance().getNestMapJsonCfgManager().getCfgs().size() == 3);

		Assert.assertTrue(init2Cfg.getId() == 2222);
		Assert.assertEquals(init2Cfg.getVal2(), 123457);
		Assert.assertEquals(init2Cfg.getVal(), "3,4,5");
		Assert.assertTrue(init2Cfg.getVal3() > 1.19d && init2Cfg.getVal3() < 1.21d);



		List<Init3Cfg> initCfgs = (List<Init3Cfg>)InitJsonManager.getInstance().getNestListJsonCfgManager().getCfgListById(3333);

		Assert.assertTrue(initCfgs.size() == 1);
		Assert.assertTrue(InitJsonManager.getInstance().getNestListJsonCfgManager().getCfgs().size() == 3);

		Init3Cfg init3Cfg = initCfgs.get(0);

		Assert.assertTrue(init3Cfg.getId() == 3333);
		Assert.assertEquals(init3Cfg.getVal2(), 123458);
		Assert.assertEquals(init3Cfg.getVal(), "5,6,7");
		Assert.assertTrue(init3Cfg.getVal3() > 1.29d && init3Cfg.getVal3() < 1.31d);
	}
}
