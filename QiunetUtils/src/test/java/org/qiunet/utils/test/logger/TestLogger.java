package org.qiunet.utils.test.logger;

import org.junit.jupiter.api.Test;
import org.qiunet.utils.test.base.BaseTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author qiunet
 *         Created on 17/1/6 12:00.
 */
public class TestLogger extends BaseTest{

	@Test
	public void loggerLoop() throws InterruptedException {
		for (int i = 0 ; i < 5; i++) {
			String msg = i+"我们\n是中国人abc\ndccc";
			if (i < 3)
				logger.info(msg);
			else if (i == 3)
				logger.error(msg);
			else
				logger.debug(msg);
		}
	}
//	@Test
//	public void loggerException() throws InterruptedException {
//		logger.info("我们\n是中国人abc\ndccc", new NullPointerException("Test"));
//	}
	@Test
	public void testInvoke() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class clazz = Class.forName("org.qiunet.utils.logger.LoggerType");
		Field field = clazz.getField("DUODUO_HOTSWAP");
		Object logger = field.get(null);
		Method method = clazz.getMethod("error", String.class);
		method.invoke(logger, "哈哈哈");
	}
}
