package org.qiunet.flash.handler.context.request.data.pb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 *
 *
 * @author qiunet
 * 2020-09-21 15:35
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PbResponse {
	/**
	 * 协议id
	 * @return
	 */
	int value();
}
