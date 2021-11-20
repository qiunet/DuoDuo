package org.qiunet.utils.test.jmh;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.google.common.collect.Lists;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.math.MathUtil;

import java.io.IOException;
import java.util.List;

/***
 * Benchmark                                     Mode  Cnt        Score        Error  Units
 * TestJsonAndProtobuf.testJsonSerialize        thrpt    3  2845000.715 ± 287498.223  ops/s
 * TestJsonAndProtobuf.testProtobufSerialize    thrpt    3  2423221.545 ± 252700.364  ops/s
 *
 * TestJsonAndProtobuf.testJsonDeserialize      thrpt    3  1989813.290 ± 580257.673  ops/s
 * TestJsonAndProtobuf.testProtobufDeserialize  thrpt    3  9700656.278 ± 782471.226  ops/s
 *
 * @author qiunet
 * 2021/11/8 16:04
 */
@Fork(1)
@Threads(4)
@State(Scope.Thread)
@Measurement(iterations = 2)
public class TestJsonAndProtobuf {
	private float val1;
	private int val2;
	public static void main(String[] args) throws RunnerException {
		new Runner(
				new OptionsBuilder()
						.include(TestJsonAndProtobuf.class.getSimpleName())
						.measurementIterations(3)
						.syncIterations(false)
						.jvmArgs("-server")
						.build()
		).run();
	}

	private static final int count = 20;
	/**
	 * protobuf codec
	 */
	private Codec<PlayerObj> codec = ProtobufProxy.create(PlayerObj.class);
	/**
	 * 一个player list
	 */
	private List<PlayerData> playerObjs = Lists.newArrayListWithCapacity(count);

	@Setup
	public void setup() {
		for (int i = 0; i < count; i++) {
			List<ItemObj> pack = Lists.newArrayList();
			for (int i1 = 0; i1 < 5; i1++) {
				pack.add(new ItemObj(MathUtil.random(100000), MathUtil.random(100, 200)));
			}
			PlayerObj playerObj = new PlayerObj(MathUtil.random(10000000, 200000000), MathUtil.random(100), MathUtil.random(10000), "Name" + i, "eeeeewkljd;fskdjf" + i, pack);
			playerObjs.add(new PlayerData(playerObj));
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	public void testJsonSerialize(){
		PlayerData playerData = playerObjs.get(MathUtil.random(playerObjs.size()));
		JsonUtil.toJsonString(playerData.playerObj);
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	public void testJsonDeserialize(){
		PlayerData playerData = playerObjs.get(MathUtil.random(playerObjs.size()));
		JsonUtil.toJsonString(playerData.json);
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	public void testProtobufSerialize() throws IOException {
		PlayerData playerData = playerObjs.get(MathUtil.random(playerObjs.size()));
		codec.encode(playerData.playerObj);
	}


	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	public void testProtobufDeserialize() throws IOException {
		PlayerData playerData = playerObjs.get(MathUtil.random(playerObjs.size()));
		codec.decode(playerData.data);
	}

	private class PlayerData {
		private PlayerObj playerObj;

		private String json;

		private byte[] data;
		public PlayerData(PlayerObj playerObj) {
			this.playerObj = playerObj;

			this.json = JsonUtil.toJsonString(this.playerObj);
			try {
				this.data = codec.encode(this.playerObj);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
