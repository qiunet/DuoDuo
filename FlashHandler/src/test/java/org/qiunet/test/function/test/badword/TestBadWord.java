package org.qiunet.test.function.test.badword;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.function.badword.BadWordFilter;
import org.qiunet.function.badword.DefaultBadWord;
import org.qiunet.function.badword.LoadBadWordEventData;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/27 19:35
 **/
public class TestBadWord {

	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.EVENT).scanner();
	}
	@Test
	public void testBadWord() {
		DefaultBadWord defaultBadWord = new DefaultBadWord(new String[]{"柟", "王岐山", "王玉刚", "毛泽东", "fuck", "www.qq.com"});
		LoadBadWordEventData.valueOf(defaultBadWord).fireEventHandler();

		Assertions.assertEquals("毛泽东", BadWordFilter.instance.powerFind("sss毛2泽3东7--"));
		Assertions.assertEquals("fuck", BadWordFilter.instance.powerFind("sss毛fuck东7--"));
		Assertions.assertEquals(null, BadWordFilter.instance.powerFind("王小川"));

		Assertions.assertEquals("柟", BadWordFilter.instance.find("sss王柟山7--"));
		Assertions.assertEquals("王岐山", BadWordFilter.instance.find("s王ss王岐山7--"));

		Assertions.assertEquals("ss王s****7我****--", BadWordFilter.instance.powerFilter("ss王s王s#岐&&山7我毛~泽~东--"));
		Assertions.assertEquals("ss王s***7我***--", BadWordFilter.instance.doFilter("ss王s王岐山7我毛泽东--"));
		Assertions.assertEquals("访问**********就行", BadWordFilter.instance.doFilter("访问www.qq.com就行"));
	}
}
