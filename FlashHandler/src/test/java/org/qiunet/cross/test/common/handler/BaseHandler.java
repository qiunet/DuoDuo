package org.qiunet.cross.test.common.handler;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 09:52
 */
public abstract class BaseHandler<T extends IChannelData> extends PersistConnPbHandler<PlayerActor, T> {

}
