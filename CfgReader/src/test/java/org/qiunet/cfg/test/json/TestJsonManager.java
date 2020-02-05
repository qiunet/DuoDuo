package org.qiunet.cfg.test.json;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.cfg.test.Init2Cfg;
import org.qiunet.cfg.test.Init3Cfg;
import org.qiunet.cfg.test.InitCfg;
import org.qiunet.cfg.test.json.InitJsonManager;
import org.qiunet.utils.classScanner.ClassScanner;

import java.util.List;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2020-02-04 19:22
 **/
public class TestJsonManager {

	@Test
	public void testJsonInit() throws Exception{
		ClassScanner.getInstance().scanner();

		InitCfg initCfg = InitJsonManager.getInstance().getSimpleMapJsonCfgManager().getCfgById(1111);
		Assert.assertTrue(initCfg.getId() == 1111);
		Assert.assertEquals(initCfg.getVal2(), 123456);
		Assert.assertEquals(initCfg.getVal1(), "1,2,3");
		Assert.assertTrue(initCfg.getVal3() > 1.09d && initCfg.getVal3() < 1.11d);

		Map<Integer, InitCfg> map = InitJsonManager.getInstance().getSimpleMapJsonCfgManager().getCfgs();
		Assert.assertTrue(map.size() == 3);



		Init2Cfg init2Cfg = InitJsonManager.getInstance().getNestMapJsonCfgManager().getCfgByIdAndSubId(2222, "3,4,5");

		Assert.assertTrue(InitJsonManager.getInstance().getNestMapJsonCfgManager().getCfgs().size() == 3);

		Assert.assertTrue(init2Cfg.getId() == 2222);
		Assert.assertEquals(init2Cfg.getVal2(), 123457);
		Assert.assertEquals(init2Cfg.getVal1(), "3,4,5");
		Assert.assertTrue(init2Cfg.getVal3() > 1.19d && init2Cfg.getVal3() < 1.21d);



		List<Init3Cfg> initCfgs = InitJsonManager.getInstance().getNestListJsonCfgManager().getCfgListById(3333);

		Assert.assertTrue(initCfgs.size() == 1);
		Assert.assertTrue(InitJsonManager.getInstance().getNestListJsonCfgManager().getCfgs().size() == 3);

		Init3Cfg init3Cfg = initCfgs.get(0);

		Assert.assertTrue(init3Cfg.getId() == 3333);
		Assert.assertEquals(init3Cfg.getVal2(), 123458);
		Assert.assertEquals(init3Cfg.getVal1(), "5,6,7");
		Assert.assertTrue(init3Cfg.getVal3() > 1.29d && init3Cfg.getVal3() < 1.31d);
	}
}
