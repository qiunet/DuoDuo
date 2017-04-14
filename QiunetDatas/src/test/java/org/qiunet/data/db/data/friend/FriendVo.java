package org.qiunet.data.db.data.friend;

import org.qiunet.data.redis.entity.FriendPo;

/**
 * @author qiunet
 *         Created on 17/2/12 14:39.
 */
public class FriendVo {
	private FriendPo friendPo;
	public FriendVo (FriendPo po){
		this.friendPo = po;
	}
	
	public FriendPo getFriendPo() {
		return friendPo;
	}
}
