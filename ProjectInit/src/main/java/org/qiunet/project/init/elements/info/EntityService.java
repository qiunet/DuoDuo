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

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
