package org.qiunet.utils.test.collection;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

/***
 * length     	list  		skipList		treeSet
 * 5		 14249796	    5149923			10016575
 * 10 		 11841343		4024316			8217781
 * 40		 4846603		2820356			5807853
 * 50		 3852095		2526415			5601629
 * 100		 2378609	    2297986			4752844
 * 150		 1702089		2175690			4621291
 * 200		 1262084		2152967			4331126
 * 1000 	 128862			1796576			3117137
 *
 * list 数据越多. 排序越慢. 这个符合我们的预期.
 * skipList 的性能在数据100以后, 相比数组逐渐出现优势
 * treeSet 在长度40后, 优势开始出现.
 * 所以如果你的排行长度40以上. 就可以使用TreeSet, 当然得保证线程安全.
 *
 * @author qiunet
 * 2020-11-21 06:55
 **/
@Fork(1)
@Threads(1)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 5)
public class TestSkipListAndListProfile {
	private static final  Comparator<RankListObj> comparator = (o1, o2) -> ComparisonChain.start()
		.compare(o2.score, o1.score)
				.compare(o1.score, o2.score)
				.result();

	private static final int length = 1000;
	private  static final Map<Long, RankListObj> dataList = Maps.newHashMapWithExpectedSize((int)(1.4 * length));
	private  static final Map<Long, RankListObj> dataSlist = Maps.newHashMapWithExpectedSize((int)(1.4 * length));
	private  static final Map<Long, RankListObj> dataTreeSet = Maps.newHashMapWithExpectedSize((int)(1.4 * length));

	private static final Set<RankListObj> treeSet = new TreeSet<>(comparator);
	private static final List<RankListObj> list = Lists.newArrayListWithCapacity(length);
	private static final ConcurrentSkipListSet<RankListObj> slist = new ConcurrentSkipListSet<>(comparator);

	@Benchmark
	public void testSkipList() {
		long playerId = ThreadLocalRandom.current().nextLong(length);
		RankListObj obj = dataSlist.get(playerId);
		slist.remove(obj);
		obj.alterScore(ThreadLocalRandom.current().nextLong(10));
		slist.add(obj);
	}

	@Benchmark
	public void testTreeSet() {
		long playerId = ThreadLocalRandom.current().nextLong(length);
		RankListObj obj = dataTreeSet.get(playerId);
		treeSet.remove(obj);
		obj.alterScore(ThreadLocalRandom.current().nextLong(10));
		treeSet.add(obj);
	}

	@Benchmark
	public void testList() {
		long playerId = ThreadLocalRandom.current().nextLong(length);
		RankListObj obj = dataList.get(playerId);
		obj.alterScore(ThreadLocalRandom.current().nextLong(10));
		list.sort(comparator);
	}

	@Setup
	public void init(){
		list.clear();
		slist.clear();
		treeSet.clear();
		LongStream.range(0, length).forEach(i -> {
			RankListObj obj1 = RankListObj.valueOf(i, i);
			list.add(obj1);
			dataList.put(i, obj1);

			RankListObj obj2 = RankListObj.valueOf(i, i);
			dataSlist.put(i, obj2);
			slist.add(obj2);

			RankListObj obj3 = RankListObj.valueOf(i, i);
			dataTreeSet.put(i, obj3);
			treeSet.add(obj2);
		});
		list.sort(comparator);
	}

	public static void main(String[] args) throws RunnerException {
		new Runner(new OptionsBuilder()
			.include(TestSkipListAndListProfile.class.getSimpleName())
			.syncIterations(false)
			.jvmArgs("-server")
			.build()).run();
	}

	private static class RankListObj {
		private long playerId;
		private long score;
		private long dt;


		public static RankListObj valueOf(long playerId, long score) {
			RankListObj rankListObj = new RankListObj();
			rankListObj.dt = System.nanoTime();
			rankListObj.playerId = playerId;
			rankListObj.score = score;
			return rankListObj;
		}

		public void alterScore(long score) {
			this.dt = System.nanoTime();
			this.score += score;
		}

		public long getPlayerId() {
			return playerId;
		}

		public long getScore() {
			return score;
		}


		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			RankListObj that = (RankListObj) o;
			return playerId == that.playerId;
		}

		@Override
		public String toString() {
			return "{" +
				"id=" + playerId +
				", score=" + score +
				'}';
		}

		@Override
		public int hashCode() {
			return Objects.hash(playerId);
		}
	}
}
