package org.qiunet.data.support;

import org.qiunet.data.core.entity.IEntity;

/***
 * Bo的外部构造器
 * @param <Do>
 * @param <Bo>
 */
@FunctionalInterface
public interface BoSupplier<Do extends IEntity<?>, Bo extends IEntityBo<Do>> {
	/**
	 * Do得到一个Bo
	 * @param aDo
	 * @return
	 */
	Bo get(Do aDo);
}
