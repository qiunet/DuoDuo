package org.qiunet.flash.handler.context.status;

import java.util.Objects;

/***
 * GameStatus 断言
 *
 * @author qiunet
 * 2022/11/22 18:00
 */
public final class GameStatusAssert {
	/**
	 * 断言机制
	 * @param condition 条件. 如果为false, 会抛出status给前端
	 * @param status 条件false时候抛出
	 */
	public static void assertTrue(boolean condition, IGameStatus status) {
		if (! condition) {
			throw StatusResultException.valueOf(status);
		}
	}
	/**
	 * 断言机制
	 * @param condition 条件. 如果为true, 会抛出status给前端
	 * @param status 条件true时候抛出
	 */
	public static void assertFalse(boolean condition, IGameStatus status) {
		if (condition) {
			throw StatusResultException.valueOf(status);
		}
	}
	/**
	 * 断言机制
	 * @param o1 参数1
	 * @param o2 参数2
	 * @param status o1 != o2  时候抛出
	 */
	public static void assertEquals(Object o1, Object o2, IGameStatus status) {
		if (! Objects.equals(o1, o2)) {
			throw StatusResultException.valueOf(status);
		}
	}
	/**
	 * 断言机制
	 * @param o1 参数1
	 * @param o2 参数2
	 * @param status o1 == o2  时候抛出
	 */
	public static void assertNotEquals(Object o1, Object o2, IGameStatus status) {
		if (Objects.equals(o1, o2)) {
			throw StatusResultException.valueOf(status);
		}
	}
}
