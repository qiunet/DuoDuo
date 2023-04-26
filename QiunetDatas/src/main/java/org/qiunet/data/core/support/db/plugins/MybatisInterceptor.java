package org.qiunet.data.core.support.db.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Intercepts({
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
})
public class MybatisInterceptor implements Interceptor {
	private final Logger logger = LoggerType.DUODUO_SQL.getLogger();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (! logger.isInfoEnabled()) return invocation.proceed();

		long start = System.nanoTime();
		try {
			return invocation.proceed();
		}finally {
			long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
			logger.info("{}\t耗时:[{} ms]", formatSql(invocation), diff);
		}
	}

	/**
	 * 参数规范化
	 * @param obj 参数对象
	 * @return 规范后的参数值
	 */
	private static String getParameterValue(Object obj) {
		if (obj == null) {
			return "<null>";
		}

		if (obj instanceof Date dt) {
			return DateUtil.dateToString(dt);
		}

		String val;
		if (obj instanceof String) {
			val = "'" + obj + "'";
		} else {
			val = obj.toString();
		}
		// 有问号会导致replaceFirst里面失效. 不会匹配到对应的地方
		return val.replaceAll("[?, $]", "");
	}

	private String formatSql(Invocation invocation) {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter =  invocation.getArgs().length > 1 ? invocation.getArgs()[1]: null;
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);

		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Object parameterObject = boundSql.getParameterObject();
		if (parameterMappings == null || parameterMappings.isEmpty() || parameterObject == null) {
			return boundSql.getSql();
		}

		Configuration configuration = mappedStatement.getConfiguration();
		String sql = boundSql.getSql().replaceAll("\\s+", " ");
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
