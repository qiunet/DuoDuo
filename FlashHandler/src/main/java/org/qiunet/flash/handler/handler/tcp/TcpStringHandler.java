package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.player.IPlayerActor;

/**
 * Created by qiunet.
 * 17/11/21
 */
public abstract class TcpStringHandler<P extends IPlayerActor> extends BaseTcpHandler<P, String> {

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String parseRequestData(byte[] bytes) {
		return getDataType().parseBytes(bytes);
	}
}
