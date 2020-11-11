package org.qiunet.tests.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:56
 */
@PbResponse(1000000)
@ProtobufClass(description = "登录Online响应")
public class LoginOnlineResponse implements IpbResponseData {
	private int day;

	public static LoginOnlineResponse valueOf(int day) {
		LoginOnlineResponse response = new LoginOnlineResponse();
		response.day = day;
		return response;
	}

	public int getDay() {
		return day;
	}
}
