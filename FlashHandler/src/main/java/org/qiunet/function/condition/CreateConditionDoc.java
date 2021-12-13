package org.qiunet.function.condition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 生成给策划的文档.
 * 策划用来配置
 *
 * @author qiunet
 * 2021/12/13 14:08
 */
public class CreateConditionDoc implements IApplicationContextAware {
	private static List<ICondition> conditions = Lists.newArrayList();
	private CreateConditionDoc(){}

	/**
	 * 生成condition 文件配置文件
	 * @param directory 生成的目录
	 * @throws Exception -
	 */
	public static void generator(File directory) throws Exception {
		Preconditions.checkState(directory != null && directory.isDirectory(), "Directory must be a directory!");
		JSONArray jsonArray = new JSONArray();
		for (ICondition condition : conditions) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", condition.getType().name());
			jsonObject.put("desc", ((IConditionType) condition.getType()).desc());
			JSONArray params = new JSONArray();
			for (Field field : condition.getClass().getDeclaredFields()) {
				if (! field.isAnnotationPresent(ConditionField.class)) {
					continue;
				}
				ConditionField conditionField = field.getAnnotation(ConditionField.class);
				params.add(ImmutableMap.of("param", field.getName(), "desc", conditionField.desc()));
			}
			jsonObject.put("params", params);
			jsonArray.add(jsonObject);
		}
		String name = "ConditionDoc.json";
		FileUtil.createFileWithContent(new File(directory, name), jsonArray.toString());
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		conditions = context.getSubTypesOf(ICondition.class).stream()
				.filter(clz -> ! Modifier.isAbstract(clz.getModifiers()))
				.filter(clz -> clz == ConditionNot.class)
				.map(clz -> {
					try {
						return clz.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new CustomException(e, "class {} can not instance!", clz.getName());
					}
				}).sorted((o1, o2) -> ComparisonChain.start().compare(o1.getType(), o2.getType()).result())
				.collect(Collectors.toList());
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.TESTER;
	}
}
