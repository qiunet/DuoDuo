package org.qiunet.data1.db.entity;

import org.qiunet.data1.support.IDataSupport;

public abstract class DbEntity<Key> implements IDbEntity<Key> {
	private IDataSupport<DbEntity> dataSupport;

	@Override
	public void setDataSupport(IDataSupport dataSupport) {
		this.dataSupport = dataSupport;
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
