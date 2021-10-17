package org.qiunet.excel2cfgs.swing.panel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.swing.SwingUtil;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.listener.JButtonMouseListener;
import org.qiunet.utils.string.StringUtil;

import javax.swing.*;
import java.awt.*;

/***
 * json 格式化的面板
 * @Author qiunet
 * @Date 2021/2/9 21:57
 **/
public class JsonFormatPanel extends IconJPanel {

    private JPanel showPanel;
	private JTextArea originJson;
	private JButton formatJsonBtn;
	private JTextArea formatJson;

	public JsonFormatPanel() {
		formatJsonBtn.addMouseListener(new JButtonMouseListener(formatJsonBtn, UiConstant.TOOL_BAR_BACK_COLOR, Color.white));
		formatJsonBtn.addActionListener(e -> {
			String string = originJson.getText();
			String trim = string.trim();
			if (StringUtil.isEmpty(trim)) {
				return;
			}

			Object result;
			try {
				if (trim.startsWith("{")) {
					result = JSONObject.parseObject(string);
				} else {
					result = JSONArray.parseArray(trim);
				}
			} catch (Exception ex) {
				SwingUtil.alterError(ex.getMessage());
				ex.printStackTrace();
				return;
			}
			String jsonString = JSON.toJSONString(result, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
					SerializerFeature.WriteDateUseDateFormat);
			jsonString = jsonString.replaceAll("\\t", "    ").replaceAll(":", " : ");
			formatJson.setText(jsonString);
		});
	}

	@Override
    public void unActivate() {
		formatJson.setText("");
		originJson.setText("");
    }

    @Override
    public void activate() {

    }


    @Override
    public IconButtonType type() {
        return IconButtonType.json;
    }

    @Override
    public JPanel getShowPanel() {
        return showPanel;
    }

    @Override
    public void addToParent(ToolTabPanel toolTabPanel) {
        toolTabPanel.getPanelUp().add(this.getButton());
    }

}
