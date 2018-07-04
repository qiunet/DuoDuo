package org.qiunet.project.init;

/**
 * Created by qiunet on 4/8/17.
 */
public final class ProjectInitCreator {
	private ProjectInitCreator(){}

	/***
	 * 根据模板创建玩家的对象
	 */
	public static void create() {
		create(new DefaultProjectInitConfig());
	}
	/***
	 * 根据模板创建玩家的对象
	 */
	public static void create(IProjectInitConfig config) {
		new ProjectInitData(config).create();
	}
}
