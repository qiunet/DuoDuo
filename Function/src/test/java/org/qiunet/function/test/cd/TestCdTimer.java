package org.qiunet.function.test.cd;

import org.junit.Assert;
import org.junit.Test;
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
		cdTimer.removeCd(CdType.CHAT);
		cdTimer.recordCd(CdType.CHAT);

		boolean timeout = cdTimer.isTimeout(CdType.CHAT);
		Assert.assertFalse(timeout);
	}
}
