package org.qiunet.cfg.test.keyval;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

	@BeforeAll
	public static void preExec() throws Throwable {
		ClassScanner.getInstance(ScannerType.CFG).scanner();
	}

	@Test
	public void test(){
		Assertions.assertEquals(3, integers.size());
	}
}
