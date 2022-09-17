package org.qiunet.cross.actor.data;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.utils.fakeenum.FakeEnum;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/***
 * 跨服获取数据.
 *
 * @author qiunet
 * 2020-10-28 10:50
 */
public abstract class CrossData<Data extends IUserTransferData> extends FakeEnum<CrossData<Data>> {
	/**
	 * data 数据的class
	 */
	private final Class<Data> dataClz;
	protected CrossData() {
		Type type = this.getClass().getGenericSuperclass();
		Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
		this.dataClz = ((Class<Data>) actualTypeArguments[0]);
	}

	public Class<Data> getDataClz() {
		return dataClz;
	}

	/**
	 * 逻辑服创建 对象.
	 * @param playerActor 自己强转成playerActor
	 * @return
	 */
	public abstract Data get(PlayerActor playerActor);

	/**
	 * 触发数据更新
	 * @param data cross端传过来的数据
	 */
	public abstract void update(PlayerActor playerActor, Data data);
}
