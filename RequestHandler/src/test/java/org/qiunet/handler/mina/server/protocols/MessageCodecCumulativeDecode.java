package org.qiunet.handler.mina.server.protocols;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.qiunet.handler.handler.IHandler;
import org.qiunet.handler.handler.RequestHandlerMapping;
import org.qiunet.handler.iodata.adapter.InputByteStreamBuilder;
import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.iodata.net.LeaderIoData;

/**
 * @author Zero
 * @mail baozilaji@126.com
 * @datetime Jun 6, 2015 3:53:36 PM
 * @desc 断包和粘包的特殊处理
 */
public class MessageCodecCumulativeDecode extends CumulativeProtocolDecoder {
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		in.mark();
		if (in.remaining() >= LeaderIoData.LEADER_DATA_LENGTH){
			byte [] header = new byte[LeaderIoData.LEADER_DATA_LENGTH];
			InputByteStream leader = null , body = null;
			try {
				in.get(header);
				LeaderIoData leaderIoData = new LeaderIoData();
				leader = InputByteStreamBuilder.getInputByteStream(header);
				leaderIoData.dataReader(leader);

				if (leaderIoData.getCmdId() > 0 && leaderIoData.getLength() > 0 && in.remaining() >= leaderIoData.getLength() ) {
					in.reset();
					byte [] datas = new byte[leaderIoData.getLength() + LeaderIoData.LEADER_DATA_LENGTH];
					in.get(datas);

					IHandler handler = RequestHandlerMapping.getInstance().getHandler(leaderIoData.getCmdId());
					AbstractRequestData requestData = handler.getRequestDataObj();
					body = InputByteStreamBuilder.getInputByteStream(datas);
					requestData.dataReader(body);
					out.write(requestData);
					return true;
				}
			}catch (Exception e) {
				e.printStackTrace();
				return true;
			}finally {
				if (leader != null) leader.close();
				if (body != null) body.close();
			}
		}
		in.reset();
		return false;
	}
}
