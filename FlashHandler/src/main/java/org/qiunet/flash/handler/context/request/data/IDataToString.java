package org.qiunet.flash.handler.context.request.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/***
 * data -> toString
 *
 * @author qiunet
 * 2020-10-27 16:45
 */
public interface IDataToString {
	/**
	 * 转成string
	 * @return
	 */
	default String _toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
