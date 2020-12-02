package org.qiunet.function.test.formula;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.parse.FormulaParseManager;
import org.qiunet.utils.scanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 15:47
 */
public class TestFormula {
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}


	@Test
	public void test(){
		String str = "(5 * (3 + 2*6)) / (2 + 3)";
		IFormula<Object> parse = FormulaParseManager.parse(str);
		double cal = parse.cal(null, null);
		Assert.assertTrue(cal > 14.9d && cal < 15.1);
	}
}
