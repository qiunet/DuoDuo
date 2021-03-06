package org.qiunet.listener.test.event;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.listener.event.EventManager;
import org.qiunet.utils.scanner.ClassScanner;

import java.util.concurrent.atomic.AtomicInteger;

public class TestListener {
	public static final AtomicInteger loginEventCount = new AtomicInteger();
	public static final AtomicInteger levelUpEventCount = new AtomicInteger();
	@BeforeClass
	public static void init() throws Exception {
		ClassScanner.getInstance().scanner();
	}

	public static final int uid = 100000;
	public static final int oldLevel = 10;
	public static final int newLevel = 12;

	@Test
	public void listener(){
		new LoginEventData(uid).fireEventHandler();
		EventManager.fireEventHandler(new LevelUpEventData(uid, oldLevel, newLevel));

		Assert.assertEquals(3, loginEventCount.get());
		Assert.assertEquals(1, levelUpEventCount.get());
	}
}
