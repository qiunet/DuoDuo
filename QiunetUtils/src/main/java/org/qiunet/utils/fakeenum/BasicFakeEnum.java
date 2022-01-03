package org.qiunet.utils.fakeenum;

import java.io.Serializable;

/***
 * 假枚举需要继承的基类
 *
 * 会继承该类 并在有 {@link EnumClass}的类使用
 * 按照字段名注入{@link BasicFakeEnum#name()}
 *
 * @author qiunet
 * 2022/1/3 08:38
 */
public abstract class BasicFakeEnum<E extends BasicFakeEnum<E>> implements Comparable<E>, Serializable {
	/**
	 * 枚举名
	 */
	private String name;
	/**
	 * 序列号
	 */
	private int ordinal;


	public final String name(){
		return name;
	}

	public final int ordinal() {
		return ordinal;
	}

	@Override
	public boolean equals(Object o) {
		return this == o;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	public static <T extends BasicFakeEnum<T>> T valueOf(Class<T> enumType,
														 String name) {
		return valueOf(enumType.getName(), name);
	}
	/**
	 * 根据指定的enum type 和name 返回对应的枚举值
	 * @param enumType 枚举的 className
	 * @param name
	 * @param <T>
	 * @return
	 */
	public static <T extends BasicFakeEnum<T>> T valueOf(String enumType,
												String name) {
		if (name == null)
			throw new NullPointerException("Name is null");

		T result = FakeEnumManager.valueOfEnum(enumType, name);
		if (result != null)
			return result;

		throw new IllegalArgumentException(
				"No enum constant " + enumType + "." + name);
	}
	/**
	 * Returns the Class object corresponding to this enum constant's
	 * enum type.  Two enum constants e1 and  e2 are of the
	 * same enum type if and only if
	 *   e1.getDeclaringClass() == e2.getDeclaringClass().
	 * (The value returned by this method may differ from the one returned
	 * by the {@link Object#getClass} method for enum constants with
	 * constant-specific class bodies.)
	 *
	 * @return the Class object corresponding to this enum constant's
	 *     enum type
	 */
	@SuppressWarnings("unchecked")
	public final Class<E> getDeclaringClass() {
		Class<?> clazz = getClass();
		Class<?> zuper = clazz.getSuperclass();
		return (zuper == Enum.class) ? (Class<E>)clazz : (Class<E>)zuper;
	}

	protected final Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	public int compareTo(E o) {
		BasicFakeEnum<?> other = (BasicFakeEnum<?>)o;
		BasicFakeEnum<E> self = this;
		if (self.getClass() != other.getClass() && // optimization
				self.getDeclaringClass() != other.getDeclaringClass())
			throw new ClassCastException();
		return self.ordinal - other.ordinal;
	}
}
