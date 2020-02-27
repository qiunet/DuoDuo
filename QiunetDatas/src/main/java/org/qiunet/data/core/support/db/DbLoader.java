package org.qiunet.data.core.support.db;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.qiunet.data.redis.constants.RedisDbConstants;
import org.qiunet.data.util.DbProperties;
import org.qiunet.utils.hook.ShutdownHookThread;
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
import java.util.*;

class DbLoader {
	private DbProperties dbProperties = DbProperties.getInstance();
	private static final Logger logger = LoggerType.DUODUO.getLogger();

	/**mybatis 的配置文件名称**/
	private static final String DEFAULT_MYBATIS_FILENAME = "mybatis/mybatis-config.xml";
	/**
	 *DbSourceType 对应的 dataSource
	 */
	private Map<String, SqlSessionFactory> dataSources = new HashMap<>();

	private static final SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

	/**
	 *mybatis 配置文件的名称
	 */
	private static final String MYBATIS_CONFIG_FILENAME = "mybatis_config_filename";

	/***
	 * Db模式和Cache单数据库模式下, 默认的数据库源. 如果没有. 会取第一个(认为配置里也就一个).
	 */
	private static final String DEFAULT_DATABASE_SOURCE = "default_database_source";

	private String mybatisConfigFileName = DEFAULT_MYBATIS_FILENAME;

	/**需要设定的连接池属性. 以及默认值**/
	private static final List<DatasourceAttr> datasourceSettings = new ArrayList<DatasourceAttr>() {
		{
			add(new DatasourceAttr("url", "", String.class));
			add(new DatasourceAttr("username", "", String.class));
			add(new DatasourceAttr("password", "", String.class));
			add(new DatasourceAttr("driverClassName", "", String.class));

			add(new DatasourceAttr("maxIdle", 10, int.class));
			add(new DatasourceAttr("minIdle", 2, int.class));
			add(new DatasourceAttr("maxTotal", 200, int.class));
			add(new DatasourceAttr("initialSize", 20, int.class));
			add(new DatasourceAttr("maxWaitMillis", 1500, long.class));
			add(new DatasourceAttr("logAbandoned", true, boolean.class));
			add(new DatasourceAttr("testOnBorrow", true, boolean.class));
			add(new DatasourceAttr("testWhileIdle", true, boolean.class));
			add(new DatasourceAttr("numTestsPerEvictionRun", 10, int.class));
			add(new DatasourceAttr("removeAbandonedTimeout", 60, int.class));
			add(new DatasourceAttr("validationQuery", "select 1", String.class));
			add(new DatasourceAttr("minEvictableIdleTimeMillis", 30000, long.class));
			add(new DatasourceAttr("timeBetweenEvictionRunsMillis", 10000, long.class));
			add(new DatasourceAttr("removeAbandonedOnMaintenance", true, boolean.class));
		}
	};
	/***
	 *
	 * 拼成这样的串.
	 * datasource.global.url
	 * datasource.0.url
	 * @param datasourceName 数据库命名
	 * @param key 想取的对应的key
	 */
	private String getConfigKey(String datasourceName, String key) {
		return "database."+datasourceName+"."+key;
	}
	private volatile static DbLoader instance;

	private DbLoader() {
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}

