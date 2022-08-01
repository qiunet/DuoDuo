package org.qiunet.test.function.test.ai;

import org.junit.jupiter.api.Test;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.executor.RandomExecutor;
import org.qiunet.function.ai.node.executor.SequenceExecutor;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.function.ai.xml.reader.AiBuilder;
import org.qiunet.test.function.test.ai.action.*;
import org.qiunet.test.function.test.ai.condition.SeeGoblinCondition;
import org.qiunet.test.function.test.ai.condition.SeeOmaCondition;
import org.qiunet.test.function.test.ai.enums.Enemy;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 * 从前有一个胆小如鼠的英雄，
 *
 * 他看到哥布林就会跑过去打它；
 * 他看到半兽人就会逃跑；
 * 他看不到哥布林也看不到半兽人就会休息；
 * 他看到哥布林也看到半兽人也会跑
 *
 * qiunet
 * 2021/8/17 10:14
 **/
public class AiTest {
	@Test
	public void test(){
		Hero hero = new Hero();
		Escape escape = new Escape(new SeeOmaCondition());
		Fight fight = new Fight(new SeeGoblinCondition().and(new SeeOmaCondition().not()));
		RunToTarget runToTarget1 = new RunToTarget(new SeeGoblinCondition().and(new SeeOmaCondition().not()));
		Idle idle = new Idle(null);
		GetExp getExp = new GetExp(null);

		BehaviorRootTree<Hero> tree = new BehaviorRootTree<>(hero, false);
		RandomExecutor<Hero> randomExecutor = new RandomExecutor<>(new SeeGoblinCondition().not().and(new SeeOmaCondition().not()));
		randomExecutor.addChild(idle, getExp);

		IBehaviorExecutor<Hero> sequenceExecute = new SequenceExecutor<>(null);
		sequenceExecute.addChild(runToTarget1, fight);

		tree.addChild(randomExecutor, sequenceExecute, escape);
		this.aiTest(tree);
	}



	@Test
	public void readXML(){
		ClassScanner.getInstance(ScannerType.BEHAVIOR_ACTION).scanner("org.qiunet");

		Hero hero = new Hero();
		AiBuilder<Hero> aiBuilder = new AiBuilder<>(hero, "ai.xml");
		BehaviorRootTree<Hero> rootTree = aiBuilder.buildRootTree();
		this.aiTest(rootTree);
	}

	private void aiTest(BehaviorRootTree<Hero> tree) {
		Hero hero = tree.getOwner();
		hero.seeEnemy(Enemy.GOBLIN);
		// 第一次执行跑步中1
		tree.tick();
		// 第二次攻击哥布林
		tree.tick();

		// 看见半兽人. 要逃跑
		hero.seeEnemy(Enemy.OMA);
		tree.tick();

		hero.clearSeeEnemy();
		// 执行5次. 测试随机执行器
		for (int i = 0; i < 5; i++) {
			tree.tick();
		}
	}
}
