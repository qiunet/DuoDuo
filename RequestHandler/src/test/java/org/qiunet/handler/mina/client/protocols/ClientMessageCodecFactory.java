package org.qiunet.handler.mina.client.protocols;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.qiunet.handler.mina.server.protocols.MessageCodecEncode;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * @datetime May 31, 2015 11:53:28 PM
 * @desc 协议编解码器工厂类
 */
public class ClientMessageCodecFactory implements ProtocolCodecFactory {
	private MessageCodecEncode encode;
	private CumulativeDecode decode;

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		if(encode==null) encode = new MessageCodecEncode();
		return encode;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		if(decode==null) decode = new CumulativeDecode();
		return decode;
	}
}
