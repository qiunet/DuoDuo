package org.qiunet.utils.test.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.qiunet.utils.math.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author qiunet
 *         Created on 16/11/6 13:54.
 */
public class TestMathUtilPowerTwo {

	private static Stream<Arguments> params(){
		List<Arguments> ret = new ArrayList<>();
		ret.add(Arguments.arguments(0, true));
		ret.add(Arguments.arguments(1, true));
		ret.add(Arguments.arguments(2, true));
		ret.add(Arguments.arguments(4, true));
		ret.add(Arguments.arguments(8, true));
		ret.add(Arguments.arguments(16, true));
		ret.add(Arguments.arguments(32, true));
		ret.add(Arguments.arguments(64, true));
		ret.add(Arguments.arguments(128, true));
		ret.add(Arguments.arguments(1024, true));

		ret.add(Arguments.arguments(3, false));
		ret.add(Arguments.arguments(5, false));
		ret.add(Arguments.arguments(6, false));
		ret.add(Arguments.arguments(1023, false));
		return ret.stream();
	}

	@ParameterizedTest
	@MethodSource("params")
	public void testRandom(int val, boolean eq){
		Assertions.assertEquals(MathUtil.isPowerOfTwo(val), eq);
	}
}
