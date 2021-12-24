package org.qiunet.cfg.test.xd;

import org.junit.jupiter.api.Assertions;
import org.qiunet.cfg.wrapper.INestListCfgWrapper;
import org.qiunet.cfg.wrapper.INestMapCfgWrapper;
import org.qiunet.cfg.wrapper.ISimpleMapCfgWrapper;
import org.qiunet.utils.scanner.anno.AutoWired;

/***
 *
 * @author qiunet
 * 2020-04-24 07:40
 **/
public enum TestXdService {
	instance;

	@AutoWired
	private ISimpleMapCfgWrapper<Integer, XdSimpleMapInitCfg> simpleMapCfgWrapper;
	@AutoWired
	private ISimpleMapCfgWrapper<Integer, XdRewardInitCfg> XdRewardWrapper;
	@AutoWired
	private INestMapCfgWrapper<Integer, String, XdNestMapInitCfg> nestMapCfgWrapper;
	@AutoWired
	private INestListCfgWrapper<Integer, XdNestListInitCfg> nestListCfgWrapper;


	public void testReward(){
		XdRewardInitCfg cfg = XdRewardWrapper.getCfgById(1111);
		Assertions.assertNotNull(cfg.getVal1());
		Assertions.assertEquals(cfg.getVal1().size(), 3);
	}

	public void testNestList(){
		Assertions.assertEquals(3, nestListCfgWrapper.size());
		Assertions.assertTrue(nestListCfgWrapper.contains(1111));
		Assertions.assertTrue(nestListCfgWrapper.contains(1111, 0));

		XdNestListInitCfg cfg = nestListCfgWrapper.getCfgsById(2222, 0);
		Assertions.assertEquals(cfg.getVal2(), 123457);
	}

	public void testNestMap(){
		Assertions.assertTrue(nestMapCfgWrapper.contains(1111, "1;2;3"));
		Assertions.assertEquals(3, nestMapCfgWrapper.size());
		XdNestMapInitCfg cfg = nestMapCfgWrapper.getCfgById(1111, "1;2;3");

		Assertions.assertEquals(cfg.getVal2(), 123456);
	}

	public void testSimpleMap() {
		XdSimpleMapInitCfg cfg = simpleMapCfgWrapper.getCfgById(1111);
		Assertions.assertEquals(3, simpleMapCfgWrapper.size());

		Assertions.assertTrue(simpleMapCfgWrapper.contains(2222));
		Assertions.assertTrue(simpleMapCfgWrapper.contains(3333));

		Assertions.assertEquals(cfg.getVal1(), "1;2;3");
	}
}
