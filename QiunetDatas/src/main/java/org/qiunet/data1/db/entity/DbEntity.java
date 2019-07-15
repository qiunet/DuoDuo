package org.qiunet.data1.db.entity;

import org.qiunet.data1.support.DataSupportMapping;
import org.qiunet.data1.support.IDataSupport;

public abstract class DbEntity<Key> implements IDbEntity<Key> {
	private IDataSupport<DbEntity> dataSupport;

	public DbEntity() {
		this.dataSupport = DataSupportMapping.getMapping(getClass());
	}

	@Override
	public void insert() {
		this.dataSupport.insert(this);
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
