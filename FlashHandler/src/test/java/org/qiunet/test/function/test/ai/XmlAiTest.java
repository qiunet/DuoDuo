package org.qiunet.test.function.test.ai;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.function.ai.xml.reader.AiBuilder;
import org.qiunet.utils.scanner.ClassScanner;

/***
 *
 * @author qiunet
 * 2021/12/17 17:40
 */
public class XmlAiTest {

	@BeforeClass
	public static void init() {
		ClassScanner.getInstance().scanner("org.qiunet");
	}

	@Test
	public void read(){
		Hero hero = new Hero();
		AiBuilder<Hero> aiBuilder = new AiBuilder<>(hero, "ai.xml");
		BehaviorRootTree<Hero> rootTree = aiBuilder.buildRootTree();
		AiTest.aiTest(rootTree);
	}
}
