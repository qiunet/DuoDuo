package org.qiunet.function.gm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 标注 gm 命令处理的注解
 *
 * 标注的方法. 第一个参数必须是PlayerActor.
 * 后面参数类型必须是: int  long  string
 *
 * 标注的方法必须返回 IGameStatus, 表示是否执行成功.
 * 也可以抛出异常. 表示错误
 * @author qiunet
 * 2021-01-08 11:43
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GmCommand {
	/***
	 * 排序 大排前.
	 * 之后按照 name 字母顺序.
	 * @return
	 */
	int order() default 0;
	/**
	 * 按钮显示名
	 * @return
	 */
	String commandName();
}
