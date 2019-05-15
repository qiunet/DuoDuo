package org.qiunet.utils.classLoader;

import org.junit.Test;

public class TestHotSwap {
	@Test
	public void testHotSwap(){
		String path = getClass().getResource("/").getPath();
		ChangeClass changeClass = new ChangeClass();
		changeClass.show();

		ClassHotSwap.hotSwap(path);
		changeClass.show();
		//
		new ChangeClass().show();
	}
}
