package org.qiunet.cross.test.event;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.cross.event.BaseCrossPlayerEventData;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:31
 */
@ProtobufClass(description = "跨服登录事件")
public class CrossPlayerLoginEventData extends BaseCrossPlayerEventData {
}
