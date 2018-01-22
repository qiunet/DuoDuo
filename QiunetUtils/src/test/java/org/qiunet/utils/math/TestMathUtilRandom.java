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
public class TestMathUtilRandom {
	private int randStart;
	private int randEnd;

	private int val;
	public TestMathUtilRandom(int randStart, int randEnd, int val) {
		this.randStart = randStart;
		this.randEnd = randEnd;
		this.val = val;
	}
	@Parameterized.Parameters
	public static Collection params(){
		List<Object[]> ret = new ArrayList<>();
		for (int i = 0 ; i < 20; i++) {
			ret.add(new Object[]{i, i+1, i});
		}
		return ret;
	}

	@Test
	public void testRandom(){
		int rand = MathUtil.random(randStart, randEnd);
		Assert.assertEquals(val, rand);
	}
}
