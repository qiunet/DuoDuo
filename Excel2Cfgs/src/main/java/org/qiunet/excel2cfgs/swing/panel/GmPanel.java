package org.qiunet.excel2cfgs.swing.panel;

import com.intellij.uiDesigner.core.GridLayoutManager;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;
import java.awt.*;

/***
 *
 *
 * @author qiunet
 * 2021-02-24 15:56
 */
public class GmPanel extends IconJPanel {
    private JPanel showPanel;

    @Override
    public void activate() {

    }

    @Override
    public void unActivate() {

    }

    @Override
    public void addToParent(ToolTabPanel toolTabPanel) {
        toolTabPanel.getPanelUp().add(getButton());
    }

    @Override
    public IconButtonType type() {
        return IconButtonType.gm;
    }

    @Override
    public JPanel getShowPanel() {
        return showPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        showPanel = new JPanel();
        showPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return showPanel;
    }
}