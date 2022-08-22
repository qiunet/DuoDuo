package org.qiunet.function.ai.node.root;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.observer.IObserverSupportOwner;
import org.qiunet.flash.handler.common.observer.ObserverSupport;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseDecorator;
import org.qiunet.function.ai.node.executor.RootExecutor;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *  执行器ROOT
 *
 * qiunet
 * 2021/7/26 09:37
 **/
public final class BehaviorRootTree<Owner extends MessageHandler<Owner>> extends BaseDecorator<Owner> implements IObserverSupportOwner<Owner> {
	/**
	 * 准备工作.
	 * 只执行一次
	 */
	private final AtomicBoolean prepared = new AtomicBoolean(false);

	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 观察者
	 */
	private final ObserverSupport<Owner> observerSupport;
	/**
	 * id 分配
	 */
	private final AtomicInteger idMaker = new AtomicInteger();
	/**
	 * 属主
	 */
	private final Owner owner;
	/**
	 * 是否打印日志
	 */
	private final boolean printLog;
	/**
	 * 默认使用 selector 节点作为root节点
	 */
	public BehaviorRootTree(Owner owner){
		this(owner, false);
	}

	public BehaviorRootTree(Owner owner, boolean printLog){
		super(new RootExecutor<>(null, "Root"), "Root");
		this.observerSupport = new ObserverSupport<>(owner);
		this.printLog = printLog;
		this.owner = owner;
	}

	public void tick(){
		if (! prepared.get() && prepared.compareAndSet(false, true)) {
			node.prepare();
		}

		if (! node.isRunning()) {
			// 有的执行器需执行前清理状态. 等
			node.initialize();
			if (printLog) {
				logger.info("===================START===================");
			}
		}
		node.run();
		if (printLog) {
			String log = this.log();

			logger.info("--------------------tick start--------------------");
			logger.info("{}行为树状态:\n{}" , owner.getIdentity(), log.substring(0, log.length() - 1));
			logger.info("-------------------- tick end --------------------");
		}
		// 不管SUCCESS 还是 FAILURE 都执行清理操作.
		if (! node.isRunning()) {
			node.release();
			if (printLog) {
				logger.info("=================== END ===================");
			}
		}
	}

	private String log() {
		return log(this.node, 0);
	}

	/**
	 * 日志打印
	 * @param node
	 * @param tabCount
	 * @return
	 */
	private String log(IBehaviorNode<Owner> node, int tabCount) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.statusLogger().log(tabCount));

		if (! node.statusLogger().isExecuted()) {
			if (node instanceof IBehaviorExecutor) {
				sb.append(StringUtil.repeated("\t", tabCount + 1));
				sb.append("......\n");
			}
			// 没有执行. 下面的node状态就不需要看了.
			return sb.toString();
		}

		if (node instanceof IBehaviorExecutor) {
			for (IBehaviorNode<Owner> childNode : ((IBehaviorExecutor<Owner>) node).getChildNodes()) {
				sb.append(this.log(childNode, tabCount + 1));
			}
		}
		return sb.toString();
	}

	@Override
	public IBehaviorExecutor<Owner> addChild(IBehaviorNode<Owner>... actions) {
		((IBehaviorExecutor<Owner>) this.node).addChild(actions);
		return this;
	}

	/**
	 * 分配一个ID
	 * @return
	 */
	public int generatorId(){
		return idMaker.incrementAndGet();
	}

	@Override
	public BehaviorRootTree<Owner> rootNode() {
		return this;
	}

	@Override
	public int getId() {
		// root 的id为0
		return 0;
	}

	@Override
	public Owner getOwner() {
		return owner;
	}

	@Override
	public ActionStatus run() {
		return node.run();
	}

	@Override
	protected ActionStatus execute() {
		throw new CustomException("Not a callable method");
	}

	@Override
	public ObserverSupport<Owner> getObserverSupport() {
		return observerSupport;
	}
}
