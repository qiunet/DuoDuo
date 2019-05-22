package org.qiunet.utils.badword;

import org.junit.Assert;
import org.junit.Test;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/27 19:35
 **/
public class TestBadWord {
	@Test
	public void testBadWord() {
		BadWordFilter.getInstance().loadBadWord(new DefaultBadWord(new String[]{"柟", "王岐山", "王玉刚", "毛泽东", "www.qq.com"}));

		Assert.assertEquals("毛泽东", BadWordFilter.getInstance().powerFind("sss毛2泽3东7--"));

		Assert.assertEquals("柟", BadWordFilter.getInstance().find("sss王柟山7--"));
		Assert.assertEquals("王岐山", BadWordFilter.getInstance().find("s王ss王岐山7--"));
		Assert.assertEquals("ss王s***7我***--", BadWordFilter.getInstance().doFilter("ss王s王岐山7我毛泽东--"));
		Assert.assertEquals("访问**********就行", BadWordFilter.getInstance().doFilter("访问www.qq.com就行"));
	}
}
