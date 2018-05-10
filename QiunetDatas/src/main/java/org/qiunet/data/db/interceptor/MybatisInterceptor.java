package org.qiunet.data.db.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Properties;

@Intercepts({
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
})
public class MybatisInterceptor implements Interceptor {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO_SQL);
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (! logger.isInfoEnabled()) return invocation.proceed();

		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter =  invocation.getArgs().length > 1 ? invocation.getArgs()[1]: null;
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();

		long start = System.currentTimeMillis();
		try {
			return invocation.proceed();
		}finally {
			long diff = System.currentTimeMillis() - start;

			StringBuilder sb = new StringBuilder();
			sb.append(showSql(configuration, boundSql));
			sb.append("\t耗时:[").append(diff).append("ms]");
			logger.info(sb.toString());
		}
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}
	private static String getParameterValue(Object obj) {
		if (obj instanceof String) {
			return "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			return "'" + DateUtil.dateToString(((Date) obj)) + "'";
		} else {
			return obj == null ? "" : obj.toString();
		}
	}

	private String showSql(Configuration configuration, BoundSql boundSql) {
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Object parameterObject = boundSql.getParameterObject();
		if (parameterMappings == null || parameterMappings.isEmpty() || parameterObject == null) {
			return boundSql.getSql();
		}

		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
			sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

		} else {
			MetaObject metaObject = configuration.newMetaObject(parameterObject);
			for (ParameterMapping parameterMapping : parameterMappings) {
				String propertyName = parameterMapping.getProperty();
				if (metaObject.hasGetter(propertyName)) {
					Object obj = metaObject.getValue(propertyName);
					sql = sql.replaceFirst("\\?", getParameterValue(obj));
				} else if (boundSql.hasAdditionalParameter(propertyName)) {
					Object obj = boundSql.getAdditionalParameter(propertyName);
					sql = sql.replaceFirst("\\?", getParameterValue(obj));
				}
			}
		}
		return sql;
	}

}
