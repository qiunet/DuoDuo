package org.qiunet.utils.test.async;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.async.ParallelProcess;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
 *
 * @author qiunet
 * 2022/1/5 22:51
 */
public class TestParallelProcess {

	@Test
	public void test() {
		List<Integer> integers = IntStream.range(0, 10).boxed().collect(Collectors.toList());
		AtomicInteger counter = new AtomicInteger();
		ParallelProcess.newProcess(integers).handle(val -> {
					System.out.println(Thread.currentThread().getName());
					counter.incrementAndGet();
				});
		Assertions.assertEquals(integers.size(), counter.get());
	}
}
