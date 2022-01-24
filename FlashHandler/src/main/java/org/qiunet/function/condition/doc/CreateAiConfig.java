package org.qiunet.function.condition.doc;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.ConditionField;
import org.qiunet.function.condition.ConditionNot;
import org.qiunet.function.condition.ICondition;
import org.qiunet.function.condition.IConditionType;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 生成给策划的文档.
 * 策划用来配置
 *
 * @author qiunet
 * 2021/12/13 14:08
 */
public class CreateAiConfig implements IApplicationContextAware {
	private static final List<ICondition> conditions = Lists.newArrayList();
	private static final List<Class<? extends IBehaviorAction>> actions = Lists.newArrayList();
	private CreateAiConfig(){}

	/**
	 * 生成condition 文件配置文件
	 * @param directory 生成的目录
	 * @throws Exception -
	 */
	public static void generator(String directory) throws Exception {
		generator(new File(directory));
	}
	/**
	 * 生成condition 文件配置文件
	 * @param directory 生成的目录
	 * @throws Exception -
	 */
	public static void generator(File directory) throws Exception {
		Preconditions.checkState(directory != null && directory.isDirectory(), "Directory must be a directory!");
		AiConfigDoc aiConfigDoc = new AiConfigDoc(buildConditionDocs(), buildAiActionDocs());
		FileUtil.createFileWithContent(new File(directory, "AiConfig.json"), JsonUtil.toJsonString(aiConfigDoc, SerializerFeature.PrettyFormat));
	}

	private static List<AiActionDoc> buildAiActionDocs() {
		List<AiActionDoc> actionDocs = new ArrayList<>();
		for (Class<? extends IBehaviorAction> action : actions) {
			BehaviorAction behaviorAction = action.getAnnotation(BehaviorAction.class);
			actionDocs.add(new AiActionDoc(action.getName(), behaviorAction.name()));
		}
		return actionDocs;
	}

	private static List<ConditionDoc> buildConditionDocs() {
		List<ConditionDoc> conditionDocs = new ArrayList<>();
		for (ICondition condition : conditions) {
			ConditionDoc conditionDoc = new ConditionDoc();
			conditionDoc.setType(condition.getType().name());
			conditionDoc.setDesc(((IConditionType) condition.getType()).desc());
			List<ConditionParamDoc> params = Lists.newLinkedList();
			for (Field field : condition.getClass().getDeclaredFields()) {
				if (! field.isAnnotationPresent(ConditionField.class)) {
					continue;
				}
				ConditionField conditionField = field.getAnnotation(ConditionField.class);
				params.add(ConditionParamDoc.valueOf(field.getName(), field.getType(), conditionField.desc()));
			}
			conditionDoc.setParamDoc(params);
			conditionDocs.add(conditionDoc);
		}
		return conditionDocs;
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		List<ICondition> collect = context.getSubTypesOf(ICondition.class).stream()
				.filter(clz -> !Modifier.isAbstract(clz.getModifiers()))
				.filter(clz -> clz != ConditionNot.class)
				.map(clz -> {
					try {
						return clz.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new CustomException(e, "class {} can not instance!", clz.getName());
					}
				}).sorted((o1, o2) -> ComparisonChain.start().compare(o1.getType(), o2.getType()).result())
				.collect(Collectors.toList());

		conditions.addAll(collect);

		List<Class<? extends IBehaviorAction>> classList = context.getSubTypesOf(IBehaviorAction.class).stream()
				.filter(clz -> !Modifier.isAbstract(clz.getModifiers()))
				.collect(Collectors.toList());
		actions.addAll(classList);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.CREATE_AI_CONFIG;
	}
}
