package org.qiunet.utils.math;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
	private String b;
	private double[] param;
	private BigDecimal result;

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
		Assert.assertEquals(BigDecimalUtil.equlsTo(BigDecimalUtil.mul(b, param), result), true);
	}

}
