package org.qiunet.data.support;

import org.qiunet.data.core.entity.IEntity;

public interface IDataSupport<Do extends IEntity, Bo> {

	Bo insert(Do aDo);

	void delete(Bo bo);

	void update(Bo bo);

	Bo convertBo(Do aDo);
}
