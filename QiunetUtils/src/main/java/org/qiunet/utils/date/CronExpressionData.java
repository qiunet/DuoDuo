package org.qiunet.utils.date;

import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.string.StringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 调度的表达式
 */
public class CronExpressionData {
	private static final  int[] calendars = {Calendar.SECOND,Calendar.MINUTE,Calendar.HOUR_OF_DAY,Calendar.DAY_OF_MONTH,Calendar.MONTH,Calendar.DAY_OF_WEEK};
	/**对应  calendar 中的SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY 是中国的周几*/
	public static final Map<Integer, Byte> DAY_OF_WEEK = new HashMap<Integer, Byte>(){
		private static final long serialVersionUID = -4941953324010074815L;
		{
			put(Calendar.MONDAY, (byte)1);
			put(Calendar.TUESDAY, (byte)2);
			put(Calendar.WEDNESDAY, (byte)3);
			put(Calendar.THURSDAY, (byte)4);
			put(Calendar.FRIDAY, (byte)5);
			put(Calendar.SATURDAY, (byte)6);
			put(Calendar.SUNDAY, (byte)7);
		}
	};

	private IExpressionPart[] parts = new IExpressionPart[6];

	public CronExpressionData(String cronExpressionString) {
		String quartzTimes [] = cronExpressionString.split(" +");
		for (int i = 0; i < 6; i++) {
			if (quartzTimes[i].equals("*")) parts[i] = new NormalExpressionPart();
			else if (quartzTimes[i].contains(",")) parts[i] = new ArrayExpressionPart(quartzTimes[i]);
			else if (quartzTimes[i].contains("-")) parts[i] = new IntervalExpressionPart(quartzTimes[i]);
			else if (quartzTimes[i].contains("/")) parts[i] = new GapExpressionPart(quartzTimes[i]);
			else parts[i] = new SpecifyExpressionPart(quartzTimes[i]);
		}
	}

	/***
	 * 给定时间是否有效
	 * @param dt
	 * @return
	 */
	public boolean isValid(Date dt){
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		for (int i = 0; i < parts.length; i++) {
			int curr = c.get(calendars[i]);
			if(calendars[i] == Calendar.MONTH) curr++;
			if(calendars[i] == Calendar.DAY_OF_WEEK) curr = DAY_OF_WEEK.get(curr);

			if (! parts[i].valid(curr)) {
				return false;
			}
		}
		return true;
	}

	private interface IExpressionPart {
		/**
		 * 给定的数字是否有效.
		 * @param num
		 * @return
		 */
		boolean valid(int num);
	}
	private class NormalExpressionPart implements IExpressionPart {
		@Override
		public boolean valid(int num) {
			return true;
		}
	}

	private class SpecifyExpressionPart implements IExpressionPart {
		private int val;
		private SpecifyExpressionPart(String partStr) {
			this.val = Integer.parseInt(partStr);
		}

		@Override
		public boolean valid(int num) {
			return num == val;
		}
	}

	private class ArrayExpressionPart implements IExpressionPart {
		private Integer [] vals;
		private ArrayExpressionPart(String partStr) {
			this.vals = StringUtil.conversion(partStr, ",", Integer.class);
		}
		@Override
		public boolean valid(int num) {
			return CommonUtil.existInList(num, vals);
		}
	}

	private class IntervalExpressionPart implements IExpressionPart {
		private int min, max;
		private IntervalExpressionPart (String partStr) {
			Integer [] datas = StringUtil.conversion(partStr, "-", Integer.class);
			this.min = datas[0];
			this.max = datas[1];
		}
		@Override
		public boolean valid(int num) {
			return num >= min && num <= max;
		}
	}

	private class GapExpressionPart implements IExpressionPart {
		private int start;
		private int gap;
		private GapExpressionPart(String partStr) {
			Integer [] datas = StringUtil.conversion(partStr, "/", Integer.class);
			this.start = datas[0];
			this.gap = datas[1];
		}
		@Override
		public boolean valid(int num) {
			return (num - start) % gap == 0;
		}
	}
}
