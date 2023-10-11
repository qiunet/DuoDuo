package org.qiunet.flash.handler.context.request.check.param;

import io.netty.channel.Channel;
import org.qiunet.cfg.manager.keyval.KeyValManager;
import org.qiunet.flash.handler.context.request.check.IRequestCheck;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.function.badword.BadWordFilter;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;

/***
 * 字符串参数检查
 *
 * @author qiunet
 * 2022/1/5 18:01
 */
class RequestStringParamCheck implements IRequestCheck {
	private final Field field;
	/**
	 * 自定义 最小值
	 */
	private final long min;
	/**
	 * 中文校验
	 */
	private final boolean cnCheck;
	/**
	 * 自定义 最大值
	 */
	private final long max;
	/**
	 * 移除两边空格字符
	 */
	private final boolean trim;
	/**
	 * 对字符串进行{@link StringUtil#powerfulTrim(String)}，只保留中英文+数字
	 */
	private final boolean powerTrim;
	/**
	 * 检查空
	 */
	private final boolean checkEmpty;
	/**
	 * 检查关键字
	 */
	private final boolean checkBadWorld;

	public RequestStringParamCheck(Field field) {
		this.field = field;

		StringParam param = this.field.getAnnotation(StringParam.class);
		this.max = KeyValManager.instance.getOrDefault(param.maxKey(), param.max());
		this.min = KeyValManager.instance.getOrDefault(param.minKey(), param.min());

		this.checkBadWorld = param.checkBadWord();
		this.checkEmpty = param.checkEmpty();
		this.powerTrim = param.powerTrim();
		this.trim = param.trim();

		this.cnCheck = param.cnCheck();
	}

	@Override
	public void check(Channel channel, IChannelData data) {
		String val = (String) ReflectUtil.getField(this.field, data);
		if (this.trim && !StringUtil.isEmpty(val)) {
			val = StringUtil.powerfulTrim(val);
			ReflectUtil.setField(data, field, val);
		}

		if (this.powerTrim && !StringUtil.isEmpty(val)) {
			val = StringUtil.powerfulTrim(val);
			ReflectUtil.setField(data, field, val);
		}

		if (checkEmpty && StringUtil.isEmpty(val)) {
			throw StatusResultException.valueOf(IGameStatus.STRING_PARAM_EMPTY_ERROR);
		}

		int length = 0;
		if (val != null) {
			length = val.length();
			if (cnCheck) {
				length = StringUtil.getMixedStringLength(val);
			}
		}

		if (min != 0 &&  length < min) {
			throw StatusResultException.valueOf(IGameStatus.STRING_PARAM_LENGTH_ERROR);
		}

		if (max != 0 && length > max) {
			throw StatusResultException.valueOf(IGameStatus.STRING_PARAM_LENGTH_ERROR);
		}

		if (val == null) {
			return;
		}

		if (checkBadWorld && BadWordFilter.instance.powerFind(val) != null) {
			throw StatusResultException.valueOf(IGameStatus.STRING_PARAM_BAD_WORD_ERROR);
		}
	}
}
