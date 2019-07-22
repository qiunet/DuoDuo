package org.qiunet.data1.db.entity;

import org.qiunet.data1.support.DataSupportMapping;
import org.qiunet.data1.support.IDataSupport;
import org.qiunet.data1.support.IEntityBo;

public abstract class DbEntity<Key, Bo extends IEntityBo> implements IDbEntity<Key, Bo> {
	private IDataSupport<DbEntity, Bo> dataSupport;

	public DbEntity() {
		this.dataSupport = DataSupportMapping.getMapping(getClass());
	}

	@Override
	public Bo insert() {
		return this.dataSupport.insert(this);
	}

	@Override
	public void update() {
		this.dataSupport.update(this);
	}

	@Override
	public void delete() {
		this.dataSupport.delete(this);
	}
}
