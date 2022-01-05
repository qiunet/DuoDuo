package org.qiunet.flash.handler.context.request.param.check;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2022/1/5 17:56
 */
enum ParamCheckType {

	STRING {
		@Override
		boolean match(Field field) {
			return field.isAnnotationPresent(StringParam.class) && field.getType() == String.class;
		}

		@Override
		IParamCheck build(Field field) {
			return new StringParamCheck(field);
		}
	},

	NUMBER {
		@Override
		boolean match(Field field) {
			return field.isAnnotationPresent(NumberParam.class) && (
					Number.class.isAssignableFrom(field.getType())
					|| field.getType() == double.class
					|| field.getType() == float.class
					|| field.getType() == long.class
					|| field.getType() == int.class
			);
		}

		@Override
		IParamCheck build(Field field) {
			return new NumberParamCheck(field);
		}
	};

	/**
	 * 是否匹配
	 * @param field
	 * @return
	 */
	abstract boolean match(Field field);

	/**
	 * 给出  IParamCheck
	 * @param field
	 * @return
	 */
	abstract IParamCheck build(Field field);
	static final ParamCheckType [] values = values();
}
