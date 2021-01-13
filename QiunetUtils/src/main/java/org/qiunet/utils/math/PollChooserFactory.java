package org.qiunet.utils.math;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by qiunet.
 * 17/11/29
 */
public interface PollChooserFactory {
	/**
	 * default pool chooser factory
	 */
	DefaultPollChooserFactory DEFAULT = new DefaultPollChooserFactory();
	/**
	 * Returns a new {@link PollChooser}.
	 */
	default <Element> PollChooser<Element> newChooser(Element[] executors) {
		return newChooser(Lists.newArrayList(executors));
	}
	/**
	 * Returns a new {@link PollChooser}.
	 * 使用list 构造的, list在外部增加元素. 里面也会增加.
	 */
	<Element> PollChooser<Element> newChooser(List<Element> executors);

	/**
	 * Chooses the next {@link PollChooser} to use.
	 */
	interface PollChooser<Element> {

		/**
		 * Returns the new {@link Element} to use.
		 */
		Element next();
	}
}
