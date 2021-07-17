package org.qiunet.utils.test.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/***
 * length     	list  		map
 * 5		 52312462	    119100497
 * 10 		 44298417		105671204
 * 100		 16628849	    117890289
 * 1000		 2052041		83157608
 * 10000 	 167822			61251047
 *
 * list 数据越多. 查找越慢. 这个符合我们的预期.
 * map因为hash算法问题. 最终部分的查找会变更为 linkList和Tree的查找. 所以数据量越多. 越慢也是对的
 * 从测试来看. map的查找性能一直远远比list高
 *
 * @author qiunet
 * 2020-11-21 06:55
 **/
@Fork(1)
@Threads(1)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 5)
public class TestHashMapAndListProfile {
	private static final int length = 10000;
	private final List<Integer> list = Lists.newArrayListWithCapacity(length);
	private final Map<Integer, Integer> map = Maps.newHashMapWithExpectedSize((int) (1.4 * length));

	@Benchmark
	public int testMap() {
		int nextInt = ThreadLocalRandom.current().nextInt(length);
		return map.get(nextInt);
	}

	@Benchmark
	public int testList() {
		int nextInt = ThreadLocalRandom.current().nextInt(length);
		return list.indexOf(nextInt);
	}

	@Setup
	public void init(){
		list.clear();
		map.clear();
		IntStream.range(0, length).forEach(i -> {
			list.add(i);
			map.put(i, i);
		});
	}

	public static void main(String[] args) throws RunnerException {
		new Runner(new OptionsBuilder()
			.include(TestHashMapAndListProfile.class.getSimpleName())
			.syncIterations(false)
			.jvmArgs("-server")
			.build()).run();
	}
}
