package org.qiunet.tests.response;

import org.qiunet.test.response.ProtobufResponse;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.tests.proto.PlayerIndexResponse;
import org.qiunet.tests.robot.Robot;

import static org.qiunet.tests.protocol.ProtocolId.Test.PLAYER_INDEX_RESP;

/**
 * Created by qiunet.
 * 17/12/9
 */
@Response(ID = PLAYER_INDEX_RESP)
public class PlayerIndexResp extends ProtobufResponse<PlayerIndexResponse, Robot> {
	@Override
	public void response(Robot robot, PlayerIndexResponse playerIndexResponse) {
		robot.packData.items = playerIndexResponse.getItems();
	}
}
