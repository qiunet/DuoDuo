package org.qiunet.utils.test.listener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.listener.event.EventManager;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.concurrent.atomic.AtomicInteger;

public class TestListener {
	public static final AtomicInteger loginEventCount = new AtomicInteger();
	public static final AtomicInteger levelUpEventCount = new AtomicInteger();
	@BeforeAll
	public static void init() throws Exception {
		ClassScanner.getInstance(ScannerType.EVENT).scanner();
	}

	public static final int uid = 100000;
	public static final int oldLevel = 10;
	public static final int newLevel = 12;

	@Test
	public void listener(){
		new LoginEvent(uid).fireEventHandler();
		EventManager.fireEventHandler(new LevelUpEvent(uid, oldLevel, newLevel));

		Assertions.assertEquals(3, loginEventCount.get());
		Assertions.assertEquals(1, levelUpEventCount.get());
	}
}
