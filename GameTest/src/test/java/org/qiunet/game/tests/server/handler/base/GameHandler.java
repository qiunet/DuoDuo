package org.qiunet.game.tests.server.handler.base;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * 基础的handler
 *
 * qiunet
 * 2021/8/20 09:49
 **/
public abstract class GameHandler<Req extends IChannelData> extends PersistConnPbHandler<PlayerActor, Req> {

}
