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
		String str = "(5 * (3 + 2*6)) / (2 + 3)";
		IFormula parse = FormulaParseManager.parse(str);
		double cal = parse.cal();
		Assertions.assertTrue(cal > 14.9d && cal < 15.1);
	}
	@Test
	public void testRandom(){
		String str = "[(3 + 6), (5 + 20)]";
		IFormula parse = FormulaParseManager.parse(str);
		Assertions.assertEquals("[(3 + 6), (5 + 20)]", parse.toString());
	}

	@Test
	public void testSqrt(){
		String str = "sqrt(5 + 4)";
		IFormula parse = FormulaParseManager.parse(str);
		double cal = parse.cal();
		Assertions.assertTrue(cal > 2.9d && cal < 3.1);
		Assertions.assertEquals("sqrt(5 + 4)", parse.toString());
	}

	@Test
	public void testAttr(){
		String str = "self.ATT * 30% + var2";
		IFormula<IFormulaParam> formula = FormulaParseManager.parse(str);
		System.out.println(formula);
	}

	/**
	 * 万分比测试
	 */
	@Test
	public void testRadio() {
		String str = "1000%% * 10000%";
		IFormula parse = FormulaParseManager.parse(str);
		double cal = parse.cal();
		Assertions.assertTrue(cal > 9.9d && cal < 10.1);

	}
}
