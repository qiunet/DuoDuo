package org.qiunet.data.core.support.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.hook.ShutdownHookUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

class DbLoader {
	private final ServerConfig serverConfig = ServerConfig.instance;
	private static final Logger logger = LoggerType.DUODUO.getLogger();

	/**mybatis 的配置文件名称**/
	private static final String DEFAULT_MYBATIS_FILENAME = "mybatis/mybatis-config.xml";
	/**
	 *DbSourceType 对应的 dataSource
	 */
	private final Map<String, String> dbNameMapping = Maps.newHashMap();
	private final Map<String, SqlSessionFactory> dataSources = Maps.newHashMap();

	/**
	 *mybatis 配置文件的名称
	 */
	private static final String MYBATIS_CONFIG_FILENAME = "mybatis_config_filename";


	private String mybatisConfigFileName = DEFAULT_MYBATIS_FILENAME;

	/**需要设定的连接池属性. 以及默认值**/
	private static final List<DatasourceAttr> datasourceSettings = new ArrayList<>();

	static {
		datasourceSettings.add(new DatasourceAttr("url", "", String.class));
		datasourceSettings.add(new DatasourceAttr("username", "", String.class));
		datasourceSettings.add(new DatasourceAttr("password", "", String.class));
		datasourceSettings.add(new DatasourceAttr("driverClassName", "", String.class));

		datasourceSettings.add(new DatasourceAttr("maxIdle", 10, int.class));
		datasourceSettings.add(new DatasourceAttr("minIdle", 2, int.class));
		datasourceSettings.add(new DatasourceAttr("maxTotal", 200, int.class));
		datasourceSettings.add(new DatasourceAttr("initialSize", 20, int.class));
		datasourceSettings.add(new DatasourceAttr("maxWaitMillis", 1500, long.class));
		datasourceSettings.add(new DatasourceAttr("logAbandoned", true, boolean.class));
		datasourceSettings.add(new DatasourceAttr("testOnBorrow", true, boolean.class));
		datasourceSettings.add(new DatasourceAttr("testWhileIdle", true, boolean.class));
		datasourceSettings.add(new DatasourceAttr("numTestsPerEvictionRun", 10, int.class));
		datasourceSettings.add(new DatasourceAttr("removeAbandonedTimeout", 60, int.class));
		datasourceSettings.add(new DatasourceAttr("validationQuery", "select 1", String.class));
		datasourceSettings.add(new DatasourceAttr("minEvictableIdleTimeMillis", 30000, long.class));
		datasourceSettings.add(new DatasourceAttr("timeBetweenEvictionRunsMillis", 10000, long.class));
		datasourceSettings.add(new DatasourceAttr("removeAbandonedOnMaintenance", true, boolean.class));
	}
	/***
	 *
	 * 拼成这样的串.
	 * datasource.global.url
	 * datasource.0.url
	 * @param datasourceName 数据库命名
	 * @param key 想取的对应的key
	 */
	private String getConfigKey(String datasourceName, String key) {
		return "db."+datasourceName+"."+key;
	}
	private static DbLoader instance;

	private DbLoader() {
		if (instance != null) {
			throw new CustomException("Instance Duplication!");
		}

		try {
			if (serverConfig.containKey(MYBATIS_CONFIG_FILENAME)) {
				mybatisConfigFileName = serverConfig.getString(MYBATIS_CONFIG_FILENAME);
			}
			this.loaderDataSource();

			ShutdownHookUtil.getInstance().addShutdownHook( () -> {
				while (DriverManager.getDrivers().hasMoreElements()){
					Driver driver = DriverManager.getDrivers().nextElement();
					try {
						DriverManager.deregisterDriver(driver);
					} catch (SQLException e) {
						LoggerType.DUODUO_SQL.error("", e);
					}
				}
			});
		} catch (Exception e) {
			LoggerType.DUODUO_SQL.error("", e);
		}
		instance = this;
	}

	static DbLoader getInstance() {
		if (instance == null) {
			synchronized (DbLoader.class) {
				if (instance == null)
				{
					new DbLoader();
				}
			}
		}
		return instance;
	}

