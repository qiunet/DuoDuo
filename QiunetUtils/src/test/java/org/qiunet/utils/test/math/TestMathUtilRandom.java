package org.qiunet.utils.test.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author qiunet
 *         Created on 16/11/6 13:54.
 */
public class TestMathUtilRandom {

	private static Stream<Arguments> params(){
		return IntStream.range(1, 20)
				.mapToObj(i -> Arguments.arguments(i, i*2));
	}

	@ParameterizedTest
	@MethodSource("params")
	public void testRandom(int randStart, int randEnd){
		int rand = MathUtil.random(randStart, randEnd);
		LoggerType.DUODUO.info(randStart +", "+ randEnd+ ", "+rand);
		Assertions.assertTrue(rand >= randStart && rand < randEnd);
	}
}
