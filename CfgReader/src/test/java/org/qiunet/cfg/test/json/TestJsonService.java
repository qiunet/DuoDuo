package org.qiunet.cfg.test.json;

import org.junit.Assert;
import org.qiunet.cfg.annotation.CfgWrapperAutoWired;
import org.qiunet.cfg.wrapper.INestListCfgWrapper;
import org.qiunet.cfg.wrapper.INestMapCfgWrapper;
import org.qiunet.cfg.wrapper.ISimpleMapCfgWrapper;

/***
 *
 * @author qiunet
 * 2020-04-24 07:40
 **/
public enum TestJsonService {
	instance;

	@CfgWrapperAutoWired
	private ISimpleMapCfgWrapper<Integer, JsonSimpleMapInitCfg> simpleMapCfgWrapper;
	@CfgWrapperAutoWired
	private ISimpleMapCfgWrapper<Integer, JsonRewardInitCfg> jsonRewardWrapper;
	@CfgWrapperAutoWired
	private INestMapCfgWrapper<Integer, String, JsonNestMapInitCfg> nestMapCfgWrapper;
	@CfgWrapperAutoWired
	private INestListCfgWrapper<Integer, JsonNestListInitCfg> nestListCfgWrapper;


	public void testReward(){
		JsonRewardInitCfg cfg = jsonRewardWrapper.getCfgById(1111);
		Assert.assertNotNull(cfg.getVal1());
		Assert.assertEquals(cfg.getVal1().size(), 3);

		Assert.assertEquals(Long.MAX_VALUE, cfg.getVal2());
	}

	public void testNestList(){
		Assert.assertEquals(3, nestListCfgWrapper.size());
		Assert.assertTrue(nestListCfgWrapper.contains(1111));
		Assert.assertTrue(nestListCfgWrapper.contains(1111, 0));

		JsonNestListInitCfg cfg = nestListCfgWrapper.getCfgsById(2222, 0);
		Assert.assertEquals(cfg.getVal2(), 123457);
	}

	public void testNestMap(){
		Assert.assertTrue(nestMapCfgWrapper.contains(1111, "1,2,3"));
		Assert.assertEquals(3, nestMapCfgWrapper.size());
		JsonNestMapInitCfg cfg = nestMapCfgWrapper.getCfgById(1111, "1,2,3");

		Assert.assertEquals(cfg.getVal2(), 123456);
	}

	public void testSimpleMap() {
		JsonSimpleMapInitCfg cfg = simpleMapCfgWrapper.getCfgById(1111);
		Assert.assertEquals(3, simpleMapCfgWrapper.size());

		Assert.assertTrue(simpleMapCfgWrapper.contains(2222));
		Assert.assertTrue(simpleMapCfgWrapper.contains(3333));

		Assert.assertEquals(cfg.getVal1(), "1,2,3");
	}
}
