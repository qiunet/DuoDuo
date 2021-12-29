package org.qiunet.function.consume;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.function.base.IOperationType;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;

/**
 *  消耗的上下文
 * @param <Obj>
 */
public class ConsumeContext<Obj extends IThreadSafe> {
	/**
	 * 记录真实消耗.
	 * 可能底层使用替代资源了.
	 */
	private final Map<Integer, Long> realConsumes = Maps.newHashMap();
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

	public Map<Integer, Long> getRealConsumes() {
		return realConsumes;
	}
}
