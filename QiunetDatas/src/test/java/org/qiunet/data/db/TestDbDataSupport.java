package org.qiunet.data.db;

import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.data.db.loader.DataLoader;
import org.qiunet.data.db.loader.PlayerDataLoader;
import org.qiunet.data.support.DbDataListSupport;
import org.qiunet.data.support.DbDataSupport;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.Map;

public class TestDbDataSupport {
	@DataLoader(PlayerBo.class)
	private static final DbDataSupport<Long, PlayerDo, PlayerBo> dataSupport = new DbDataSupport<>(PlayerDo.class, PlayerBo::new);
	@DataLoader(ItemBo.class)
	private static final DbDataListSupport<Long, Integer, ItemDo, ItemBo> dataListSupport = new DbDataListSupport<>(ItemDo.class, ItemBo::new);

	private static final PlayerDataLoader playerDataLoader = new PlayerDataLoader(10000);

	@BeforeClass
	public static void init(){
		ClassScanner.getInstance(ScannerType.PLAYER_DATA_LOADER).scanner();
	}

	@Test
	public void testEntity() {

		String name = "秋阳";
		PlayerDo playerDo = new PlayerDo();
		playerDo.setExp(1111111111111L);
		playerDo.setName("秋阳1");
		playerDo.setLevel(10);
		playerDo.setUid(playerDataLoader.getPlayerId());
		PlayerBo playerBo = playerDataLoader.insertDo(playerDo);

		playerBo.getDo().setName(name);
		playerBo.getDo().setLevel(100);
		playerBo.update();

		PlayerBo bo = playerDataLoader.getData(PlayerBo.class);
		Assert.assertEquals(bo.getDo().getName(), name);
		Assert.assertEquals(bo.getDo().getLevel(), 100);

		bo.delete();

		bo = playerDataLoader.getData(PlayerBo.class);
		Assert.assertNull(bo);
	}

	@Test
	public void testEntityList(){

		ItemDo itemDo1 = new ItemDo();
		itemDo1.setUid(playerDataLoader.getPlayerId());
		itemDo1.setItem_id(1);
		itemDo1.setCount(1);
		ItemBo bo1 = playerDataLoader.insertDo(itemDo1);

		ItemDo itemDo2 = new ItemDo();
		itemDo2.setUid(playerDataLoader.getPlayerId());
		itemDo2.setItem_id(2);
		itemDo2.setCount(2);
		ItemBo bo2 = playerDataLoader.insertDo(itemDo2);

		Map<Integer, ItemBo> boMap = playerDataLoader.getMapData(ItemBo.class);
		Assert.assertEquals(boMap.size(), 2);

		for (ItemBo bo : boMap.values()) {
			Assert.assertTrue(bo.getDo().getItem_id() >= 1 && bo.getDo().getItem_id() <= 2);
			Assert.assertEquals(bo.getDo().getCount(), bo.getDo().getItem_id());
		}

		bo2.getDo().setCount(3);
		bo2.update();

		boMap = playerDataLoader.getMapData(ItemBo.class);
		boMap.values().forEach(po -> {
			if (po.getDo().getItem_id() == 2){
				Assert.assertEquals(3, po.getDo().getCount());
			}
		});

		Maps.newHashMap(boMap).values().forEach(ItemBo::delete);

		boMap = playerDataLoader.getMapData(ItemBo.class);
		Assert.assertTrue(boMap.isEmpty());
	}
}
