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
		public void unchecked() {
			SettingManager.getInstance().getSetting().setJsonChecked(false);
		}

		@Override
		public void saveStatus() {
			SettingManager.getInstance().getSetting().setJsonChecked(true);
		}
	},
	xml {
		@Override
		public void unchecked() {
			SettingManager.getInstance().getSetting().setXmlChecked(false);
		}

		@Override
		public void saveStatus() {
			SettingManager.getInstance().getSetting().setXmlChecked(true);
		}
	},
	;

	public static void uncheckedAll(){
		for (OutputFormatType type : values()) {
			type.unchecked();
		}
	}

	public abstract void unchecked();

	public abstract void saveStatus();
}
