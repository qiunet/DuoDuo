package org.qiunet.function.ai.xml.reader;

import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.action.BehaviorActionManager;
import org.qiunet.function.ai.node.executor.RandomExecutor;
import org.qiunet.function.ai.node.executor.SelectorExecutor;
import org.qiunet.function.ai.node.executor.SequenceExecutor;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.function.condition.ConditionManager;
import org.qiunet.function.condition.IConditions;
import org.qiunet.utils.exceptions.CustomException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

import static org.w3c.dom.Node.ELEMENT_NODE;

/***
 * 读取 并生成一个AI root
 *
 * @author qiunet
 * 2021/12/17 17:13
 */
public class AiBuilder<Owner> {
	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	private static final String CONDITION_ATTRIBUTE_NAME = "condition";
	private static final String RANDOM_EXECUTOR_NAME = "random";
	private static final String SELECTOR_EXECUTOR_NAME = "selector";
	private static final String SEQUENCE_EXECUTOR_NAME = "sequence";
	private static final String ACTION_TAG_NAME = "action";

	/**
	 * 文件路径
	 */
	private final String aiFilePath;
	/**
	 * AI 主体
	 */
	private final Owner owner;
	/**
	 * ai 路径
	 * @param owner
	 * @param aiFilePath ai 描述文件路径
	 */
	public AiBuilder(Owner owner, String aiFilePath) {
		this.aiFilePath = aiFilePath;
		this.owner = owner;
	}

	/**
	 * 构造一个行为树 root 返回root节点
	 * @return
	 */
	public BehaviorRootTree<Owner> buildRootTree() {
		BehaviorRootTree<Owner> rootTree = new BehaviorRootTree<>(owner);
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(aiFilePath) ){
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(inputStream);
			Element rootElement = document.getDocumentElement();
			rootTree.setName(rootElement.getAttribute("name"));


			this.forEachChild(rootElement, rootTree);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootTree;
	}

	private void forEachChild(Node node, IBehaviorExecutor<Owner> parent) throws Exception {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node item = node.getChildNodes().item(i);
			if (item.getNodeType() != ELEMENT_NODE) {
				continue;
			}

			Element element = (Element) item;
			String tagName = element.getTagName();

			String condition = element.getAttribute(CONDITION_ATTRIBUTE_NAME);
			IConditions<Owner> conditions = ConditionManager.createCondition(condition);

			IBehaviorNode<Owner> behaviorNode;
			switch (tagName) {
				case RANDOM_EXECUTOR_NAME:
					behaviorNode = new RandomExecutor<>(conditions);
					break;
				case SELECTOR_EXECUTOR_NAME:
					behaviorNode = new SelectorExecutor<>(conditions);
					break;
				case SEQUENCE_EXECUTOR_NAME:
					behaviorNode = new SequenceExecutor<>(conditions);
					break;
				case ACTION_TAG_NAME:
					behaviorNode = BehaviorActionManager.instance.createAction(element.getAttribute("clazz"), conditions);
					break;
				default:
					throw new CustomException("Not support for tag {}", tagName);
			}
			parent.addChild(behaviorNode);
			if (behaviorNode instanceof IBehaviorExecutor) {
				this.forEachChild(item, (IBehaviorExecutor<Owner>) behaviorNode);
			}
		}

	}
}
