package reflect;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;
import org.qiunet.utils.reflect.ReflectUtil;

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

	@Test
	public void test(){
		Class<?> type = ReflectUtil.findGenericParameterizedType(ReflectTestClass.class, ITest2.class::isAssignableFrom);
		Assert.assertEquals(type, Test2.class);
	}
}
