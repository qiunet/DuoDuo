package org.qiunet.tests.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:28
 */
@ProtobufClass(description = "登录请求")
public class LoginRequest implements IpbRequestData {

	private String openId;

	public static LoginRequest valueOf(String openId) {
		LoginRequest request = new LoginRequest();
		request.openId = openId;
		return request;
	}

	public String getOpenId() {
		return openId;
	}
}
