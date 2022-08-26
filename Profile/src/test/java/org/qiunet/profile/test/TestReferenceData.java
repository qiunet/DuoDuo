package org.qiunet.profile.test;

import org.junit.jupiter.api.Test;
import org.qiunet.profile.reference.ReferenceData;

/***
 *
 * @author qiunet
 * 2022/8/26 09:48
 */
public class TestReferenceData {

	private static final ReferenceData<String> data = new ReferenceData<>();
	@Test
	public void test() {
		data.record("1", 10);
		data.record("1", 12);
		data.record("1", 9);
		data.record("1", 21);

		data.record("2", 120);
		data.record("2", 112);
		data.record("2", 91);
		data.record("2", 201);


		data.print(System.out);

	}
}
