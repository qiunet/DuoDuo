package org.qiunet.utils.math;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiunet.
 * 17/11/29
 */
public class DefaultPollChooserFactory implements PollChooserFactory {
	public static final DefaultPollChooserFactory DEFAULT = new DefaultPollChooserFactory();
	private DefaultPollChooserFactory(){}
	@Override
	public <Element> PollChooser<Element> newChooser(Element[] executors) {
		if (MathUtil.isPowerOfTwo(executors.length)) {
			return new PowerOfTwoPollChooser(executors);
		} else {
			return new GenericPollChooser(executors);
		}
	}

	private static final class PowerOfTwoPollChooser<Element> implements PollChooser<Element> {
		private final AtomicInteger idx = new AtomicInteger();
		private final Element[] elements;

		PowerOfTwoPollChooser(Element[] elements) {
			this.elements = elements;
		}

		@Override
		public Element next() {
			return elements[idx.getAndIncrement() & elements.length - 1];
		}
	}

	private static final class GenericPollChooser<Element> implements PollChooser<Element> {
		private final AtomicInteger idx = new AtomicInteger();
		private final Element[] elements;

		GenericPollChooser(Element[] elements) {
			this.elements = elements;
		}

		@Override
		public Element next() {
			return elements[Math.abs(idx.getAndIncrement() % elements.length)];
		}
	}
}
