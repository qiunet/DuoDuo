package org.qiunet.cross.actor.data;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.utils.protobuf.IProtobufClass;

/***
 *跨服传输的玩家数据基类
 *
 * @author qiunet
 * 2020-10-28 12:11
 */
@ProtobufClass(description = "跨服传输的玩家数据基类")
public abstract class BaseCrossTransferData implements IProtobufClass {
}
