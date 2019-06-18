package org.qiunet.utils.listener;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.classScanner.ClassScanner;

public class TestListener {
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void listener(){
		ListenerManager.getInstance().fireEventHandler(new Test1EventData());
		ListenerManager.getInstance().fireEventHandler(new Test2EventData());
	}
}
