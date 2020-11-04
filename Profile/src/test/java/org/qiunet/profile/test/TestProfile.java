package org.qiunet.profile.test;

import org.junit.Test;
import org.qiunet.profile.Profile;

/***
 *
 *
 * @author qiunet
 * 2020-11-04 16:46
 */
public class TestProfile {

	private static final Profile<String, ProfileType> profile = new Profile<>(ProfileType.class);

	@Test
	public void test(){
		profile.createRow("pt1").add(ProfileType.Test1, 10000).add(ProfileType.Test2, 12000).submit();
		profile.createRow("pt1").add(ProfileType.Test1, 5000).add(ProfileType.Test2, 8000).submit();

		profile.createRow("pt2").add(ProfileType.Test1, 5000).add(ProfileType.Test2, 8000).submit();
		profile.print(System.out);
	}
}
