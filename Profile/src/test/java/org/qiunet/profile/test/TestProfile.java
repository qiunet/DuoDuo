package org.qiunet.profile.test;

import org.junit.jupiter.api.Test;
import org.qiunet.profile.Profile;
import org.qiunet.profile.printer.LoggerPrintStream;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * @author qiunet
 * 2020-11-04 16:46
 */
public class TestProfile {

	private static final Profile<String, ProfileType> profile = new Profile<>(ProfileType.class);

	private static final LoggerPrintStream printStream =  new LoggerPrintStream(LoggerType.DUODUO.getLogger());
	@Test
	public void test(){
		profile.createRow("pt1").add(ProfileType.Test1, 10000).add(ProfileType.Test2, 12000).submit();
		profile.createRow("pt1").add(ProfileType.Test1, 5000).add(ProfileType.Test2, 8000).submit();

		profile.createRow("pt2").add(ProfileType.Test1, 5000).add(ProfileType.Test2, 8000).submit();
		profile.print(printStream);

		profile.print(System.out);
	}
}
