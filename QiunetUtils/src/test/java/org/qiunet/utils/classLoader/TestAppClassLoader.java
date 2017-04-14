package org.qiunet.utils.classLoader;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

import java.lang.reflect.Method;

/**
 * @author qiunet
 *         Created on 17/1/24 10:25.
 */
public class TestAppClassLoader extends BaseTest{
	@Test
	public void testClassLoad(){
		String [] paths = {GameAppClassLoader.class.getResource("/lib/").getPath(), System.getProperty("user.dir")+"/clazzes/"};
		GameAppClassLoader loader = new GameAppClassLoader(paths, new String[]{"org.test"});
		boolean exception = false;
		try {
			
			Class objectClass = loader.loadClass("java.lang.String");
			Assert.assertTrue(objectClass.getClassLoader() == null || ! "GameAppClassLoader".equals(objectClass.getClassLoader().getClass().getSimpleName()));
			
			Class c1 = loader.loadClass("org.test.ObjectA");
			Object object1 = c1.newInstance();
			Assert.assertEquals(object1.toString(), "GameAppClassLoader");
			
			Method method1 = object1.getClass().getMethod("createObject");
			Object object2 = method1.invoke(object1);
			Assert.assertEquals("org.test.ObjectB", object2.getClass().getName());
			Assert.assertEquals(object2.toString() , "GameAppClassLoader");
			
			Class c2 = loader.loadClass("javaz.utils.date.DateUtil");
			Object object3 = c2.newInstance();
			Assert.assertEquals(object3.getClass().getClassLoader().getClass().getSimpleName(), "GameAppClassLoader");
			
			Method method2 = object1.getClass().getMethod("createString");
			String string = (String) method2.invoke(object1);
			Assert.assertEquals("TestA", string);
		} catch (Exception e) {
			exception = true;
			e.printStackTrace();
		} finally {
			Assert.assertTrue(! exception);
		}
	}
}
