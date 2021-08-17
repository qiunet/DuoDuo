package org.qiunet.function.test.ai.enums;

/***
 *
 * 敌人
 *
 * qiunet
 * 2021/8/17 10:17
 **/
public enum Enemy {

	GOBLIN("哥布林"),

	OMA("半兽人"),
	;
	private final String name;

	Enemy(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
