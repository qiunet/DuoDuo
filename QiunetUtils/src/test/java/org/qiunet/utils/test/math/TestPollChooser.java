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
		Assertions.assertEquals(0, (int) chooser.next());
		Assertions.assertEquals(1, (int) chooser.next());
		Assertions.assertEquals(2, (int) chooser.next());
		Assertions.assertEquals(3, (int) chooser.next());
		Assertions.assertEquals(0, (int) chooser.next());
        Assertions.assertEquals(1, (int) chooser.next());

		nums1 = new Integer[]{0, 1, 2};
		chooser = DefaultPollChooserFactory.DEFAULT.newChooser(nums1);
        Assertions.assertEquals(0, (int) chooser.next());
        Assertions.assertEquals(1, (int) chooser.next());
        Assertions.assertEquals(2, (int) chooser.next());
        Assertions.assertEquals(0, (int) chooser.next());
        Assertions.assertEquals(1, (int) chooser.next());

	}
}
