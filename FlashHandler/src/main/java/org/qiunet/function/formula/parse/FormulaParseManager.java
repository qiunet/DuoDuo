package org.qiunet.function.formula.parse;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.qiunet.function.formula.IFormula;
import org.qiunet.function.formula.IFormulaParam;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.util.List;
import java.util.Set;

/***
 *
 *
 * @author qiunet
 * 2020-12-02 11:10
 */
public class FormulaParseManager {
	/**
	 * 解析公式
	 * @param formulaString
	 * @param <Obj>
	 * @return
	 */
	public static <Obj extends IFormulaParam> IFormula<Obj> parse(String formulaString) {
		return FormulaParseManager0.instance.parse(formulaString);
	}


	/***
	 *
	 *
	 * @author qiunet
	 * 2020-12-02 11:05
	 */
	enum FormulaParseManager0 implements IApplicationContextAware {
		instance;

		@Override
		public ScannerType scannerType() {
			return ScannerType.FORMULA;
		}

		private final List<IFormulaParse> parses = Lists.newArrayList();
		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<? extends IFormulaParse>> contextSubTypesOf = context.getSubTypesOf(IFormulaParse.class);
			contextSubTypesOf.forEach(clz -> parses.add((IFormulaParse) context.getInstanceOfClass(clz)));
			parses.sort((o1, o2) -> ComparisonChain.start().compare(o2.order(), o1.order()).result());
		}

		<Obj extends IFormulaParam> IFormula<Obj> parse(FormulaParseContext<Obj> context, String formulaString) {
			for (IFormulaParse parse : parses) {
				IFormula<Obj> formula = parse.parse(context, formulaString);
				if (formula != null) {
					return formula;
				}
			}
			return null;
		}

		<Obj extends IFormulaParam> IFormula<Obj> parse(String formulaString) {
			int count1 = StringUtils.countMatches(formulaString, "(");
			int count2 = StringUtils.countMatches(formulaString, ")");
			Preconditions.checkState(count1 == count2, "Formula [%s] brackets is not match!", formulaString);

			FormulaParseContext<Obj> context = new FormulaParseContext<>();
			String formula = this.bracketPreHandler(context, formulaString, 0);
			return this.parse(context, formula);
		}

		/**
		 * 括号预处理
		 * (5 * (3 +  2)) / 5
		 * (5 + 2) * (3 + 3)
		 */
		private <Obj extends IFormulaParam> String bracketPreHandler(FormulaParseContext<Obj> context, String string, int cursor) {
			String originString = string;
			int begin = string.indexOf("(", cursor);
			if (begin  < 0) {
				return string;
			}
			int i;
			for (i = begin + 1; i < string.length(); i++) {
				char c = string.charAt(i);
				if (c == '(') {
					string = this.bracketPreHandler(context, string, i);
					break;
				}
				if (c == ')') {
					int index = context.add(this.parse(context, string.substring(begin + 1, i)));
					string = string.substring(0, begin) + "${"+index+"}" + string.substring(i+1);
					if (cursor > 0) {
						return string;
					}
					break;
				}
			}
			if (originString.equals(string)){
				throw new CustomException("Data {} Brackets is not match!", originString);
			}
			return this.bracketPreHandler(context, string, 0);
		}
	}
}
