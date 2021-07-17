package org.qiunet.utils.test.json;

public class User {

	private int userId;

	private String name;

	private int age;

	public int getAge() {
		return age;
	}

	public int getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
