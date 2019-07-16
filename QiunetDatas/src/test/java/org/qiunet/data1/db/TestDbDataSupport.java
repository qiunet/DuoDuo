package org.qiunet.data1.db;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data1.support.DbDataListSupport;
import org.qiunet.data1.support.DbDataSupport;

import java.util.Map;

public class TestDbDataSupport {

	private static DbDataSupport<Long, PlayerPo, PlayerVo> dataSupport = new DbDataSupport<>(PlayerPo.class, PlayerVo::new);

	private static DbDataListSupport<Long, Integer, ItemPo, ItemVo> dataListSupport = new DbDataListSupport<>(ItemPo.class, ItemVo::new);
	private long uid = 10000;

	@Test
	public void testEntity() {
		String name = "秋阳";
		PlayerPo playerPo = new PlayerPo();
		playerPo.setExp(1111111111111L);
		playerPo.setName("秋阳1");
		playerPo.setLevel(10);
		playerPo.setUid(uid);
		PlayerVo playerVo = playerPo.insert();

		playerVo.getPo().setName(name);
		playerVo.getPo().setLevel(100);
		playerVo.getPo().update();

		PlayerVo vo = dataSupport.getVo(uid);
		Assert.assertEquals(vo.getPo().getName(), name);
		Assert.assertEquals(vo.getPo().getLevel(), 100);

		vo.getPo().delete();

		vo = dataSupport.getVo(uid);
		Assert.assertNull(vo);
	}

	@Test
	public void testEntityList(){

		ItemPo itemPo1 = new ItemPo();
		itemPo1.setUid(uid);
		itemPo1.setItem_id(1);
		itemPo1.setCount(1);
		ItemVo vo1 = itemPo1.insert();

		ItemPo itemPo2 = new ItemPo();
		itemPo2.setUid(uid);
		itemPo2.setItem_id(2);
		itemPo2.setCount(2);
		ItemVo vo2 = itemPo2.insert();

		Map<Integer, ItemVo> voMap = dataListSupport.getVoMap(uid);
		Assert.assertEquals(voMap.size(), 2);

		for (ItemVo vo : voMap.values()) {
			Assert.assertTrue(vo.getPo().getItem_id() >= 1 && vo.getPo().getItem_id() <= 2);
			Assert.assertEquals(vo.getPo().getCount(), vo.getPo().getItem_id());
		}

		vo2.getPo().setCount(3);
		vo2.getPo().update();

		voMap = dataListSupport.getVoMap(uid);
		voMap.values().forEach(po -> {
			if (po.getPo().getItem_id() == 2){
				Assert.assertEquals(3, po.getPo().getCount());
			}
		});

		voMap.values().forEach(vo -> vo.getPo().delete());

		voMap = dataListSupport.getVoMap(uid);
		Assert.assertTrue(voMap.isEmpty());
	}
}
