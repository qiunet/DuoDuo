package org.qiunet.function.test.ai;

import org.junit.Test;
import org.qiunet.function.ai.node.executor.RandomExecutor;
import org.qiunet.function.ai.node.executor.SequenceExecutor;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.function.test.ai.action.*;
import org.qiunet.function.test.ai.enums.Enemy;

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
		Escape escape = new Escape(hero);
		Fight fight = new Fight(hero);
		RunToTarget runToTarget1 = new RunToTarget(hero);
		Idle idle = new Idle(hero);
		GetExp getExp = new GetExp(hero);

		BehaviorRootTree tree = new BehaviorRootTree();
		tree.addChild(new RandomExecutor().addChild(idle, getExp),
				new SequenceExecutor().addChild(runToTarget1, fight),
				escape);


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