		try {
			if (dbProperties.containKey(MYBATIS_CONFIG_FILENAME)) {
				mybatisConfigFileName = dbProperties.getString(MYBATIS_CONFIG_FILENAME);
			}
			this.loaderDataSource();

			ShutdownHookThread.getInstance().addShutdownHook( () -> {
				AbandonedConnectionCleanupThread.checkedShutdown();
				while (DriverManager.getDrivers().hasMoreElements()){
					Driver driver = DriverManager.getDrivers().nextElement();
					try {
						DriverManager.deregisterDriver(driver);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
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
		for (Object key : dbProperties.returnMap().keySet()) {
			if (!key.toString().endsWith("driverClassName")) {
				continue;
			}
			String name = StringUtil.split(key.toString(), ".")[1];
			if (sets.contains(name)) {
				continue;
			}

			SqlSessionFactory factory = buildSqlSessionFactory(name);
			this.dataSources.put(name, factory);
			sets.add(name);
		}

		if (dbProperties.containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			int dbSourceCount = RedisDbConstants.MAX_DB_COUNT / dbProperties.getInt(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY);
			for (int i = 0; i < dbSourceCount; i++) {
				if (! this.dataSources.containsKey(String.valueOf(i))){
					throw new NullPointerException("DbSourceKey [database."+i+".*] config is not exist in db.properties");
				}
			}
		}
	}
	/**
	 * 根据name 构建SqlSessionFactory
	 * @param prefix
	 * @return
	 * @throws Exception
	 */
	private SqlSessionFactory buildSqlSessionFactory(String prefix) throws Exception {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setConnectionInitSqls(Arrays.asList("set names 'utf8mb4'"));
		for (DatasourceAttr setting : datasourceSettings) {
			Object val = setting.defaultVal;
			String dbKey = getConfigKey(prefix, setting.name);
			if(val.getClass() == int.class || val.getClass() == Integer.class) {
				val = dbProperties.getInt(dbKey, (Integer) val);
			}else if (val == boolean.class || val.getClass() == Boolean.class) {
				if (dbProperties.containKey(dbKey)) {
					val = dbProperties.getBoolean(dbKey);
				}
			}else if(val.getClass() == String.class){
				val = dbProperties.getString(dbKey, (String) val);
			}

			Method method = BasicDataSource.class.getDeclaredMethod(setting.methodName, setting.clazz);
			method.invoke(dataSource, val);
		}

		ShutdownHookThread.getInstance().addShutdownHook(() -> {
			// 添加关闭.
			try {
				dataSource.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		Environment environment = new Environment.Builder(prefix)
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
				logger.info("Parsed name["+prefix+"] configuration file: '" + this.mybatisConfigFileName + "'");
			}
			configuration.setEnvironment(environment);
		} catch (Exception e) {
			logger.error("Failed to parse mapping resource: '" + mybatisConfigFileName + "'", e);
			throw new ExceptionInInitializerError("Failed to parse mapping resource: '" + mybatisConfigFileName + "'");
		} finally {
			ErrorContext.instance().reset();
		}
		return builder.build(configuration);
	}

	private static class DatasourceAttr {
		private String name;
		private Object defaultVal;
		private Class<?> clazz;
		private String methodName;
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
	 * 得到默认的sqlFactory
	 * @return
	 */
	private SqlSessionFactory getDefaultSqlSessionFactory(){
		if (! dbProperties.containKey(DEFAULT_DATABASE_SOURCE) && this.dataSources.size() != 1) {
			throw new NullPointerException("default config size must be 1!");
		}
		if (defaultSqlSessionFactory == null) {
			if (dbProperties.containKey(DEFAULT_DATABASE_SOURCE)) {
				this.defaultSqlSessionFactory = dataSources.get(dbProperties.getString(DEFAULT_DATABASE_SOURCE));
			}else {
				this.defaultSqlSessionFactory = new ArrayList<>(dataSources.values()).get(0);
			}
		}
		return defaultSqlSessionFactory;
	}
	private SqlSessionFactory defaultSqlSessionFactory;
	/***
	 * 获得一个默认sqlSession
	 * @return
	 */
	SqlSession getDefaultSqlSession(){
		SqlSessionFactory factory = getDefaultSqlSessionFactory();
		SqlSession sqlSession = factory.openSession(true);
		InvocationHandler handler = new SqlSessionTemp(sqlSession);
		return (SqlSession) Proxy.newProxyInstance(handler.getClass().getClassLoader(), sqlSession.getClass().getInterfaces(), handler);
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
		private SqlSession sqlSession;

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
}
