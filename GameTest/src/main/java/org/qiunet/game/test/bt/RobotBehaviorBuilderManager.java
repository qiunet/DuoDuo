package org.qiunet.game.test.bt;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.util.List;
import java.util.Set;

/***
 * 构造一个RootExecutor
 *
 * qiunet
 * 2021/7/26 09:39
 **/
public enum RobotBehaviorBuilderManager implements IApplicationContextAware {
	instance;

	private final List<BehaviorBuilderData> datas = Lists.newArrayList();
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<? extends IBehaviorBuilder>> types = context.getSubTypesOf(IBehaviorBuilder.class);
		for (Class<? extends IBehaviorBuilder> type : types) {
			IBehaviorBuilder builder = (IBehaviorBuilder) context.getInstanceOfClass(type);
			datas.add(new BehaviorBuilderData(builder));
		}
	}

	/**
	 * 构造一个 root executor
	 * @param obj 传入的参数
	 * @return
	 */
	public <Owner extends MessageHandler<Owner>> BehaviorRootTree<Owner> buildRootExecutor(Owner obj, boolean printLog) {
		BehaviorRootTree<Owner> root = new BehaviorRootTree<>(obj, printLog);
		datas.forEach(data -> root.addChild(data.build(obj)));
		root.initialize();
		return root;
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.ROBOT_BEHAVIOR_BUILDER;
	}

	private static class BehaviorBuilderData<Owner extends MessageHandler<Owner>> {
		private final IBehaviorBuilder<Owner> obj;

		public BehaviorBuilderData(IBehaviorBuilder<Owner> obj) {
			this.obj = obj;
		}

		public IBehaviorNode<Owner> build(Owner param){
			IBehaviorNode<Owner> node = obj.buildExecutor(param);
			node.setName(obj.getClass().getSimpleName());
			return node;
		}
	}
}
