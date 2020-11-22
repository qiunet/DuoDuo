package org.qiunet.utils.collection;

import com.google.common.collect.ImmutableMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/***
 * length     	ImmutableMap  		unmodifiableMap
 * 100			 121119216	    		101743721
 * 1000		 	 26297761				69003202
 * 5000		 	 31862831				54617537
 * 10000 	 	 43744642				43727430
 *
 * 从数据看. 两者应该性能相当. 但是 ImmutableMap 不稳定.
 *
 * @author qiunet
 * 2020-11-21 06:55
 **/
@Fork(1)
@Threads(1)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 5)
public class TestImmutableMapAndUnModifyMapProfile {
	private static final int length = 5000;
	private Map<Integer, Integer> unModifyMap;
	private Map<Integer, Integer> immutableMap;

	@Benchmark
	public int testUnModifyMap() {
		int nextInt = ThreadLocalRandom.current().nextInt(length);
		return unModifyMap.get(nextInt);
	}

	@Benchmark
	public int testImmutableMap() {
		int nextInt = ThreadLocalRandom.current().nextInt(length);
		return immutableMap.get(nextInt);
	}

	@Setup
	public void init(){
		Map<Integer, Integer> map = new HashMap<>((int)(1.4 * length));
		IntStream.range(0, length).forEach(i -> {
			map.put(i, i);
		});
		immutableMap = ImmutableMap.copyOf(map);
		unModifyMap = Collections.unmodifiableMap(map);
	}

	public static void main(String[] args) throws RunnerException {
		new Runner(new OptionsBuilder()
			.include(TestImmutableMapAndUnModifyMapProfile.class.getSimpleName())
			.syncIterations(false)
			.jvmArgs("-server")
			.build()).run();
	}
}
