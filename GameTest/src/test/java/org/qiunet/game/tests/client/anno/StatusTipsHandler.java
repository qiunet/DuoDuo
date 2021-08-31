package org.qiunet.game.tests.client.anno;

import org.qiunet.game.tests.server.enums.GameStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 状态信息的处理
 *
 * qiunet
 * 2021/8/8 21:05
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusTipsHandler {
	/**
	 *  status id array.
	 *
	 * @return status message id
	 */
	GameStatus [] value();
}
