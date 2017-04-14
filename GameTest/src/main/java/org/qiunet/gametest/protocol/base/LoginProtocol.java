package org.qiunet.gametest.protocol.base;

import org.qiunet.enums.ProtocolType;

/**
 * @author qiunet
 *         Created on 16/12/13 18:58.
 */
public abstract class LoginProtocol implements IProtocol{

	protected ProtocolType type;
	protected LoginProtocol(ProtocolType type) {
		this.type = type;
	}

	public ProtocolType getProtocolType(){
		return type;
	}
}
