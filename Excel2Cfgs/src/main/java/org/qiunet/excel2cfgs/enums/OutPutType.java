package org.qiunet.excel2cfgs.enums;

/***
 *
 *
 * @author qiunet
 * 2020-01-09 11:43
 ***/
public enum  OutPutType {
	/***
	 * 输出服务端 客户端
	 */
	ALL {
		@Override
		public boolean canWrite(RoleType roleType) {
			return true;
		}
	},
	/***
	 * 仅输出客户端
	 */
	CLIENT {
		@Override
		public boolean canWrite(RoleType roleType) {
			return roleType == RoleType.CLENTER;
		}
	},
	/***
	 * 仅输出服务端
	 */
	SERVER {
		@Override
		public boolean canWrite(RoleType roleType) {
			return roleType == RoleType.SERVER;
		}
	},
	;
	public abstract boolean canWrite(RoleType roleType);
}
