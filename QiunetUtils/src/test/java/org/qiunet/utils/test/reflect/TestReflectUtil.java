package org.qiunet.utils.test.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.test.base.BaseTest;

import java.lang.reflect.Field;
import java.util.List;

/***
 * 测试 ReflectUtil
 *
 * qiunet
 * 2021/6/30 22:58
 **/
public class TestReflectUtil extends BaseTest {

	public interface ITest1 { }
	public interface ITest2 { }

	public static class Test1 implements ITest1 {}
	public static class Test2 implements ITest2 {}

	private List<ITest1> list;

	public static abstract class BaseReflectTestClass<T extends ITest1, F extends ITest2> {
		abstract T getTest1();
		abstract T getTest2();
	}

	public static class ReflectTestClass extends BaseReflectTestClass<Test1, Test2> {

		@Override
		Test1 getTest1() {
			return null;
		}

		@Override
		Test1 getTest2() {
			return null;
		}
	}

	public static class NoParamClass {

	}
	@Test
	public void test(){
		Class<?> type0 = ReflectUtil.findGenericParameterizedType(ReflectTestClass.class, ITest2.class::isAssignableFrom);
		Assertions.assertEquals(type0, Test2.class);

		Class<?> type1 = ReflectUtil.findGenericParameterizedType(NoParamClass.class, clz -> true);
		Assertions.assertNull(type1);
	}

	@Test
	public void testListGenericType() throws NoSuchFieldException {
		Field field = TestReflectUtil.class.getDeclaredField("list");
		Class<?> type = ReflectUtil.getListGenericParameterizedType(field);
		Assertions.assertEquals(type, ITest1.class);
	}
}
