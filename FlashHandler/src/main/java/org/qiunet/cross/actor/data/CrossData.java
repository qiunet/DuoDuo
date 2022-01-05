package org.qiunet.cross.actor.data;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.utils.fakeenum.FakeEnum;

/***
 * 跨服获取数据.
 *
 * @author qiunet
 * 2020-10-28 10:50
 */
public abstract class CrossData<Data extends IUserTransferData> extends FakeEnum<CrossData<Data>> {
	/**
	 * 逻辑服创建 对象.
	 * @param playerActor 自己强转成playerActor
	 * @return
	 */
	public abstract Data create(PlayerActor playerActor);
}
