package org.qiunet.flash.handler.gamecfg;

/**
 * 简单的一层map的接口
 * Map<key, Cfg>
 * Created by qiunet.
 * 17/6/3
 */
public interface ISimpleMapConfig<KEY> {
	/**
	 * 得到key
	 * @return
	 */
	public KEY getKey();
}
