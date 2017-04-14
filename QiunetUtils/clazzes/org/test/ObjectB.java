package org.test;
/**
 * 用来测试加载类是否正确
 * @author qiunet
 *         Created on 17/1/24 10:27.
 */
public class ObjectB {
	public String createString(){
		return new String("TestB");
	}
	@Override
	public String toString() {
		return getClass().getClassLoader().getClass().getSimpleName();
	}
}
