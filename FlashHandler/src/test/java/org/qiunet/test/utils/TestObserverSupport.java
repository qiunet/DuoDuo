package org.qiunet.test.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.CommMessageHandler;
import org.qiunet.flash.handler.common.observer.IObserver;
import org.qiunet.flash.handler.common.observer.Observer;
import org.qiunet.flash.handler.common.observer.ObserverSupport;

import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @author qiunet
 * 2022/9/27 11:21
 */
public class TestObserverSupport {
	private static final CommMessageHandler handler = new CommMessageHandler();

	private static final ObserverSupport<CommMessageHandler> observerSupport = new ObserverSupport<>(handler);

	public interface ITest extends IObserver {
		void fire();
	}

	@Test
	public void test() {
		AtomicInteger counter = new AtomicInteger();
		Observer<ITest> observer = observerSupport.attach(ITest.class, counter::incrementAndGet);

		observerSupport.syncFire(ITest.class, ITest::fire);
		Assertions.assertEquals(1, counter.get());

		observer.remove();

		observerSupport.syncFire(ITest.class, ITest::fire);
		Assertions.assertEquals(1, counter.get());
	}
}
