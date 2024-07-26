package org.qiunet.flash.handler.common.annotation;

import org.qiunet.data.util.ServerConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 跳过调试输出内容打印.
 *
 * @author qiunet
 * 2020-09-23 15:17
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipDebugOut {
	/**
	 * 是否只跳过正式服
	 * @return true 仅线上跳过
	 */
	boolean onlyOfficial() default false;

	class DebugOut {
		public static boolean test(Class<?> clz) {
			if (ServerConfig.isDebugEnv()) {
				return true;
			}

			if (! clz.isAnnotationPresent(SkipDebugOut.class)) {
				return true;
			}

			if (! ServerConfig.isOfficial()) {
				return clz.getAnnotation(SkipDebugOut.class).onlyOfficial();
			}

			return false;
		}
	}
}
