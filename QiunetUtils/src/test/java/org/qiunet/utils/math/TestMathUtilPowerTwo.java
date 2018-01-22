package org.qiunet.utils.math;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author qiunet
 *         Created on 16/11/6 13:54.
 */
@RunWith(Parameterized.class)
public class TestMathUtilPowerTwo {
	private int val;
	private boolean eq;
	public TestMathUtilPowerTwo(int val, boolean eq) {
		this.eq = eq;
		this.val = val;
	}
	@Parameterized.Parameters
	public static Collection params(){
		List<Object[]> ret = new ArrayList<>();
		ret.add(new Object[]{0, true});
		ret.add(new Object[]{1, true});
		ret.add(new Object[]{2, true});
		ret.add(new Object[]{4, true});
		ret.add(new Object[]{8, true});
		ret.add(new Object[]{16, true});
		ret.add(new Object[]{32, true});
		ret.add(new Object[]{64, true});
		ret.add(new Object[]{128, true});
		ret.add(new Object[]{1024, true});

		ret.add(new Object[]{3, false});
		ret.add(new Object[]{5, false});
		ret.add(new Object[]{6, false});
		ret.add(new Object[]{1023, false});
		return ret;
	}

	@Test
	public void testRandom(){
		Assert.assertEquals(MathUtil.isPowerOfTwo(val), eq);
	}
}
