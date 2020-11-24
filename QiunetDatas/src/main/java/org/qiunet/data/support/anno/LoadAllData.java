package org.qiunet.data.support.anno;

import java.lang.annotation.*;

/***
 * 初始化就加载所有数据.
 *
 * @author qiunet
 * 2020-11-24 17:48
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadAllData {
}
