package org.qiunet.utils.listener;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.classScanner.ClassScanner;

import java.util.concurrent.atomic.AtomicInteger;

public class TestListener {
	public static AtomicInteger test1Count = new AtomicInteger();
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void listener(){
		new Test1EventData().fireEventHandler();

		Assert.assertEquals(1, test1Count.get());
	}
}
