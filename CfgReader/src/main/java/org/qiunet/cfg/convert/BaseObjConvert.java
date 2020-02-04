package org.qiunet.cfg.convert;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.qiunet.cfg.base.ICfg;

/***
 *
 * 配置文件里面 string 转对象的基类
 * @author qiunet
 * 2020-02-04 12:13
 **/
public abstract class BaseObjConvert<T> extends AbstractSingleValueConverter {


	@Override
	public Object fromString(String str) {
		return fromString0(str);
	}

	/***
	 * 转换成自己需要的对象
	 * @param str
	 * @return
	 */
	protected abstract T fromString0(String str);
}
