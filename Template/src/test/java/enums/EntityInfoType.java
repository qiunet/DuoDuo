package enums;

import org.qiunet.data.core.support.entityInfo.*;
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
