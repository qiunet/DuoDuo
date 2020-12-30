package org.qiunet.function.formula.enums;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.formula.param.FighterAttrFormulaParam;
import org.qiunet.function.formula.param.IFighter;

/***
 * 战斗单元 参数 双方枚举
 *
 * @author qiunet
 * 2020-12-30 12:59
 */
public enum FighterParamSide {
	/**
	 * 自身
	 */
	self {
		@Override
		public <Type extends Enum<Type> & IAttrEnum<Type>> IFighter<Type> getFighter(FighterAttrFormulaParam<Type> param) {
			return param.getSelf();
		}
	},
	/**
	 *  目标
	 */
	target {
		@Override
		public <Type extends Enum<Type> & IAttrEnum<Type>> IFighter<Type> getFighter(FighterAttrFormulaParam<Type> param) {
			return param.getTarget();
		}
	};
	public abstract  <Type extends Enum<Type> & IAttrEnum<Type>> IFighter<Type> getFighter(FighterAttrFormulaParam<Type> param);
}
