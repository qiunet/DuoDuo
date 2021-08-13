package org.qiunet.function.condition;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/***
 *
 * 条件生成管理
 * @author qiunet
 * 2020-12-31 12:25
 */
public class ConditionManager {
	public static final IConditions<?> EMPTY_CONDITION = new Conditions<>(ImmutableList.of());
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

		private final Map<String, Class<? extends ICondition>> conditionMap = Maps.newHashMap();

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
							 iCondition = conditionMap.get(cfg.getType()).newInstance();
						 } catch (InstantiationException | IllegalAccessException e) {
							 e.printStackTrace();
						 }
						 iCondition.init(cfg);
						 return iCondition;
					 })
					 .collect(Collectors.toList());
			 return new Conditions(collect);
		 }

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<? extends ICondition>> classes = context.getSubTypesOf(ICondition.class);
			for (Class<? extends ICondition> aClass : classes) {
				if (Modifier.isAbstract(aClass.getModifiers())) {
					continue;
				}

				ICondition instanceOfClass = aClass.newInstance();
				conditionMap.put(instanceOfClass.getType().name(), aClass);
			}
		}
	}
}
