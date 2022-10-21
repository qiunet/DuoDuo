package org.qiunet.utils.test.convert;

import org.qiunet.utils.convert.IEnumReadable;

/***
 *
 * @author qiunet
 * 2021/12/13 13:37
 */
public enum TestType implements IEnumReadable {

	NONE(1),

	TYPE1(2),
	;
	private final int type;

	TestType(int type) {
		this.type = type;
	}

	@Override
	public int value() {
		return type;
	}
}
