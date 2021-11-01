package org.qiunet.utils.string;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Field;

/***
 * 对对象进行toString
 *
 * @author qiunet
 * 2021/10/21 15:52
 **/
public class ToString {
	private static final ToStringStyle TO_STRING_STYLE = new DToStringStyle();

	public static String toString(Object obj) {
		return new DReflectionToStringBuilder(obj, TO_STRING_STYLE).toString();
	}

	/***
	 * ToStringBuilderStyle 自定义
	 *
	 * @author qiunet
	 * 2021/10/21 16:23
	 **/
	private static class DToStringStyle extends MultilineRecursiveToStringStyle {

		public DToStringStyle() {
			super();
			this.setUseShortClassName(true);
			this.setUseIdentityHashCode(false);
		}


	}

	private static class DReflectionToStringBuilder extends ReflectionToStringBuilder {

		public DReflectionToStringBuilder(Object object, ToStringStyle style) {
			super(object, style);
		}

		@Override
		protected boolean accept(Field field) {
			if (field.getDeclaringClass() == Enum.class) {
				return false;
			}
			return super.accept(field);
		}
	}
}
