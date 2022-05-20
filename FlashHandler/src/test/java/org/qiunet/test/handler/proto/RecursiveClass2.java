package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/2/9 11:14
 */

public class RecursiveClass2 {
	@Protobuf(description = "")
	private int intVal;

	@Protobuf(description = "性别")
	private GenderType genderType;

	@Protobuf(description = "list")
	private List<GenderType> typeList;

	@Protobuf(description = "obj")
	private RecursiveObj2 obj2;
}
