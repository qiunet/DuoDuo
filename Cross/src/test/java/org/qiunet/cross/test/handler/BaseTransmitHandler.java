package org.qiunet.cross.test.handler;

import org.qiunet.cross.common.handler.BaseWsPbTransmitHandler;
import org.qiunet.cross.test.common.actor.PlayerActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 15:54
 */
public abstract class BaseTransmitHandler<REQ extends IpbRequestData> extends BaseWsPbTransmitHandler<PlayerActor, REQ> {
}
