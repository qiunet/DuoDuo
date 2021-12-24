package org.qiunet.utils.test.math;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.qiunet.utils.math.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zhengjian
 * Created on 21/1/19 19:54.
 */
@RunWith(Parameterized.class)
public class TestBigDecimal {
	private final String b;
	private final double[] param;
	private final BigDecimal result;

	public TestBigDecimal(String b, double[] param, BigDecimal result) {
		this.b = b;
		this.param = param;
		this.result = result;
	}

	@Parameterized.Parameters
	public static Collection params() {
		List<Object[]> ret = new ArrayList<>();
		ret.add(new Object[]{"1", new double[]{0.4, 20.0}, new BigDecimal("8.0")});
		return ret;
	}

	@Test
	public void testRandom() {
		Assert.assertTrue(BigDecimalUtil.equalsTo(BigDecimalUtil.mul(b, param), result));
	}

}
