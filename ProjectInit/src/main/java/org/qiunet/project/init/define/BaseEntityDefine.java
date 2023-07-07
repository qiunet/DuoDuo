package org.qiunet.project.init.define;

import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.project.init.enums.EntityType;
import org.qiunet.project.init.util.InitProjectUtil;
import org.qiunet.utils.string.StringUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 *
 *
 * qiunet
 * 2019-08-14 22:31
 ***/
 abstract class BaseEntityDefine implements IEntityDefine {
	/***
	 * 对象的类名
	 */
	protected String name;
	/***
	 * 主键
	 */
	protected String key;
	/**
	 * 对表的注释
	 */
	protected String comment;
	/***
	 * 包名相对于userDir的路径
	 */
	protected String baseDir = "src/main/java";
	/***
	 * 包名 路径
	 */
	protected String packageName;
	/**
	 * 是否异步 或者同步
	 */
	protected boolean async = true;
	/***
	 * 是否分库
	 */
	protected String dbSource;
	/**
	 * 是否分表
	 */
	protected boolean splitTable;
	/***
	 * 表名
	 */
	protected String tableName;
	/***
	 * 所有的字段定义
	 */
	protected List<FieldDefine> fieldDefines = new ArrayList<>();
	/***
	 * 所有的构造函数定义
	 */
	protected List<ConstructorDefine> constructorDefines = new ArrayList<>();

	protected EntityType entityType;
	protected Class<? extends IEntity> entityClass;

	protected BaseEntityDefine(EntityType entityType, Class<? extends IEntity> entityClass) {
		this.entityType = entityType;
		this.entityClass = entityClass;
	}

	public Class<? extends IEntity> getEntityClass() {
		return entityClass;
	}

	@Override
	public EntityType getType() {
		return entityType;
	}

	@Override
	public String getDoName() {
		return name;
	}

	@Override
	public String getBoName() {
		return name.replace("Do", "Bo");
	}

	@Override
	public String getServiceName() {
		return name.replace("Do", "Service");
	}

	@Override
	public String getEntityPackage() {
		return packageName+".entity";
	}

	@Override
	public List<ConstructorDefine> getConstructorDefines() {
		return constructorDefines;
	}

	@Override
	public List<FieldDefine> getFieldDefines() {
		return fieldDefines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void addField(FieldDefine fieldDefine) {
		this.fieldDefines.add(fieldDefine);
	}

	public void addConstructor(ConstructorDefine constructorDefine){
		this.constructorDefines.add(constructorDefine);
		constructorDefine.init(this);
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public String getDbSource() {
		return dbSource;
	}

	public void setDbSource(String dbSource) {
		this.dbSource = dbSource;
	}

	public boolean isSplitTable() {
		return splitTable;
	}

	public void setSplitTable(boolean splitTable) {
		this.splitTable = splitTable;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	@Override
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String getKeyName() {
		return key;
	}

	@Override
	public boolean isList() {
		return entityType.isList();
	}

	@Override
	public boolean isCacheType() {
		return entityType.isCacheType();
	}

	@Override
	public boolean isRedisType() {
		return entityType.isRedisType();
	}


	@Override
	public boolean isDbType() {
		return entityType.isDbType();
	}

	@Override
	public String getKeyType() {
		Optional<FieldDefine> keyField = fieldDefines.stream().filter(f -> f.getName().equals(key)).findFirst();
		FieldDefine fieldDefine = keyField.orElseThrow(() -> new NullPointerException("DoName ["+name+"] have not a field named ["+key+"]"));

		switch (fieldDefine.getType()) {
			case "int":
				return "Integer";
			case "long":
				return "Long";
			case "String":
				return "String";
			case "boolean":
				return "Boolean";
			default:
				throw new IllegalArgumentException("not support key type "+fieldDefine.getType());
		}
	}

	@Override
	public Path outputPath() {
		return Paths.get(InitProjectUtil.getRealUserDir().getAbsolutePath(), baseDir,
			packageName.replaceAll("\\.", "/"));
	}

	@Override
	public String getNameSpace() {
		return DbUtil.getNameSpace(this.name);
	}

	@Override
	public String getTableName() {
		if (StringUtil.isEmpty(tableName)) {
			this.tableName = DbUtil.getDefaultTableName(this.name);
		}
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String getDeleteSql() {
		return "DELETE FROM " + realTableName() + " " + buildWhereCondition()+";";
	}

	@Override
	public String getInsertSql() {
		String sb = "INSERT INTO " + realTableName() + " (" +
			fieldDefines.stream().map(FieldDefine::getName).collect(Collectors.joining("`, `", "`", "`")) +
			") VALUES (" +
			fieldDefines.stream().map(FieldDefine::getName).collect(Collectors.joining("}, #{", "#{", "}")) +
			");";
		return sb;
	}

	@Override
	public String getSelectSql() {
		return "SELECT * FROM " + realTableName() + " " + "WHERE " + getKey() + " = #{" + getKey()+ "};";
	}

	@Override
	public String getSelectAllSql() {
		return "SELECT * FROM " + realTableName()+';';
	}

	@Override
	public String getUpdateSql() {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(realTableName()).append(" SET ");
		for (int i = 0; i < fieldDefines.size(); i++) {
			FieldDefine define = fieldDefines.get(i);
			if (define.getName().equals(key)) continue;

			sb.append("`").append(define.getName()).append("` = #{")
				.append(define.getName()).append("}");
			if (i < fieldDefines.size() - 1) sb.append(", ");
		}
		sb.append(" ").append(buildWhereCondition()).append(";");
		return sb.toString();
	}

	/**
	 * 真实的 表名
	 * 包含 库信息 分表信息等
	 * @return
	 */
	protected String realTableName() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTableName());
		if (isSplitTable()) sb.append("_${tbIndex}");
		return sb.toString();
	}
	/**
	 * 搞定where condition
	 * @return
	 */
	protected abstract String buildWhereCondition();

	public boolean isCommentEmpty(){
		return StringUtil.isEmpty(comment);
	}

	public boolean isDbSourceEmpty(){
		return StringUtil.isEmpty(dbSource);
	}
	@Override
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isNeedImportColumnJdbcType(){
		return fieldDefines.stream().anyMatch(f -> Objects.nonNull(f.getJdbcType()));
	}
}
