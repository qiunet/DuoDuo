package org.qiunet.utils.async.future;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 *
 * @author qiunet
 * 2023/10/27 16:13
 */
public class DNettyPromise<V> extends DefaultPromise<V> {

	public DNettyPromise() {
		super(GlobalEventExecutor.INSTANCE);
	}
}
