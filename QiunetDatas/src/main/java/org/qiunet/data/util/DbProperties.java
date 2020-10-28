package org.qiunet.data.util;

import com.google.common.collect.Sets;
import org.qiunet.utils.properties.LoaderProperties;
import org.qiunet.utils.string.StringUtil;

import java.util.Set;

public final class DbProperties extends LoaderProperties {
	private static DbProperties instance = new DbProperties();
	public static DbProperties getInstance() {
		return instance;
	}
	private Set<String> entity2TableDbSourceRange;
	/***
	 * 要求相对 classpath的地址
	 */
	private DbProperties() {
		super("db.properties");
	}

	@Override
	protected void onReloadOver() {
		String [] strs = new String[0];
		String entityToTableRange = getString("entity_to_table_range");
		if (!StringUtil.isEmpty(entityToTableRange)) {
			strs = StringUtil.split(entityToTableRange, ",");
		}
		this.entity2TableDbSourceRange = Sets.newHashSet(strs);
	}


	public boolean isDbSourceNameInRange(String dbSourceName) {
		return entity2TableDbSourceRange.contains(dbSourceName);
	}

	/**
	 * 得到默认数据源
	 * @return
	 */
	public String getDefaultDbSource(){
		return getString("default_database_source");
	}
}
