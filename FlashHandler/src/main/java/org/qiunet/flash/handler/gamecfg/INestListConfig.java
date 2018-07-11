package org.qiunet.flash.handler.gamecfg;

/**
 *
 *  一个key  对应一个 cfg list的结构
 *  结构: Map<key, List<cfg>>
 * Created by qiunet.
 * 17/6/19
 */
public interface INestListConfig<KEY> {
	/***
	 *
	 * @return
	 */
	public KEY getKey();
}
