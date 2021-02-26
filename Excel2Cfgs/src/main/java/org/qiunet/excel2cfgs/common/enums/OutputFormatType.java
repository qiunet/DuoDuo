package org.qiunet.excel2cfgs.common.enums;

import org.qiunet.excel2cfgs.setting.SettingManager;

/***
 *
 * @Author qiunet
 * @Date 2021/2/11 22:20
 **/
public enum OutputFormatType {
	json {
		@Override
		public void saveStatus() {
			SettingManager.getInstance().getSetting().setJsonChecked(true);
		}
	},
	xml {
		@Override
		public void saveStatus() {
			SettingManager.getInstance().getSetting().setXmlChecked(true);
		}
	},
	xd {
		@Override
		public void saveStatus() {
			SettingManager.getInstance().getSetting().setXdChecked(true);
		}
	};
	public abstract void saveStatus();
}
