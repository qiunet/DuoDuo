package org.qiunet.utils.math;

/**
 * Created by qiunet.
 * 17/11/29
 */
public interface PollChooserFactory {

	/**
	 * Returns a new {@link PollChooser}.
	 */
	<Element> PollChooser<Element> newChooser(Element[] executors);

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
