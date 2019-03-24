package org.qiunet.project.init.elements.info;

import org.qiunet.project.init.elements.entity.Entity;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/3 23:49
 **/
public class EntityService {

	private String name;
	private Entity entity;
	private String packagePath;
	private EntityInfo entityInfo;
	public EntityService(String name, Entity entity, EntityInfo entityInfo, String packagePath) {
		this.name = name;
		this.entity = entity;
		this.entityInfo = entityInfo;
		this.packagePath = packagePath;
	}

	public EntityInfo getEntityInfo() {
		return entityInfo;
	}

	public String getPoName(){
		return entityInfo.getPoref();
	}

	public String getVoName(){
		return entityInfo.getVo();
	}

	public String getPoPackage(){
		return entity.getPackagePath() +"."+ getPoName();
	}

	public String getVoPackage(){
		return entity.getPackagePath() +"."+ getVoName();
	}

	public String getDbInfoKeyName() {
		return entity.getDbInfoKey();
	}

	public String getDbInfoKeyType() {
		return entity.getDbInfoKeyType();
	}

	public String getSubKeyName() {
		return entity.getSubKey();
	}

	public String getSubKeyType() {
		return entity.getSubKeyType();
	}

	public String getInfoClassPackage(){
		return entityInfo.getInfoPackagePath()+"."+getInfoClassName();
	}

	public String getInfoClassName(){
		return entityInfo.getName();
	}

	public Entity getEntity() {
		return entity;
	}

	public String getName() {
		return name;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public String getEntityDataSupportPackage(){
		return entity.getType().getDataSupportClass().getName();
	}

	public String getEntityDataSupportClass(){
		return entity.getType().getDataSupportClass().getSimpleName();
	}

	public boolean isList(){
		return entity.isRedisList();
	}
}
