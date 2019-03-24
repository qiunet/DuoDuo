package org.qiunet.project.init.enums;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.core.support.entityInfo.IEntityListInfo;

/**
 * @author qiunet
 *         Created on 17/2/15 19:40.
 */
public enum EntityInfoType {
	IEntityListInfo(IEntityListInfo.class),
	IEntityInfo(IEntityInfo.class),
	;
	private Class clazz;
	private EntityInfoType(Class clazz) {
		this.clazz = clazz;
	}

	public Class getClazz() {
		return clazz;
	}
}
