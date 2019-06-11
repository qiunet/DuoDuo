package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.context.header.IProtocolHeaderAdapter;

import java.net.InetSocketAddress;

public interface IClientConfig {

	IProtocolHeaderAdapter getProtocolHeaderAdapter();

	InetSocketAddress getAddress();
}
