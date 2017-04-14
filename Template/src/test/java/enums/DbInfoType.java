package enums;

import org.qiunet.data.db.support.info.*;
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
	private DbInfoType(Class clazz) {
		this.clazz = clazz;
	}
	
	public Class getClazz(){
		return clazz;
	}
}
