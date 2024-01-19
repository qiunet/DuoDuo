package org.qiunet.test.cross.common.event;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.cross.event.ToCrossPlayerEvent;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:31
 */
@ProtobufClass(description = "跨服登录事件")
public class CrossPlayerLoginEvent extends ToCrossPlayerEvent {
}
