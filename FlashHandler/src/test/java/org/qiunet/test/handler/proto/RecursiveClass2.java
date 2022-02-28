package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/2/9 11:14
 */

@ChannelData(ID = -1, desc = "")
public class RecursiveClass2 implements IChannelData {
	@Protobuf(description = "")
	private int intVal;

	@Protobuf(description = "性别")
	private GenderType genderType;

	@Protobuf(description = "list")
	private List<GenderType> typeList;

	@Protobuf(description = "obj")
	private RecursiveObj2 obj2;
}
