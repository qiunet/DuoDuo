package org.qiunet.data1.db.entity;

import org.qiunet.data1.support.DataSupportMapping;
import org.qiunet.data1.support.IDataSupport;
import org.qiunet.data1.support.IEntityVo;

public abstract class DbEntity<Key, Vo extends IEntityVo> implements IDbEntity<Key, Vo> {
	private IDataSupport<DbEntity, Vo> dataSupport;

	public DbEntity() {
		this.dataSupport = DataSupportMapping.getMapping(getClass());
	}

	@Override
	public Vo insert() {
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
