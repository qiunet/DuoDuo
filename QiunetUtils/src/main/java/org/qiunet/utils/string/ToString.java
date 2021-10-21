package org.qiunet.utils.string;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/***
 * 对对象进行toString
 *
 * @author qiunet
 * 2021/10/21 15:52
 **/
public class ToString {
	private static final ToStringStyle TO_STRING_STYLE = new DToStringStyle();

	public static String toString(Object obj) {
		return ToStringBuilder.reflectionToString(obj, TO_STRING_STYLE);
	}

	/***
	 * ToStringBuilderStyle 自定义
	 *
	 * @author qiunet
	 * 2021/10/21 16:23
	 **/
	private static class DToStringStyle extends RecursiveToStringStyle {

		public DToStringStyle() {
			super();
			this.setUseShortClassName(true);
			this.setUseIdentityHashCode(false);
		}
	}
}
