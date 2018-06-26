package org.qiunet.utils.groovy;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/26 11:49
 **/
public class TestGroovy {
	@Test
	public void testScript(){
		// 文本在app中由设定xd 提供
		IGroovyRun<Integer> cal = new GroovyRun("return num * 10;");
		Map<String , Object> params = new HashMap<>();
		params.put("num", 5);

		Assert.assertEquals(Integer.valueOf(50), cal.run(params));
	}
	@Test
	public void testThreadSafe() throws InterruptedException {
		// 文本在app中由设定xd 提供
		IGroovyRun<Integer> cal = new GroovyRun("return num * 10;");
		CountDownLatch latch = new CountDownLatch(100);
		for (int i = 0; i < 10; i++) {
			new Thread(
				() -> {
					for (int i1 = 0; i1 < 10; i1++) {
						Map<String , Object> params = new HashMap<>();
						params.put("num", i1);
						int ret = cal.run(params);
						Assert.assertEquals(i1 * 10 , ret);
						latch.countDown();
					}
				}
			).start();
		}
		latch.await();
	}
	@Test
	public void TestMethod(){
		URL url = getClass().getResource("/script/testMappingJavaObject.groovy");
		IGroovyRun<Void> groovyRun = new GroovyRun(url);
		for (int i = 10; i < 20; i++) {
			UserPo userPo = new UserPo(i);
			groovyRun.invokeMethod("changeUserPo", new Object[]{userPo, i / 2});

			Assert.assertEquals(userPo.getAge(), i/2);
			if (userPo.getId() < 15) {
				Assert.assertEquals(userPo.getName(), "qiunet "+userPo.getId());
			}else {
				Assert.assertEquals(userPo.getName(), "qiunets "+userPo.getId());
			}
		}
	}
}
