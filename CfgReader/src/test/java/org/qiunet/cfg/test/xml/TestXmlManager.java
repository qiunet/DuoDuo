package org.qiunet.cfg.test.xml;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.cfg.manager.xml.NestListXmlCfgManager;
import org.qiunet.cfg.manager.xml.NestMapXmlCfgManager;
import org.qiunet.cfg.test.Init2Cfg;
import org.qiunet.cfg.test.Init3Cfg;
import org.qiunet.cfg.test.InitCfg;
import org.qiunet.cfg.test.InitWithRewardCfg;
import org.qiunet.utils.classScanner.ClassScanner;

import java.util.List;

/***
 *
 * @author qiunet
 * 2020-02-05 13:31
 **/
public class TestXmlManager {

	private NestListXmlCfgManager<Integer, Init3Cfg> nestListInitManager = new NestListXmlCfgManager<Integer, Init3Cfg>("config/init/init_data.xml"){};
	private NestMapXmlCfgManager<Integer, String, Init2Cfg> nestMapCfgManager = new NestMapXmlCfgManager<Integer, String, Init2Cfg>("config/init/init_data.xml"){};

	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void testSimpleMapInit() throws Exception{
		SimpleMapInitManager.getInstance().loadCfg();
		InitCfg initCfg = SimpleMapInitManager.getInstance().getCfgById(1111);

		Assert.assertTrue(SimpleMapInitManager.getInstance().getCfgs().size() == 3);

		Assert.assertTrue(initCfg.getId() == 1111);
		Assert.assertEquals(initCfg.getVal2(), 123456);
		Assert.assertEquals(initCfg.getVal1(), "1,2,3");
		Assert.assertTrue(initCfg.getVal3() > 1.09d && initCfg.getVal3() < 1.11d);
	}

	@Test
	public void testSimpleMapWithReward() throws Exception {
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
		nestMapCfgManager.loadCfg();
		Init2Cfg initCfg = nestMapCfgManager.getCfgByIdAndSubId(2222, "3,4,5");

		Assert.assertTrue(nestMapCfgManager.getCfgs().size() == 3);

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
