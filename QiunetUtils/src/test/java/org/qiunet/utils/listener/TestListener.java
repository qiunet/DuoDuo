package org.qiunet.utils.listener;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.classScanner.ClassScanner;

import java.util.concurrent.atomic.AtomicInteger;

public class TestListener {
	public static final AtomicInteger loginEventCount = new AtomicInteger();
	public static final AtomicInteger levelUpEventCount = new AtomicInteger();
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	public static final int uid = 100000;
	public static final int oldLevel = 10;
	public static final int newLevel = 12;

	@Test
	public void listener(){
		new LoginEventData(uid).fireEventHandler();
		ListenerManager.fireEventHandler(new LevelUpEventData(uid, oldLevel, newLevel));

		Assert.assertEquals(3, loginEventCount.get());
		Assert.assertEquals(1, levelUpEventCount.get());
	}
}