	/***
	 * 加载datasource
	 * @throws Exception
	 */
	private void loaderDataSource() throws Exception {
		Set<String> sets = new HashSet<>();
		for (Object key : serverConfig.returnMap().keySet()) {
			if (!key.toString().endsWith("driverClassName")) {
				continue;
			}
			String dbSourceName = StringUtil.split(key.toString(), ".")[1];
			if (sets.contains(dbSourceName)) {
				continue;
			}

			SqlSessionFactory factory = buildSqlSessionFactory(dbSourceName);
			dbNameMapping.put(dbSourceName, this.findDataBaseNameByUrl(factory));
			this.dataSources.put(dbSourceName, factory);
			sets.add(dbSourceName);
		}
	}
	/**
	 * 根据name 构建SqlSessionFactory
	 * @param dbSourceName
	 * @return
	 * @throws Exception
	 */
	private SqlSessionFactory buildSqlSessionFactory(String dbSourceName) throws Exception {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setConnectionInitSqls(Collections.singletonList("set names 'utf8mb4'"));
		for (DatasourceAttr setting : datasourceSettings) {
			Object val = setting.defaultVal;
			String dbKey = getConfigKey(dbSourceName, setting.name);
			if(val.getClass() == int.class || val.getClass() == Integer.class) {
				val = serverConfig.getInt(dbKey, (Integer) val);
			}else if (val == boolean.class || val.getClass() == Boolean.class) {
				if (serverConfig.containKey(dbKey)) {
					val = serverConfig.getBoolean(dbKey);
				}
			}else if(val.getClass() == String.class){
				val = serverConfig.getString(dbKey, (String) val);
			}

			Method method = BasicDataSource.class.getDeclaredMethod(setting.methodName, setting.clazz);
			method.invoke(dataSource, val);
		}

		ShutdownHookUtil.getInstance().addShutdownHook(() -> {
			// 添加关闭.
			try {
				dataSource.close();
			} catch (SQLException e) {
				LoggerType.DUODUO_SQL.error("", e);
			}
		});

		Environment environment = new Environment.Builder(dbSourceName)
				.dataSource(dataSource)
				.transactionFactory(new JdbcTransactionFactory())
				.build();

		Configuration configuration;
		try {
			InputStream mybatisConfigInputStream = getClass().getClassLoader().getResourceAsStream(mybatisConfigFileName);
			XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(mybatisConfigInputStream, null, null);
			configuration = xmlConfigBuilder.getConfiguration();

			xmlConfigBuilder.parse();
			if (logger.isInfoEnabled()) {
				logger.info("Parsed name[{}] configuration file: '{}'", dbSourceName, this.mybatisConfigFileName);
			}
			configuration.setEnvironment(environment);
		} catch (Exception e) {
			logger.error(MessageFormat.format("Failed to parse mapping resource: ''{0}''", mybatisConfigFileName), e);
			throw new ExceptionInInitializerError("Failed to parse mapping resource: '" + mybatisConfigFileName + "'");
		} finally {
			ErrorContext.instance().reset();
		}
		return new SqlSessionFactoryBuilder().build(configuration);
	}

	boolean contains(String dbSourceName) {
		return dataSources.containsKey(dbSourceName);
	}

	String dbName(String dbSource) {
		Preconditions.checkState(dbNameMapping.containsKey(dbSource), "No dbName for dbSource [%s]", dbSource);
		return dbNameMapping.get(dbSource);
	}

	private static class DatasourceAttr {
		private final String name;
		private final Object defaultVal;
		private final Class<?> clazz;
		private final String methodName;
		DatasourceAttr (String name , Object defaultVal, Class<?> clazz) {
			this.name = name;
			this.clazz = clazz;
			this.defaultVal = defaultVal;

			char [] chars = ("set"+name).toCharArray();
			chars[3] -= 32;
			this.methodName = new String(chars);
		}
	}
	/***
	 * 获得一个sqlsession
	 * @return
	 */
	 SqlSession getSqlSession(String datasourceType){
		SqlSessionFactory factory = this.dataSources.get(datasourceType);
		SqlSession sqlSession = factory.openSession(true);
		InvocationHandler handler = new SqlSessionTemp(sqlSession);
		return (SqlSession) Proxy.newProxyInstance(handler.getClass().getClassLoader(), sqlSession.getClass().getInterfaces(), handler);
	}

	private static class SqlSessionTemp implements InvocationHandler {
		private final SqlSession sqlSession;

		SqlSessionTemp(SqlSession sqlSession) {
			this.sqlSession = sqlSession;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				return method.invoke(sqlSession, args);
			}finally {
				this.sqlSession.close();
			}
		}
	}

	private String findDataBaseNameByUrl(SqlSessionFactory factory) {
	 	String jdbcUrl = ((BasicDataSource) factory.getConfiguration().getEnvironment().getDataSource()).getUrl();
		String database = null;
		int pos, pos1;
		String connUri;

		if (StringUtils.isBlank(jdbcUrl)) {
			throw new IllegalArgumentException("Invalid JDBC url.");
		}

		jdbcUrl = jdbcUrl.toLowerCase();

		if (jdbcUrl.startsWith("jdbc:impala")) {
			jdbcUrl = jdbcUrl.replace(":impala", "");
		}

		if (!jdbcUrl.startsWith("jdbc:")
			|| (pos1 = jdbcUrl.indexOf(':', 5)) == -1) {
			throw new IllegalArgumentException("Invalid JDBC url.");
		}

		connUri = jdbcUrl.substring(pos1 + 1);

		if (connUri.startsWith("//")) {
			if ((pos = connUri.indexOf('/', 2)) != -1) {
				database = connUri.substring(pos + 1);
			}
		} else {
			database = connUri;
		}

		if (database.contains("?")) {
			database = database.substring(0, database.indexOf("?"));
		}

		if (database.contains(";")) {
			database = database.substring(0, database.indexOf(";"));
		}

		if (StringUtils.isBlank(database)) {
			throw new IllegalArgumentException("Invalid JDBC url.");
		}
		return database;
	}
}
