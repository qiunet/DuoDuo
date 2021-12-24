package org.qiunet.test.function.test.cd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.function.cd.CdTimer;

/***
 *
 *
 * @author qiunet
 * 2020-03-02 12:06
 ***/
public class TestCdTimer {

	private static final CdTimer<CdType> cdTimer = new CdTimer<>();
	@Test
	public void testCd() {
		cdTimer.cleanCd(CdType.CHAT);
		cdTimer.recordCd(CdType.CHAT);

		boolean timeout = cdTimer.isTimeout(CdType.CHAT);
		Assertions.assertFalse(timeout);

		cdTimer.cleanCd(CdType.GUILD_JOIN);

		cdTimer.recordCd(CdType.GUILD_JOIN);
		timeout = cdTimer.isTimeout(CdType.GUILD_JOIN);
		Assertions.assertTrue(timeout);

		cdTimer.recordCd(CdType.GUILD_JOIN);
		timeout = cdTimer.isTimeout(CdType.GUILD_JOIN);
		Assertions.assertFalse(timeout);

		cdTimer.recordCd(CdType.GUILD_JOIN);
		timeout = cdTimer.isTimeout(CdType.GUILD_JOIN);
		Assertions.assertFalse(timeout);
	}
}
