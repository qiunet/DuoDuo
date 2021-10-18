package org.qiunet.game.tests.server.handler.base;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.game.tests.server.context.PlayerActor;

/***
 * 基础的handler
 *
 * qiunet
 * 2021/8/20 09:49
 **/
public abstract class GameHandler<Req extends IpbChannelData> extends PersistConnPbHandler<PlayerActor, Req> {

}