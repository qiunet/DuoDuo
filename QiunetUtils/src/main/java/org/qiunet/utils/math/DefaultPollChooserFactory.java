package org.qiunet.utils.math;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiunet.
 * 17/11/29
 */
public class DefaultPollChooserFactory implements PollChooserFactory {
	public static final DefaultPollChooserFactory DEFAULT = new DefaultPollChooserFactory();
	private DefaultPollChooserFactory(){}
	@Override
	public <Element> PollChooser<Element> newChooser(List<Element> executors) {
		ArrayList<Element> list = null;
		if (executors instanceof ArrayList) {
			list = ((ArrayList<Element>) executors);
		}else {
			list = Lists.newArrayList(executors);
		}
		if (MathUtil.isPowerOfTwo(executors.size())) {
			return new PowerOfTwoPollChooser(list);
		} else {
			return new GenericPollChooser(list);
		}
	}

	private static final class PowerOfTwoPollChooser<Element> implements PollChooser<Element> {
		private final AtomicInteger idx = new AtomicInteger();
		private final ArrayList<Element> elements;

		PowerOfTwoPollChooser(ArrayList<Element> elements) {
			this.elements = elements;
		}

		@Override
		public Element next() {
			return elements.get(idx.getAndIncrement() & elements.size() - 1);
		}
	}

	private static final class GenericPollChooser<Element> implements PollChooser<Element> {
		private final AtomicInteger idx = new AtomicInteger();
		private final ArrayList<Element> elements;

		GenericPollChooser(ArrayList<Element> elements) {
			this.elements = elements;
		}

		@Override
		public Element next() {
			return elements.get(Math.abs(idx.getAndIncrement() % elements.size()));
		}
	}
}
