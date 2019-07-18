package org.qiunet.data1.support;

import org.qiunet.data1.core.entity.IEntity;

public interface IDataSupport<Do extends IEntity, Bo> {

	Bo insert(Do aDo);

	void delete(Do aDo);

	void update(Do aDo);
}
