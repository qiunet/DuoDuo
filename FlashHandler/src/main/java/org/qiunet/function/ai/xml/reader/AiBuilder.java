package org.qiunet.function.ai.xml.reader;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.action.BehaviorActionManager;
import org.qiunet.function.ai.node.action.BehaviorActionParam;
import org.qiunet.function.ai.node.decorator.CounterNode;
import org.qiunet.function.ai.node.decorator.InvertNode;
import org.qiunet.function.ai.node.decorator.RepeatNode;
import org.qiunet.function.ai.node.executor.ParallelExecutor;
import org.qiunet.function.ai.node.executor.RandomExecutor;
import org.qiunet.function.ai.node.executor.SelectorExecutor;
import org.qiunet.function.ai.node.executor.SequenceExecutor;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.function.condition.ConditionManager;
import org.qiunet.function.condition.IConditions;
import org.qiunet.utils.convert.ConvertManager;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.json.TypeReferences;
import org.qiunet.utils.reflect.ReflectUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

import static org.w3c.dom.Node.ELEMENT_NODE;

/***
 * 读取 并生成一个AI root
 *
 * @author qiunet
 * 2021/12/17 17:13
 */
public class AiBuilder<Owner extends MessageHandler<Owner>> {
	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	private static final String CONDITION_ATTRIBUTE_NAME = "condition";
	private static final String NAME_ATTRIBUTE_NAME = "name";
	private static final String RANDOM_EXECUTOR_NAME = "random";
	private static final String SELECTOR_EXECUTOR_NAME = "selector";
	private static final String SEQUENCE_EXECUTOR_NAME = "sequence";
	private static final String PARALLEL_EXECUTOR_NAME = "parallel";
	private static final String REPEAT_TAG_NAME = "repeat";
	private static final String INVERT_TAG_NAME = "invert";
	private static final String COUNTER_TAG_NAME = "counter";
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
			rootTree.setName(rootElement.getAttribute(NAME_ATTRIBUTE_NAME));


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
			IBehaviorNode<Owner> behaviorNode = buildBehaviorNode(element);

			parent.addChild(behaviorNode);
			if (behaviorNode instanceof IBehaviorExecutor) {
				this.forEachChild(item, (IBehaviorExecutor<Owner>) behaviorNode);
			}
		}

	}

	private IBehaviorNode<Owner> buildBehaviorNode(Element element) {
		String tagName = element.getTagName();

		String name = element.getAttribute(NAME_ATTRIBUTE_NAME);
		boolean nameEmpty = element.hasAttribute(NAME_ATTRIBUTE_NAME);
		String condition = element.getAttribute(CONDITION_ATTRIBUTE_NAME);
		IConditions<Owner> conditions = ConditionManager.createCondition(condition);

		switch (tagName) {
			// executor
			case RANDOM_EXECUTOR_NAME:
				return nameEmpty ? new RandomExecutor<>(conditions) : new RandomExecutor<>(conditions, name);
			case SELECTOR_EXECUTOR_NAME:
				return nameEmpty ? new SelectorExecutor<>(conditions) : new SelectorExecutor<>(conditions, name);
			case SEQUENCE_EXECUTOR_NAME:
				return nameEmpty ? new SequenceExecutor<>(conditions) : new SequenceExecutor<>(conditions, name);
			case PARALLEL_EXECUTOR_NAME:
				return nameEmpty ? new ParallelExecutor<>(conditions) : new ParallelExecutor<>(conditions, name);
			// action
			case ACTION_TAG_NAME:
				IBehaviorAction<Owner> action = BehaviorActionManager.instance.createAction(element.getAttribute("clazz"), conditions);
				if (element.hasAttribute("params")) {
					Map<String, String> params = JsonUtil.getGeneralObj(element.getAttribute("params"), TypeReferences.STRING_STRING_MAP);
					params.forEach((fieldName, val) -> {
						Field field = ReflectUtil.findField(action.getClass(), fieldName);
						if (field == null || ! field.isAnnotationPresent(BehaviorActionParam.class)) {
							return;
						}
						ConvertManager.getInstance().covertAndSet(action, field, val);
					});
				}
				return action;
				// action decorator
			case REPEAT_TAG_NAME:
				return new RepeatNode<>(name, Integer.parseInt(element.getAttribute("count")));
			case COUNTER_TAG_NAME:
				return new CounterNode<>(name, Integer.parseInt(element.getAttribute("count")));
			case INVERT_TAG_NAME:
				return new InvertNode<>(name);
			default:
				throw new CustomException("Not support for tag {}", tagName);
		}
	}
}
