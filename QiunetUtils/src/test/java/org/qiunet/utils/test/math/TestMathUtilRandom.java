package org.qiunet.utils.test.math;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author qiunet
 *         Created on 16/11/6 13:54.
 */
@RunWith(Parameterized.class)
public class TestMathUtilRandom {
	private final int randStart;
	private final int randEnd;

	public TestMathUtilRandom(int randStart, int randEnd) {
		this.randStart = randStart;
		this.randEnd = randEnd;
	}
	@Parameterized.Parameters
	public static Collection params(){
		List<Object[]> ret = new ArrayList<>();
		for (int i = 1 ; i <= 20; i++) {
			ret.add(new Object[]{i, i*2});
		}
		return ret;
	}

	@Test
	public void testRandom(){
		int rand = MathUtil.random(randStart, randEnd);
		LoggerType.DUODUO.info(randStart +", "+ randEnd+ ", "+rand);
		Assert.assertTrue(rand >= randStart && rand < randEnd);
	}
}
