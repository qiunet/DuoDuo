package org.qiunet.data.db;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

	private static final PlayerDataLoader playerDataLoader = new PlayerDataLoader(Runnable::run, () -> true, 10000);

	@BeforeAll
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
		playerDataLoader.syncToDb();

		playerBo.getDo().setName(name);
		playerBo.getDo().setLevel(100);
		playerBo.update();
		playerDataLoader.syncToDb();

		PlayerBo bo = playerDataLoader.getData(PlayerBo.class);
		Assertions.assertEquals(bo.getDo().getName(), name);
		Assertions.assertEquals(bo.getDo().getLevel(), 100);

		bo.delete();
		playerDataLoader.syncToDb();

		bo = playerDataLoader.getData(PlayerBo.class);
		Assertions.assertNull(bo);
	}

	@Test
	public void testEntityList(){
		ItemDo itemDo1 = new ItemDo();
		itemDo1.setUid(playerDataLoader.getPlayerId());
		itemDo1.setItem_id(1);
		itemDo1.setCount(1);
		ItemBo bo1 = playerDataLoader.insertDo(itemDo1);
		playerDataLoader.syncToDb();

		ItemDo itemDo2 = new ItemDo();
		itemDo2.setUid(playerDataLoader.getPlayerId());
		itemDo2.setItem_id(2);
		itemDo2.setCount(2);
		ItemBo bo2 = playerDataLoader.insertDo(itemDo2);
		playerDataLoader.syncToDb();

		Map<Integer, ItemBo> boMap = playerDataLoader.getMapData(ItemBo.class);
		Assertions.assertEquals(boMap.size(), 2);

		for (ItemBo bo : boMap.values()) {
			Assertions.assertTrue(bo.getDo().getItem_id() >= 1 && bo.getDo().getItem_id() <= 2);
			Assertions.assertEquals(bo.getDo().getCount(), bo.getDo().getItem_id());
		}

		bo2.getDo().setCount(3);
		bo2.update();
		playerDataLoader.syncToDb();

		boMap = playerDataLoader.getMapData(ItemBo.class);
		boMap.values().forEach(po -> {
			if (po.getDo().getItem_id() == 2){
				Assertions.assertEquals(3, po.getDo().getCount());
			}
		});

		Maps.newHashMap(boMap).values().forEach(ItemBo::delete);
		playerDataLoader.syncToDb();

		boMap = playerDataLoader.getMapData(ItemBo.class);
		Assertions.assertTrue(boMap.isEmpty());
	}
}
