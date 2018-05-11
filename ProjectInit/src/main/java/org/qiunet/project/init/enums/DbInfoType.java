package org.qiunet.project.init.enums;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.db.support.info.IPlatformEntityDbInfo;
import org.qiunet.data.db.support.info.IPlatformEntityListDbInfo;

/**
 * @author qiunet
 *         Created on 17/2/16 16:25.
 */
public enum  DbInfoType {
	IEntityDbInfo(IEntityDbInfo.class),
	IEntityListDbInfo(IEntityListDbInfo.class),
	IPlatformEntityDbInfo(IPlatformEntityDbInfo.class),
	IPlatformEntityListDbInfo(IPlatformEntityListDbInfo.class),
	;
	private Class clazz;
	DbInfoType(Class clazz) {
		this.clazz = clazz;
	}

	public Class getClazz(){
		return clazz;
	}
}
