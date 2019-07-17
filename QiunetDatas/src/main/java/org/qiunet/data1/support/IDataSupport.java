package org.qiunet.data1.support;

import org.qiunet.data1.core.entity.IEntity;

public interface IDataSupport<Po extends IEntity, Vo> {

	Vo insert(Po po);

	void delete(Po po);

	void update(Po po);
}
