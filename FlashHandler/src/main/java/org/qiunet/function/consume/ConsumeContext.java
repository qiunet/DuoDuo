package org.qiunet.function.consume;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.function.base.IOperationType;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;

/**
 *  消耗的上下文
 * @param <Obj>
 */
public class ConsumeContext<Obj extends IThreadSafe> implements IArgsContainer {
	private final ArgsContainer argsContainer = new ArgsContainer();
	/**
	 * 记录真实消耗.
	 * 可能底层使用替代资源了.
	 */
	private final Map<BaseConsume<Obj>, Map<Integer, Long>> realConsumes = Maps.newHashMap();
	/**
	 * 使用ItemId 记录的消耗数据
	 */
	private final Map<String, Long> itemIdConsumes = Maps.newHashMap();
	/**
	 *  消耗的主体
	 *  一般Player
	 */
	private Obj obj;
	/**
	 * 消耗的内容
	 */
	private Consumes<Obj> consumes;
	/**
	 * 操作的类型. 记录日志使用
	 */
	private IOperationType operationType;
	/**
	 * 消耗的结果
	 */
	StatusResult result;

	private ConsumeContext(){}


	static <Obj extends IThreadSafe> ConsumeContext<Obj> valueOf(Obj obj, Consumes<Obj> consumes, IOperationType operationType) {
		ConsumeContext<Obj> context = new ConsumeContext<>();
		context.obj = obj;
		context.consumes = consumes;
		context.operationType = operationType;
		return context;
	}

	/**
	 * 执行消耗
	 */
	public void act() {
		if (isFail()) {
			throw new CustomException("Verify consumes result is fail!");
		}

		consumes.act(this);
	}

	public void failThrowException() {
		// == null 说明没有校验.
		if (result.isFail()) {
			throw StatusResultException.valueOf(result);
		}
	}

	public Obj getObj() {
		return obj;
	}

	public <T extends Enum<T> & IOperationType> T getOperationType() {
		return (T)operationType;
	}

	public boolean isSuccess(){
		return result != null && result.isSuccess();
	}

	public boolean isFail(){
		return !isSuccess();
	}

	/**
	 * 获得某个consume 产生的真实消耗
	 * @param consume
	 * @return
	 */
	public Map<Integer, Long> getRealConsumes(BaseConsume<Obj> consume) {
		return realConsumes.computeIfAbsent(consume, key -> Maps.newHashMapWithExpectedSize(8));
	}

	/**
	 * 得到itemId 对应的数量.
	 * @return
	 */
	public Map<String, Long> getItemIdConsumes() {
		return itemIdConsumes;
	}

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return argsContainer.getArgument(key, computeIfAbsent);
	}
}
