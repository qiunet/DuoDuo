package org.qiunet.excel2cfgs.swing;

import com.google.common.collect.Lists;
import org.qiunet.excel2cfgs.listener.Excel2CfgServerStartListenerData;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.panel.IIconPanel;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/***
 * 工具栏按钮
 *
 * @Author qiunet
 * @Date 2021/2/9 22:04
 **/
public enum IconButtonManager implements IApplicationContextAware {
	instance;

	private Map<IconButtonType, IIconPanel> panelMap;
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<? extends IIconPanel>> classes = context.getSubTypesOf(IIconPanel.class);
		this.panelMap = classes.stream()
				.filter(clz -> ! Modifier.isAbstract(clz.getModifiers()))
				.map(context::getInstanceOfClass)
				.map(obj -> (IIconPanel) obj)
				.peek(IIconPanel::initialize)
				.collect(Collectors.toMap(IIconPanel::type, Function.identity()));
	}

	@EventListener
	private void onStart(Excel2CfgServerStartListenerData data) {
		panelMap.get(IconButtonType.cfg).getButton().doClick();
	}

	/**
	 * 获得所有的面板
	 * @return
	 */
	public List<IIconPanel> getIconPanels(){
		return Lists.newArrayList(panelMap.values());
	}

	public IIconPanel getIconPanel(IconButtonType type) {
		return panelMap.get(type);
	}
}
