package org.qiunet.utils.timer;

import java.util.concurrent.Callable;
/**
 * Created by qiunet.
 * 2019-03-24 12:15
 */
public interface DelayTask<T> extends Callable<T> {
}
