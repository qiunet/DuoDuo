package org.qiunet.utils.test.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.math.DefaultPollChooserFactory;
import org.qiunet.utils.math.PollChooserFactory;

/**
 * Created by qiunet.
 * 17/11/29
 */
public class TestPollChooser {
	@Test
	public void testPollChooser(){
		Integer [] nums1 = new Integer[]{0, 1, 2, 3};
		PollChooserFactory.PollChooser<Integer> chooser = PollChooserFactory.DEFAULT.newChooser(nums1);
		Assertions.assertTrue(0 == chooser.next());
		Assertions.assertTrue(1 == chooser.next());
		Assertions.assertTrue(2 == chooser.next());
		Assertions.assertTrue(3 == chooser.next());
		Assertions.assertTrue(0 == chooser.next());
		Assertions.assertTrue(1 == chooser.next());

		nums1 = new Integer[]{0, 1, 2};
		chooser = DefaultPollChooserFactory.DEFAULT.newChooser(nums1);
		Assertions.assertTrue(0 == chooser.next());
		Assertions.assertTrue(1 == chooser.next());
		Assertions.assertTrue(2 == chooser.next());
		Assertions.assertTrue(0 == chooser.next());
		Assertions.assertTrue(1 == chooser.next());

	}
}
