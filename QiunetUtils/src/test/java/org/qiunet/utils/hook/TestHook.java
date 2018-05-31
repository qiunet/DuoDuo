package org.qiunet.utils.hook;

import org.junit.Test;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/31 12:18
 **/
public class TestHook {

	@Test
	public void testHook(){
		ShutdownHookThread.getInstance().addShutdownHook(() -> {
			System.out.println("===============SHUTDOWN1 ==================");
		});

		ShutdownHookThread.getInstance().addShutdownHook(() -> {
			System.out.println("===============SHUTDOWN2 ==================");
		});
	}
}
