package org.qiunet.function.consume;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.function.base.IOperationType;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
	 * 是否校验过
	 */
	private final AtomicBoolean verified = new AtomicBoolean();
	/**
	 *  消耗的主体
	 *  一般Player
	 */
	private Obj obj;
	/**
	 * 消耗倍数
	 */
	private int multi;
	/**
	 * 消耗的内容
	 */
	private Consumes<Obj> consumes;
	/**
	 * 消耗的类型. 记录日志使用
	 */
	private IOperationType consumeType;
	/**
	 * 消耗的结果
	 */
	ConsumeResult result;

	private ConsumeContext(){}


	static <Obj extends IThreadSafe> ConsumeContext<Obj> valueOf(Obj obj, int multi, Consumes<Obj> consumes, IOperationType consumeType) {
		ConsumeContext<Obj> context = new ConsumeContext<>();
		context.obj = obj;
		context.multi = multi;
		context.consumes = consumes;
		context.consumeType = consumeType;
		return context;
	}

	/**
	 * 执行消耗
	 */
	public void act() {
		if (! verified.get()) {
			throw new CustomException("Need verify first!");
		}

		consumes.act(this);
	}

	public Obj getObj() {
		return obj;
	}

	public IOperationType getConsumeType() {
		return consumeType;
	}

	public int getMulti() {
		return multi;
	}

	public boolean isSuccess(){
		return result == null || result.isSuccess();
	}

	public boolean isFail(){
		return !isSuccess();
	}

	public Map<Integer, Long> getRealConsumes() {
		return realConsumes;
	}
}
