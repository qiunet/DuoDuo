package org.qiunet.utils.timer.cd;

import org.junit.Assert;
import org.junit.Test;

/***
 *
 *
 * @author qiunet
 * 2020-03-02 12:06
 ***/
public class TestCdTimer {

	private static final CdTimer cdtime = new CdTimer();
	@Test
	public void testCd() {
		cdtime.removeCd(CdType.CHAT);
		boolean timeout = cdtime.validCDTimeout(CdType.CHAT);
		Assert.assertTrue(timeout);

		timeout = cdtime.validCDTimeout(CdType.CHAT);
		Assert.assertFalse(timeout);
	}
}
