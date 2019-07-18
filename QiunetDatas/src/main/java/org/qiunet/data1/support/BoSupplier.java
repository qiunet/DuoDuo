package org.qiunet.data1.support;

import org.qiunet.data1.core.entity.IEntity;

/***
 * Bo的外部构造器
 * @param <Po>
 * @param <Bo>
 */
@FunctionalInterface
public interface BoSupplier<Po extends IEntity, Bo extends IEntityBo<Po>> {
	/**
	 * po得到一个Bo
	 * @param po
	 * @return
	 */
	Bo get(Po po);
}
