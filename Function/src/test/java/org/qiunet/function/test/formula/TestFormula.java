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
	public void testExpression(){
		String str = "(5 * (3 + 2*6)) / (2 + 3)";
		IFormula parse = FormulaParseManager.parse(str);
		double cal = parse.cal();
		Assert.assertTrue(cal > 14.9d && cal < 15.1);
	}
	@Test
	public void testRandom(){
		String str = "[(3 + 6), (5 + 20)]";
		IFormula parse = FormulaParseManager.parse(str);
		Assert.assertEquals("[(3.0 + 6.0),(5.0 + 20.0)]", parse.toString());
	}
}
