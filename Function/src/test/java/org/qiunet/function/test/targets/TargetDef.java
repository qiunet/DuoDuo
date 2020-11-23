package org.qiunet.function.test.targets;

import org.qiunet.function.targets.ITargetDef;

/***
 *
 * @author qiunet
 * 2020-11-23 21:44
 **/
public class TargetDef implements ITargetDef<TargetType> {
	private TargetType targetType;
	private String targetParam;
	private long value;

	public static TargetDef valueOf(TargetType targetType, long value){
		return valueOf(targetType, value, null);
	}

	public static TargetDef valueOf(TargetType targetType, long value, String targetParam){
		TargetDef def = new TargetDef();
		def.targetParam = targetParam;
		def.targetType = targetType;
		def.value = value;
		return def;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

	public void setTargetParam(String targetParam) {
		this.targetParam = targetParam;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public TargetType getTargetType() {
		return targetType;
	}

	@Override
	public String getTargetParam() {
		return targetParam;
	}

	@Override
	public long getValue() {
		return value;
	}
}
