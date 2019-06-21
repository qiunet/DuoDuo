package org.qiunet.utils.listener;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.classScanner.ClassScanner;

import java.util.concurrent.atomic.AtomicInteger;

public class TestListener {
	public static AtomicInteger test1Count = new AtomicInteger();
	public static AtomicInteger test2Count = new AtomicInteger();
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void listener(){
		ListenerManager.getInstance().fireEventHandler(new Test1EventData());
		ListenerManager.getInstance().fireEventHandler(new Test2EventData());

		Assert.assertEquals(2, test1Count.get());
		Assert.assertEquals(1, test2Count.get());
	}
}
