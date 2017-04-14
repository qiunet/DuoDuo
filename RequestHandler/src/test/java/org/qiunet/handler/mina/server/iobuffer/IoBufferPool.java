package org.qiunet.handler.mina.server.iobuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.pool.BasicPool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/3/15 20:40.
 */
public class IoBufferPool extends BasicPool<IoBuffer> {
	private volatile static IoBufferPool instance;

	public static IoBufferPool getInstance() {
		if (instance == null) {
			synchronized (IoBufferPool.class) {
				if (instance == null)
				{
					Map<String, Integer> param = new HashMap<>();
					param.put("IoBufferPool.maxIdel", 20);
					param.put("IoBufferPool.minIdel", 10);
					param.put("IoBufferPool.maxActive", 100);
					param.put("IoBufferPool.maxWaitTimeout", 100);
					
					new IoBufferPool(new KeyValueData(param));
				}
			}
		}
		return instance;
	}
	private IoBufferPool(IKeyValueData keyValueData) {
		super(keyValueData);
		instance = this;
	}
	@Override
	protected IoBuffer create() {
		IoBuffer ioBuffer = IoBuffer.allocate(2048, false);
		ioBuffer.setAutoExpand(true);
		return ioBuffer;
	}

	@Override
	protected void clear(IoBuffer ioBuffer) {
		ioBuffer.clear();
	}

	@Override
	protected void close(IoBuffer ioBuffer) {
		ioBuffer.clear();
	}
}
