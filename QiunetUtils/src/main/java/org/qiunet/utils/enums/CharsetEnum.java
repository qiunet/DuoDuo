package org.qiunet.utils.enums;

import java.nio.charset.Charset;

/**
 * 字符编码集 一般utf-8 就行了
 * @author qiunet
 *
 */
public enum CharsetEnum {
	/**
	 * 8-bit UTF (UCS Transformation Format)
	 */
	UTF_8("utf-8"),
	/**
	 * 16-bit UTF (UCS Transformation Format) whose byte order is identified by
	 * an optional byte-order mark
	 */
	UTF_16("UTF-16"),
	/**
	 * 7-bit ASCII, as known as ISO646-US or the Basic Latin block of the
	 * Unicode character set
	 */
	US_ASCII("US-ASCII"),
	/**
	 * ISO Latin Alphabet No. 1, as known as <tt>ISO-LATIN-1</tt>
	 */
	ISO_8859_1("ISO-8859-1"),
	;
	private Charset charset;
	private CharsetEnum(String charsetName){
		this.charset = Charset.forName(charsetName);
	}

	public String toString(){
		return charset.toString();
	}
}
