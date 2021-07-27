package org.qiunet.function.test.attr;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.function.attr.IAttrChangeObserver;
import org.qiunet.function.attr.buff.AttrRctNodeBuff;
import org.qiunet.function.attr.manager.AttrManager;
import org.qiunet.function.attr.tree.AttrBox;
import org.qiunet.function.attr.tree.AttrRoad;
import org.qiunet.function.test.attr.equip.EquipAttrNode;
import org.qiunet.function.test.attr.equip.EquipPostion;
import org.qiunet.function.test.targets.PlayerActor;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.ClassScanner;
import org.slf4j.Logger;

/***
 *
 *
 * @author qiunet
 * 2020-11-20 16:59
 */
public class AttrTest {
	private static final Logger logger = LoggerType.DUODUO.getLogger();

	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void test(){
		AttrManager.printAttrTree();
		PlayerActor playerActor = new PlayerActor(1000, "qiunet");

		AttrBox<PlayerActor, AttrType> attrBox = AttrManager.buildAttrBox(playerActor);
		AttrRoad equipBaseRoad = EquipAttrNode.BASE.builderRoad();
		attrBox.attach(IAttrChangeObserver.class, (road, changed) -> {
			// 业务在这里处理属性的变更.
			logger.info("=====CHANGED=======: {}", JsonUtil.toJsonString(changed));
		});
		// 加入装备基础属性. 并对基础属性进行加成.
		attrBox.replace(equipBaseRoad, ImmutableMap.of(AttrType.ATK, 100L, AttrType.HP, 1000L));
		attrBox.replace(equipBaseRoad, AttrBuffType.allEquipBaseRct, new AttrRctNodeBuff<>(1000));
		Assert.assertEquals(110L, attrBox.get(AttrType.ATK));
		Assert.assertEquals(1100L, attrBox.get(AttrType.HP));

		// 加入头部数据, 测试ValueType rct类型
		AttrRoad gemHeadRoad = EquipAttrNode.GEM_POSITION.builderRoad(EquipPostion.HEAD);
		attrBox.replace(gemHeadRoad, ImmutableMap.of(AttrType.ATK, 100L, AttrType.R_HP, 1000L));
		Assert.assertEquals(210L, attrBox.get(AttrType.ATK));
		Assert.assertEquals(1210L, attrBox.get(AttrType.HP));

		// 加入手部数据
		AttrRoad gemHandRoad = EquipAttrNode.GEM_POSITION.builderRoad(EquipPostion.HAND);
		attrBox.replace(gemHandRoad, ImmutableMap.of(AttrType.ATK, 100L, AttrType.DEF, 1000L));
		Assert.assertEquals(310L, attrBox.get(AttrType.ATK));
		Assert.assertEquals(1000L, attrBox.get(AttrType.DEF));

		// 假如腿部属性数据
		AttrRoad gemLegRoad = EquipAttrNode.GEM_POSITION.builderRoad(EquipPostion.LEG);
		attrBox.replace(gemLegRoad, ImmutableMap.of(AttrType.ATK, 1000L, AttrType.DEF, 1000L));
		Assert.assertEquals(1310L, attrBox.get(AttrType.ATK));
		Assert.assertEquals(2000L, attrBox.get(AttrType.DEF));

		// 仅给腿部进行百分比加成
		attrBox.replace(gemLegRoad, AttrBuffType.EquipGemLegRct, new AttrRctNodeBuff<>(1000));
		Assert.assertEquals(1410L, attrBox.get(AttrType.ATK));
		Assert.assertEquals(2100L, attrBox.get(AttrType.DEF));

	}
}
