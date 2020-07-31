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
		cdtime.recordCd(CdType.CHAT);

		boolean timeout = cdtime.isTimeout(CdType.CHAT);
		Assert.assertFalse(timeout);
	}
}
