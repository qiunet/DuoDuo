package org.qiunet.excel2cfgs.swing.panel;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.swing.SwingUtil;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.listener.JButtonMouseListener;
import org.qiunet.excel2cfgs.swing.listener.JTextFieldHintListener;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.timer.TimerManager;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/***
 * 时间戳转换的面板
 *
 * @Author qiunet
 * @Date 2021/2/9 21:56
 **/
public class TimePanel extends IconJPanel implements ClipboardOwner {
    private JPanel showPanel;

    private JLabel currTimestamp;
    private JButton copyTimestamp;
    private JButton copyDatetime;
    private JLabel currDatetime;
    private JTextField timestampTextField;
    private JTextField dateTimeTextField;
    private JButton convertToDatetime;
    private JButton convertToTimeStamp;
    private JComboBox<TimeUnitWrapper> timeunit;
    /**
     * 时间调度
     */
    private ScheduledFuture<?> future;
    private final Clipboard clipboard;

    public TimePanel() {
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        for (TimeUnitWrapper value : TimeUnitWrapper.values()) {
            this.timeunit.addItem(value);
        }
        this.timeunit.setSelectedIndex(0);

        copyDatetime.addActionListener(e -> {
            clipboard.setContents(new StringSelection(currDatetime.getText()), TimePanel.this);
        });

        copyTimestamp.addActionListener(e -> {
            clipboard.setContents(new StringSelection(currTimestamp.getText()), TimePanel.this);
        });


        JTextFieldHintListener datetimeFocusListener = new JTextFieldHintListener(dateTimeTextField, "时间格式: " + DateUtil.DEFAULT_DATE_TIME_FORMAT);
        JTextFieldHintListener timestampFocusListener = new JTextFieldHintListener(timestampTextField, "填入时间戳");
        timestampTextField.addFocusListener(timestampFocusListener);
        dateTimeTextField.addFocusListener(datetimeFocusListener);

        convertToDatetime.addActionListener(e -> {
            String timeStampString = timestampTextField.getText();
            String defaultText = timestampFocusListener.getDefaultText();
            if (StringUtil.isEmpty(timeStampString) || defaultText.equals(timeStampString)) {
                SwingUtil.alterError("输入框内容为空!");
                return;
            }
            if (!StringUtil.isNum(timeStampString) || Long.parseLong(timeStampString) < 0) {
                SwingUtil.alterError("请输入正确的时间戳!");
                return;
            }

            long val = Long.parseLong(timeStampString);
            long millis = ((TimeUnitWrapper) Objects.requireNonNull(timeunit.getSelectedItem())).unit.toMillis(val);
            this.dateTimeTextField.setText(DateUtil.dateToString(millis));
        });


        convertToTimeStamp.addActionListener(e -> {
            String datetimeString = dateTimeTextField.getText();
            String defaultText = datetimeFocusListener.getDefaultText();

            if (StringUtil.isEmpty(datetimeString) || defaultText.equals(datetimeString)) {
                SwingUtil.alterError("输入框内容为空!");
                return;
            }
            String text = "";
            try {
                LocalDateTime localDateTime = DateUtil.stringToDate(datetimeString);
                long milliByTime = DateUtil.getMilliByTime(localDateTime);
                TimeUnit convertTo = ((TimeUnitWrapper) Objects.requireNonNull(timeunit.getSelectedItem())).unit;
                long convert = convertTo.convert(milliByTime, TimeUnit.MILLISECONDS);
                text = String.valueOf(convert);
            } catch (Exception ex) {
                SwingUtil.alterError("输入框内容错误!");
            }
            if (!StringUtil.isEmpty(text)) {
                timestampTextField.setText(text);
            }
        });

        copyDatetime.addMouseListener(new JButtonMouseListener(copyDatetime, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
        copyTimestamp.addMouseListener(new JButtonMouseListener(copyTimestamp, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
        convertToDatetime.addMouseListener(new JButtonMouseListener(convertToDatetime, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
        convertToTimeStamp.addMouseListener(new JButtonMouseListener(convertToTimeStamp, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
    }

    @Override
    public void unActivate() {
        if (future != null) {
            future.cancel(true);
        }
    }

    @Override
    public void activate() {
        this.future = TimerManager.executor.scheduleAtFixedRate(() -> {
            long timeMillis = System.currentTimeMillis();
            this.currDatetime.setText(DateUtil.dateToString(timeMillis));
            this.currTimestamp.setText(String.valueOf((timeMillis / 1000)));
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public IconButtonType type() {
        return IconButtonType.time;
    }

    @Override
    public JPanel getShowPanel() {
        return showPanel;
    }

    @Override
    public void addToParent(ToolTabPanel toolTabPanel) {
        toolTabPanel.getPanelUp().add(this.getButton());
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

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
        showPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        currTimestamp = new JLabel();
        Font currTimestampFont = this.$$$getFont$$$(null, -1, 24, currTimestamp.getFont());
        if (currTimestampFont != null) currTimestamp.setFont(currTimestampFont);
        currTimestamp.setForeground(new Color(-13711846));
        currTimestamp.setText("12345678");
        showPanel.add(currTimestamp, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currDatetime = new JLabel();
        Font currDatetimeFont = this.$$$getFont$$$(null, -1, 24, currDatetime.getFont());
        if (currDatetimeFont != null) currDatetime.setFont(currDatetimeFont);
        currDatetime.setForeground(new Color(-13711846));
        currDatetime.setText("2021-01-21 12:12:12");
        showPanel.add(currDatetime, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        showPanel.add(panel1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(611, 14), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(611, 24), null, 0, false));
        convertToDatetime = new JButton();
        convertToDatetime.setText("↓转换");
        convertToDatetime.setToolTipText("时间戳转换为时间");
        panel2.add(convertToDatetime, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        convertToTimeStamp = new JButton();
        convertToTimeStamp.setText("↑转换");
        convertToTimeStamp.setToolTipText("时间转换为时间戳");
        panel2.add(convertToTimeStamp, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timeunit = new JComboBox();
        Font timeunitFont = this.$$$getFont$$$(null, Font.BOLD, 18, timeunit.getFont());
        if (timeunitFont != null) timeunit.setFont(timeunitFont);
        panel2.add(timeunit, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateTimeTextField = new JTextField();
        panel1.add(dateTimeTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(611, 30), null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("PT Mono", Font.PLAIN, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("时间戳");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("PT Mono", Font.PLAIN, 18, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("时间");
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("PT Mono", Font.PLAIN, 18, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("操作");
        panel1.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timestampTextField = new JTextField();
        panel1.add(timestampTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(611, 30), null, 0, false));
        copyDatetime = new JButton();
        copyDatetime.setText("复制");
        showPanel.add(copyDatetime, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyTimestamp = new JButton();
        copyTimestamp.setText("复制");
        showPanel.add(copyTimestamp, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label1.setLabelFor(timestampTextField);
        label2.setLabelFor(dateTimeTextField);
        label3.setLabelFor(timestampTextField);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return showPanel;
    }

    private enum TimeUnitWrapper {

        SECONDS(TimeUnit.SECONDS, "秒"),

        MILLISECONDS(TimeUnit.MILLISECONDS, "毫秒");

        private final String name;
        private final TimeUnit unit;

        TimeUnitWrapper(TimeUnit unit, String name) {
            this.name = name;
            this.unit = unit;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
