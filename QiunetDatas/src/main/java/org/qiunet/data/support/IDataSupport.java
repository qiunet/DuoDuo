package org.qiunet.data.support;

import org.qiunet.data.core.entity.IEntity;

public interface IDataSupport<Do extends IEntity, Bo> {

	Bo insert(Do aDo);

	void delete(Do aDo);

	void update(Do aDo);

	Bo convertBo(Do aDo);
}
