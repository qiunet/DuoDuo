package org.qiunet.test.function.test.formula;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;
import org.qiunet.function.formula.parse.FormulaParseManager;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 15:47
 */
public class TestFormula {
	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.FORMULA).scanner();
	}


	@Test
	public void testExpression(){
		String str = "(5 * (3 + 2 * 6)) / (2 + 3)";
		IFormula parse = FormulaParseManager.parse(str);
		double cal = parse.cal();
		Assertions.assertTrue(cal > 14.9d && cal < 15.1);
		Assertions.assertEquals(parse.toString(), str);
	}
	@Test
	public void testRandom(){
		String str = "[(3 + 6), (5 + 20)]";
		IFormula parse = FormulaParseManager.parse(str);
		Assertions.assertEquals(parse.toString(), str);
	}

	@Test
	public void testSqrt(){
		String str = "4 * (5 + sqrt(5 + 4))";
		IFormula parse = FormulaParseManager.parse(str);
		double cal = parse.cal();
		Assertions.assertTrue(cal > 31.9d && cal < 32.1);
		Assertions.assertEquals(parse.toString(), str);
	}

	@Test
	public void testLog(){
		String str = "4 * (5 + log(5 + 15.0855))";
		IFormula parse = FormulaParseManager.parse(str);
		double cal = parse.cal();
		Assertions.assertTrue(cal > 31.9d && cal < 32.1);
		Assertions.assertEquals(parse.toString(), str);
	}

	@Test
	public void testLog10(){
		String str = "4 * (5 + log10(100 * 10))";
		IFormula parse = FormulaParseManager.parse(str);
		double cal = parse.cal();
		Assertions.assertTrue(cal > 31.9d && cal < 32.1);
		Assertions.assertEquals(parse.toString(), str);
	}

	@Test
	public void testAttr(){
		String str = "self.ATT * 0.3 + var2";
		IFormula<IFormulaParam> formula = FormulaParseManager.parse(str);
		Assertions.assertEquals(formula.toString(), str);
	}

	/**
	 * 万分比测试
	 */
	@Test
	public void testRadio() {
		String str = "0.1 * 100";
		IFormula formula = FormulaParseManager.parse(str);
		double cal = formula.cal();
		Assertions.assertTrue(cal > 9.9d && cal < 10.1);
		Assertions.assertEquals(formula.toString(), str);
	}
}
