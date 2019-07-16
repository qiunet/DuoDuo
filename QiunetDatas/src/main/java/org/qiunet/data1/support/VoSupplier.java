package org.qiunet.data1.support;

import org.qiunet.data1.core.entity.IEntity;

/***
 * Vo的外部构造器
 * @param <Po>
 * @param <Vo>
 */
@FunctionalInterface
public interface VoSupplier<Po extends IEntity, Vo> {
	/**
	 * po得到一个Vo
	 * @param po
	 * @return
	 */
	Vo get(Po po);
}
