package org.qiunet.flash.handler.context.request.param.check;

import org.qiunet.cfg.manager.keyval.KeyValManager;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.Field;

/***
 * 数值参数校验
 *
 * @author qiunet
 * 2022/1/5 17:03
 */
class NumberParamCheck implements IParamCheck {

	private final Field field;
	/**
	 * 自定义 最小值
	 */
	private final long min;
	/**
	 * 自定义 最大值
	 */
	private final long max;
	/**
	 * 非负数
	 */
	private final boolean positive;


	public NumberParamCheck(Field field) {
		this.field = field;

		NumberParam param = this.field.getAnnotation(NumberParam.class);
		this.max = KeyValManager.instance.getLong(param.maxKey(), param.max());
		this.min = KeyValManager.instance.getLong(param.minKey(), param.min());
		this.positive = param.positive();
	}

	@Override
	public void check(IChannelData data) {
		Number val = (Number) ReflectUtil.getField(this.field, data);
		if (positive && val.longValue() < 0) {
			throw StatusResultException.valueOf(IGameStatus.NUMBER_PARAM_ERROR);
		}

		if (min != 0 &&  val.longValue() < min) {
			throw StatusResultException.valueOf(IGameStatus.NUMBER_PARAM_ERROR);
		}

		if (max != 0 && val.longValue() > max) {
			throw StatusResultException.valueOf(IGameStatus.NUMBER_PARAM_ERROR);
		}
	}
}
