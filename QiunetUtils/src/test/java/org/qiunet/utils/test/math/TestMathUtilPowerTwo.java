package org.qiunet.utils.test.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.qiunet.utils.math.MathUtil;

/**
 * @author qiunet
 *         Created on 16/11/6 13:54.
 */
public class TestMathUtilPowerTwo {

	@ParameterizedTest
	@DisplayName("是否是2的指数")
	@ValueSource(ints = {0, 1, 2, 4, 8, 16, 32, 64, 128, 1024})
	public void powerTwoTrue(int val){
		Assertions.assertTrue(MathUtil.isPowerOfTwo(val));
	}

	@ParameterizedTest
	@DisplayName("是否不是2的指数")
	@ValueSource(ints = {5, 3, 1023})
	public void powerTwoFalse(int val){
		Assertions.assertFalse(MathUtil.isPowerOfTwo(val));
	}
}
