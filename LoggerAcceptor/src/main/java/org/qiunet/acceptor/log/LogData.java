package org.qiunet.acceptor.log;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import org.qiunet.acceptor.cfg.ConfigManager;
import org.qiunet.logger.sender.MsgHeader;

import java.nio.charset.Charset;

/**
 * Created by qiunet.
 * 17/9/22
 */
public class LogData implements Runnable {
	private static Charset charset = Charset.forName("UTF-8");
	private static Logger logger = LoggerUtil.getOutLogger();
	private ByteBuf byteBuf;
	/***
	 * 构造函数
	 * @param byteBuf
	 */
	public LogData(ByteBuf byteBuf) {
		this.byteBuf = byteBuf;
	}
	@Override
	public void run() {
		try {
			MsgHeader header = MsgHeader.parseFrom(byteBuf);
			if (! header.isValidHeader(ConfigManager.getInstance().getSecret(header.getGameId()))){
				logger.error("MsgHeader ["+header.toString()+"] length ["+byteBuf.readableBytes()+"] is error!");
				return;
			}
			byte [] msgBytes = new byte[header.getLength()];
			byteBuf.readBytes(msgBytes);
			ReferenceCountUtil.release(byteBuf);
			String logMsg = new String(msgBytes, charset);
			LoggerUtil.logAppend(header.getGameId(), header.getDt(), logMsg);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
