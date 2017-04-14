package org.qiunet.handler.mina.server.protocols;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * @datetime May 31, 2015 11:53:28 PM
 * @desc 协议编解码器工厂类
 */
public class MessageCodecFactory implements ProtocolCodecFactory {
	private MessageCodecEncode encode;
	private MessageCodecCumulativeDecode decode;

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		if(encode==null) encode = new MessageCodecEncode();
		return encode;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		if(decode==null) decode = new MessageCodecCumulativeDecode();
		return decode;
	}
}
