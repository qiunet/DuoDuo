package org.qiunet.test.testcase.persistconn;

import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.test.robot.IRobot;

/**
 * Created by qiunet.
 * 17/12/8
 */
public abstract class PersistConnProtobufTestCase<RequestData extends IpbRequestData, Robot extends IRobot> extends PersistConnTestCase<Robot> {
	@Override
	public IpbRequestData buildRequest(Robot robot) {
		return requestBuild(robot);
	}

	protected abstract RequestData requestBuild(Robot robot);
}
