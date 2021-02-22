package org.qiunet.excel2cfgs.swing.panel;

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
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.time.LocalDateTime;
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
			if (! StringUtil.isNum(timeStampString) || Long.parseLong(timeStampString) < 0) {
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
			}catch (Exception ex) {
				SwingUtil.alterError("输入框内容错误!");
			}
			if (! StringUtil.isEmpty(text)) {
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
	public IconButtonType type(){
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
