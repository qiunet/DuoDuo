package org.qiunet.cfg.test.keyval;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.BaseKeyValCfg;

/***
 *
 *
 * @author qiunet
 * 2020-09-18 17:56
 */

@Cfg(value = "config/init/init_data.json", order = 4444)
public class TestKeyValCfg extends BaseKeyValCfg {
		private String id;
		private String val1;
		private long val2;
		private double val3;

	@Override
	public String val() {
		return val1;
	}

	public String getVal1() {
			return val1;
		}

	public long getVal2() {
		return val2;
	}

	public double getVal3() {
		return val3;
	}

	@Override
	public String getId() {
			return id;
		}
}
