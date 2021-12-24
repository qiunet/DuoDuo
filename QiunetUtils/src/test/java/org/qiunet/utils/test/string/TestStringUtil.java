package org.qiunet.utils.test.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.test.base.BaseTest;

/**
 * @author qiunet
 *         Created on 16/11/6 13:55.
 */
public class TestStringUtil extends BaseTest{
	@Test
	public void testSplit(){
		String str = "1,2,";
		String[] strs = StringUtil.split(str, ",");
		Assertions.assertEquals("",strs[2]);

		str = ",1344,2";
		strs  = StringUtil.split(str, ",");
		Assertions.assertEquals(3, strs.length);
		Assertions.assertEquals("", strs[0]);

		str = "1,2";
		strs  = StringUtil.split(str, ",");
		Assertions.assertEquals(2, strs.length);

		str = ",1,2,";
		strs  = StringUtil.split(str, ",");
		Assertions.assertEquals(4, strs.length);
	}
	@Test
	public void testPowerfullTrim() {
		String str = "s\ts ";
		Assertions.assertEquals("s\ts", StringUtil.powerfulTrim(str));

		str = "\ts\ts\t";
		Assertions.assertEquals("s\ts", StringUtil.powerfulTrim(str));

		str = "abc";
		Assertions.assertEquals(str, StringUtil.powerfulTrim(str));

		str = "ㅤ";// 特殊字符
		Assertions.assertEquals("", StringUtil.powerfulTrim(str));

		str = "　尹漂亮　 　";
		Assertions.assertEquals("尹漂亮", StringUtil.powerfulTrim(str));

		str = "　　﹏ℳ๓二ৡৢﺴ";
		Assertions.assertEquals("二", StringUtil.powerfulTrim(str));

		str = "　　ۣۣ ۣۣ ۣۣ ۣۣ ۣۣ ۣۣ ";
		Assertions.assertEquals("", StringUtil.powerfulTrim(str));
	}
	@Test
	public void testIsNum(){
		Assertions.assertTrue(StringUtil.isNum("123"));
		Assertions.assertTrue(StringUtil.isNum("-123"));
		Assertions.assertFalse(StringUtil.isNum("-1ff"));
		Assertions.assertFalse(StringUtil.isNum(""));
	}
	@Test
	public void testArrayToString() {
		String [] strings = new String[]{"0", "1", "2", "3", "4", "5"};

		Assertions.assertEquals("0,1,2,3,4,5", StringUtil.arraysToString(strings, ","));
		Assertions.assertEquals("[0,1,2,3,4,5]", StringUtil.arraysToString(strings,"[", "]", ","));
		Assertions.assertEquals("[1,2,3,4]", StringUtil.arraysToString(strings,"[", "]", 1, 4,","));

		Object [] arr = new Object[]{"obj1", "obj2", strings};
		Assertions.assertEquals("obj1,obj2,[0, 1, 2, 3, 4, 5]", StringUtil.arraysToString(arr, ","));
	}
	@Test
	public void testMixedStringLength(){
		Assertions.assertEquals(StringUtil.getMixedStringLength("a行b不行"), 8);
		Assertions.assertEquals(StringUtil.getMixedStringLength("abcdefg"), 7);
		Assertions.assertEquals(StringUtil.getMixedStringLength("嘻嘻嘻"), 6);
	}
	@Test
	public void testChineseWord(){
		Assertions.assertEquals(true, StringUtil.regexChinese("我们"));
		Assertions.assertEquals(false, StringUtil.regexChinese("ab"));
		Assertions.assertEquals(false, StringUtil.regexChinese("126"));
		Assertions.assertEquals(false, StringUtil.regexChinese("^%^%#$"));
	}

	@Test
	public void testFormat() {
		Assertions.assertEquals("我叫Qiunet", StringUtil.format("{0}叫{1}", "我", "Qiunet") );
	}
}
