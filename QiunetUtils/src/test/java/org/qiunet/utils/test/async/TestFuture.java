package org.qiunet.utils.test.async;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.async.future.DPromise;

/***
 *
 *
 * @author qiunet
 * 2020-03-18 12:23
 ***/
public class TestFuture {
	private static String val = "";
	@Test
	public void testFuture() {
		DPromise<String> promise = DPromise.create();
		promise.whenComplete((res, err) -> val = res);

		String result = "qiunet";
		promise.trySuccess(result);
		Assert.assertEquals(result, val);
	}
}
