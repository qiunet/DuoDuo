package org.qiunet.tests.response;

import org.qiunet.test.response.ProtobufResponse;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.tests.proto.PlayerIndexProto;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/9
 */
@Response(ID = 1000001)
public class PlayerIndexResponse extends ProtobufResponse<PlayerIndexProto.PlayerIndexResponse, Robot> {
	@Override
	public void response(Robot robot, PlayerIndexProto.PlayerIndexResponse playerIndexResponse) {
		robot.packData.items = playerIndexResponse.getItemsList();
	}
}
