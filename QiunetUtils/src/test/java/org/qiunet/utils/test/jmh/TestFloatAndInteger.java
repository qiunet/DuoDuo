package org.qiunet.utils.test.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/***
 *
 * @author qiunet
 * 2021/11/8 16:04
 */
@Fork(1)
@Threads(4)
@State(Scope.Thread)
@Measurement(iterations = 2)
public class TestFloatAndInteger {
	private float val1;
	private int val2;
	public static void main(String[] args) throws RunnerException {
		new Runner(
				new OptionsBuilder()
						.include(TestFloatAndInteger.class.getSimpleName())
						.measurementIterations(3)
						.syncIterations(false)
						.jvmArgs("-server")
						.build()
		).run();
	}
	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	public void testFloat(){
		float x = 3.1415f;
		float y = 6.2415f;
		val1 = (float) Math.sqrt(x*x + y*y);
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	public void testInteger(){
		int x = 3;
		int y = 6;

		val2 = (int) Math.sqrt(x*x + y*y);
	}
}
