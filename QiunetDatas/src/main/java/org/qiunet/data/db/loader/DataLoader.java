package org.qiunet.data.db.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 将某个Bo数据类注册到数据加载中心
 *
 * @author qiunet
 * 2021/11/17 19:34
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataLoader {
	/**
	 * 需要加载的数据Bo class
	 * @return
	 */
	Class<? extends DbEntityBo> value();
}


