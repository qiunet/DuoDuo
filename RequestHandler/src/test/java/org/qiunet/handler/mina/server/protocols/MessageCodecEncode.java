package org.qiunet.handler.mina.server.protocols;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.qiunet.handler.iodata.adapter.OutputByteStreamBuilder;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.net.IoData;
import org.qiunet.handler.mina.server.iobuffer.IoBufferPool;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * @datetime Jun 1, 2015 12:05:39 AM
 * @desc 协议编码器
 */
public class MessageCodecEncode extends ProtocolEncoderAdapter {
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		IoBuffer ioBuffer = IoBufferPool.getInstance().getFromPool();

		IoData ioData = (IoData) message;
		OutputByteStream obs = OutputByteStreamBuilder.getOutputByteStream();
		ioData.dataWriter(obs);
		ioBuffer.put(obs.getBytes());
		ioBuffer.flip();
		out.write(ioBuffer);
	}
}
