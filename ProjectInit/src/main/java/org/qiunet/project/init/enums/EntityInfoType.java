package org.qiunet.project.init.enums;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.core.support.entityInfo.IEntityListInfo;
import org.qiunet.data.core.support.entityInfo.IPlatformEntityInfo;
import org.qiunet.data.core.support.entityInfo.IPlatformEntityListInfo;

/**
 * @author qiunet
 *         Created on 17/2/15 19:40.
 */
public enum EntityInfoType {
	IPlatformEntityListInfo(IPlatformEntityListInfo.class),
	IPlatformEntityInfo(IPlatformEntityInfo.class),
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
