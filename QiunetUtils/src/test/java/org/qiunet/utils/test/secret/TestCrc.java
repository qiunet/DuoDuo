package org.qiunet.utils.test.secret;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.zip.CRC32;

/***
 *
 *
 * @author qiunet
 * 2020-11-17 10:55
 */
@Fork(1)
@Threads(4)
@State(Scope.Thread)
@Measurement(iterations = 2)
public class TestCrc {
	private static final ThreadLocal<CRC32> tl = ThreadLocal.withInitial(CRC32::new);
	private static final int length = 100;
	private byte [] bytes;

	public static void main(String[] args) throws RunnerException {
		new Runner(
			new OptionsBuilder()
				.include(TestCrc.class.getSimpleName())
				.measurementIterations(3)
				.syncIterations(false)
				.jvmArgs("-server")
				.build()
		).run();
	}


	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	public void testThreadLocalCrc() {
		CRC32 crc32 = tl.get();
		crc32.reset();
		crc32.update(bytes);
		crc32.getValue();
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	public void testNewCrc() {
		CRC32 crc32 = new CRC32();
		crc32.update(bytes);
		crc32.getValue();
	}

	@Setup
	public void setup(){
		bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = (byte) i;
		}
	}
}
