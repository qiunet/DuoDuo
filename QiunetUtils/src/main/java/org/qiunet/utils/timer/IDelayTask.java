package org.qiunet.utils.timer;

/**
 * Created by qiunet.
 * 2019-03-24 12:15
 */
public interface IDelayTask<T> {

	T call() throws Exception;
}
