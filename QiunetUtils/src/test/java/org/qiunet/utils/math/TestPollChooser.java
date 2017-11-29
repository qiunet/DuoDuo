package org.qiunet.utils.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by qiunet.
 * 17/11/29
 */
public class TestPollChooser {
	@Test
	public void testPollChooser(){
		Integer [] nums1 = new Integer[]{0, 1, 2, 3};
		PollChooserFactory.PollChooser<Integer> chooser = DefaultPollChooserFactory.DEFAULT.newChooser(nums1);
		Assert.assertTrue(0 == chooser.next());
		Assert.assertTrue(1 == chooser.next());
		Assert.assertTrue(2 == chooser.next());
		Assert.assertTrue(3 == chooser.next());
		Assert.assertTrue(0 == chooser.next());
		Assert.assertTrue(1 == chooser.next());

		nums1 = new Integer[]{0, 1, 2};
		chooser = DefaultPollChooserFactory.DEFAULT.newChooser(nums1);
		Assert.assertTrue(0 == chooser.next());
		Assert.assertTrue(1 == chooser.next());
		Assert.assertTrue(2 == chooser.next());
		Assert.assertTrue(0 == chooser.next());
		Assert.assertTrue(1 == chooser.next());

	}
}
