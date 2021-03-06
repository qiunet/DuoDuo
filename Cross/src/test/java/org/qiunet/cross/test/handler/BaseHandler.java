package org.qiunet.cross.test.handler;

import org.qiunet.cross.test.common.actor.PlayerActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 09:52
 */
public abstract class BaseHandler<T extends IpbChannelData> extends PersistConnPbHandler<PlayerActor, T> {

}
