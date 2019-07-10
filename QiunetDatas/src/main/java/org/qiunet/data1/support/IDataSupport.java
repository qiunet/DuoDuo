package org.qiunet.data1.support;

import org.qiunet.data1.core.entity.IEntity;

public interface IDataSupport<Po extends IEntity> {

	int insert(Po po);

	int delete(Po po);

	int update(Po po);
}
