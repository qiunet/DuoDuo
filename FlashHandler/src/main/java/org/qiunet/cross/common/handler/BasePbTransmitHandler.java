package org.qiunet.cross.common.handler;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.flash.handler.netty.transmit.ITransmitHandler;

/***
 * 标识转发的handler
 *  如果是跨服的状态.
 *  handler处理会直接转发到跨服服务.
 *
 * @author qiunet
 * 2020-10-26 15:24
 */
public abstract class BasePbTransmitHandler<REQ extends IChannelData>
	extends PersistConnPbHandler<PlayerActor, REQ> implements ITransmitHandler<CrossPlayerActor, REQ> {

}
