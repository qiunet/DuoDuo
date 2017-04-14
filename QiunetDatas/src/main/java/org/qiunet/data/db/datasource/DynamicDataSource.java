package org.qiunet.data.db.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 
 * @author Zero
 * Aug 26, 2014
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		return CustomerContextHolder.getCustomerType();
	}
}
