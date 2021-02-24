package org.qiunet.excel2cfgs.swing.component;

import org.qiunet.excel2cfgs.swing.enums.ButtonStatus;
import org.qiunet.excel2cfgs.swing.panel.IIconPanel;
import org.qiunet.utils.async.LazyLoader;

/***
 *
 * @Author qiunet
 * @Date 2021/2/10 09:07
 **/
public abstract class IconJPanel implements IIconPanel {
	/**
	 * button
	 */
	private final LazyLoader<IconButton> button = new LazyLoader<IconButton>(() ->
			new IconButton(
			IIconPanel.getImageIcon(this, ButtonStatus.normal),
			IIconPanel.getImageIcon(this, ButtonStatus.enable),
			IIconPanel.getImageIcon(this, ButtonStatus.disable),
			IIconPanel.getImageIcon(this, ButtonStatus.rollover),
			name())
	);


	@Override
	public IconButton getButton() {
		return button.get();
	}
}
