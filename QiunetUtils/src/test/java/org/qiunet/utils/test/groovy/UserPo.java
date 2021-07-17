package org.qiunet.utils.test.groovy;

/**
 * Created by qiunet.
 * 17/9/4
 */
public class UserPo {
	private int id;
	private int age;
	private String name;

	public UserPo(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
