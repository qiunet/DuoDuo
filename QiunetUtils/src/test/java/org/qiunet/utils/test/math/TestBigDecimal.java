package org.qiunet.utils.test.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.qiunet.utils.math.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * @author zhengjian
 * Created on 21/1/19 19:54.
 */
public class TestBigDecimal {

	private static Stream<Arguments> params() {
		return Stream.of(
				Arguments.arguments("1", new double[]{0.4, 20.0}, new BigDecimal("8.0"))
		);
	}

	@ParameterizedTest
	@MethodSource("params")
	public void testRandom(String b, double[] param, BigDecimal result) {
		Assertions.assertTrue(BigDecimalUtil.equalsTo(BigDecimalUtil.mul(b, param), result));
	}

}
