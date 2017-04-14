package org.qiunet.gametest.protocol;

import org.qiunet.enums.ProtocolType;
import org.qiunet.gametest.protocol.base.NormalProtocol;

/**
 * @author qiunet
 *         Created on 16/12/14 10:14.
 */
public abstract class HttpsProtocol extends NormalProtocol {
	@Override
	public ProtocolType getProtocolType() {
		return ProtocolType.HTTPS;
	}
}
