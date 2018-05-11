package org.qiunet.project.init.elements.info;

import org.qiunet.project.init.elements.entity.Entity;

/**
 * Created by qiunet.
 * 17/8/8
 */
public class EntityVo {
	private String packagePath;
	private String poName;
	private String voName;

	public EntityVo(Entity entity, String voName) {
		this.voName = voName;
		this.poName = entity.getName();
		this.packagePath = entity.getPackagePath();
	}

	public String getPackagePath() {
		return packagePath;
	}

	public String getPoName() {
		return poName;
	}

	public String getVoName() {
		return voName;
	}
}
