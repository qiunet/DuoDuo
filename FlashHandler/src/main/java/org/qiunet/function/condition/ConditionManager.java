package org.qiunet.function.condition;

import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.convert.ConvertManager;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * 条件生成管理
 *
 * @author qiunet
 * 2020-12-31 12:25
 */
public class ConditionManager {
	public static final IConditions<?> EMPTY_CONDITION = new Conditions<>(ImmutableList.of());
	private static final TypeReference<List<ConditionConfig>> TYPE = new TypeReference<List<ConditionConfig>>(){};
	/**
	 * 创建Condition
	 * 字符串支持两个格式:
	 *
	 * 集合内节点都是与的关系
	 * 1.  [{"type": "..."}, {"type": "..."}]
	 * 集合与集合之间是或的关系
	 * 2.  [{"type": "..."}, {"type": "..."}] || [{"type": "..."}]
	 *
	 * 配置cfg里面加入 "not": "true" 结果取反
	 *
	 * @param conditionConfig 条件的字符串配置
	 * @return condition 实例
	 */
	public static IConditions createCondition(String conditionConfig) {
		if (StringUtil.isEmpty(conditionConfig)) {
			return ConditionManager.EMPTY_CONDITION;
		} else if (conditionConfig.contains("||")) {
			String[] splits = StringUtil.split(conditionConfig, "||");
			IConditions conditions = convert(splits[0]);
			if (splits.length > 1) {
				for (int i = 1; i < splits.length; i++) {
					conditions = conditions.or(convert(splits[i]));
				}
			}
			return conditions;
		} else {
			return convert(conditionConfig);
		}
	}

	/**
	 * 条件转换. 外部调用使用 {@link ConditionManager#createCondition(String)}
	 *
	 * @param str
	 * @return
	 */
	private static IConditions convert(String str) {
		List<ConditionConfig> configList = JsonUtil.getGeneralObj(str.trim(), TYPE);
		return ConditionManager.createCondition(configList);
	}
	/**
	 * 创建Condition
	 * @param configList 配置
	 * @return condition 实例
	 */
	public static IConditions createCondition(List<ConditionConfig> configList) {
		return ConditionManager0.instance.createCondition(configList);
	}

	 enum ConditionManager0 implements IApplicationContextAware {
		instance;

		 @Override
		 public ScannerType scannerType() {
			 return ScannerType.CONDITION;
		 }

		 private final Map<String, Class<? extends ICondition>> conditionMap = Maps.newHashMap();

		 @Override
		 public int order() {
			 return Integer.MAX_VALUE - 1;
		 }

		 /**
		 * 创建Condition
		 * @param configList 配置
		 * @return condition 实例
		 */
		IConditions createCondition(List<ConditionConfig> configList) {
			if (configList == null || configList.isEmpty()) {
				return EMPTY_CONDITION;
			}

			 List<ICondition> collect = configList.stream()
					 .map(cfg -> {
						 ICondition iCondition = null;
						 try {
						 	Preconditions.checkNotNull(conditionMap.get(cfg.getType()), "ConditionType %s is not define!", cfg.getType());
							 iCondition = conditionMap.get(cfg.getType().toUpperCase()).newInstance();
						 } catch (InstantiationException | IllegalAccessException e) {
							 e.printStackTrace();
						 }
						 Field[] declaredFields = iCondition.getClass().getDeclaredFields();
						 for (Field field : declaredFields) {
							 if (! field.isAnnotationPresent(ConditionField.class)) {
								 continue;
							 }

							 if (CommonUtil.existInList(field.getName(), "not", "type")) {
								 // 使用到Condition的关键字了.
								 throw new CustomException("condition field {}#{} use key word", field.getDeclaringClass().getName(), field.getName());
							 }

							 ConvertManager.instance.covertAndSet(iCondition, field, String.valueOf(cfg.getValue(field.getName())));
						 }
						 if (cfg.getBoolean("not")) {
							 return iCondition.not();
						 }
						 return iCondition;
					 })
					 .collect(Collectors.toList());
			 return new Conditions(collect);
		 }

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<? extends ICondition>> classes = context.getSubTypesOf(ICondition.class);
			for (Class<? extends ICondition> aClass : classes) {
				if (Modifier.isAbstract(aClass.getModifiers())
				 || aClass == ConditionNot.class
				) {
					continue;
				}

				ICondition instanceOfClass = aClass.newInstance();
				for (Field field : aClass.getDeclaredFields()) {
					if (!field.isAnnotationPresent(ConditionField.class)) {
						continue;
					}
					if (field.getName().equals("not") || field.getName().equals("or")) {
						throw new CustomException("Condition 的字段不能命名为 not  or");
					}
				}
				conditionMap.put(instanceOfClass.getType().name().toUpperCase(), aClass);
			}
		}
	}
}
