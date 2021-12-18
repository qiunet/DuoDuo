package org.qiunet.cfg.test.keyval;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.cfg.annotation.CfgValAutoWired;
import org.qiunet.utils.collection.generics.IntegerSet;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 18:26
 ***/
public class TestKeyValManager {

	@CfgValAutoWired(key = "2222")
	private static IntegerSet integers;

	@BeforeClass
	public static void preExec() throws Throwable {
		ClassScanner.getInstance(ScannerType.CFG).scanner();
	}

	@Test
	public void test(){
		Assert.assertEquals(3, integers.size());
	}
}
