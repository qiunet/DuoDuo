package org.qiunet.entity2table.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 用于注解需要进行数据初始化而执行的方法，其实也可以用来做其他事情，只是如果数据库没有数据的时候会执行被注解的方法而已。
 */
//表示注解加在方法等
@Target(ElementType.METHOD)
//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
//将此注解包含在javadoc中
@Documented
public @interface InitData {

}
