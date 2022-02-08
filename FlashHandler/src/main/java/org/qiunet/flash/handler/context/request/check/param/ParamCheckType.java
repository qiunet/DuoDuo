package org.qiunet.flash.handler.context.request.check.param;

import org.qiunet.flash.handler.context.request.check.IRequestCheck;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2022/1/5 17:56
 */
public enum ParamCheckType {

	STRING {
		@Override
		public boolean match(Field field) {
			return field.isAnnotationPresent(StringParam.class) && field.getType() == String.class;
		}

		@Override
		public IRequestCheck build(Field field) {
			return new RequestStringParamCheck(field);
		}
	},

	NUMBER {
		@Override
		public boolean match(Field field) {
			return field.isAnnotationPresent(NumberParam.class) && (
					Number.class.isAssignableFrom(field.getType())
					|| field.getType() == double.class
					|| field.getType() == float.class
					|| field.getType() == long.class
					|| field.getType() == int.class
			);
		}

		@Override
		public IRequestCheck build(Field field) {
			return new RequestNumberParamCheck(field);
		}
	};

	/**
	 * 是否匹配
	 * @param field
	 * @return
	 */
	public abstract boolean match(Field field);

	/**
	 * 给出  IParamCheck
	 * @param field
	 * @return
	 */
	public abstract IRequestCheck build(Field field);
	public static final ParamCheckType [] values = values();
}
