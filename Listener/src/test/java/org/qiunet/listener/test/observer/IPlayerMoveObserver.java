package org.qiunet.listener.test.observer;

import org.qiunet.listener.observer.IObserver;

/***
 * 移动观察者
 *
 * @author qiunet
 * 2020-09-12 12:48
 */
public interface IPlayerMoveObserver extends IObserver {


	void move(long playerId);
}
