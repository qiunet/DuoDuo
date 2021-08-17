package org.qiunet.function.ai.node.root;

import com.google.common.collect.Lists;
import org.qiunet.function.ai.builder.IBehaviorBuilder;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/***
 * 构造一个RootExecutor
 *
 * qiunet
 * 2021/7/26 09:39
 **/
public enum BehaviorManager implements IApplicationContextAware {
	instance;

	private final List<BehaviorBuilderData> datas = Lists.newArrayList();
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<? extends IBehaviorBuilder>> types = context.getSubTypesOf(IBehaviorBuilder.class);
		for (Class<? extends IBehaviorBuilder> type : types) {
			Object builder = context.getInstanceOfClass(type);
			Method method = type.getMethod("buildExecutor", Object.class);
			datas.add(new BehaviorBuilderData(builder, method));
		}
	}

	/**
	 * 构造一个 root executor
	 * @param obj 传入的参数
	 * @return
	 */
	public BehaviorRootTree buildRootExecutor(Object obj) {
		BehaviorRootTree root = new BehaviorRootTree();
		datas.forEach(data -> root.addChild(data.build(obj)));
		root.initialize();
		return root;
	}

	private static class BehaviorBuilderData {
		private final Object obj;
		private final Method method;

		public BehaviorBuilderData(Object obj, Method method) {
			this.obj = obj;
			this.method = method;
		}

		public IBehaviorNode build(Object param){
			Object o = null;
			try {
				o = method.invoke(obj, param);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new CustomException(e, "Call method {}.{} error!", obj.getClass().getSimpleName(), method.getName());
			}
			return ((IBehaviorNode) o);
		}
	}
}
