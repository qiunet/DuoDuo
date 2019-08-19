package org.qiunet.project.init.define;

import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.project.init.enums.EntityType;
import org.qiunet.project.init.util.InitProjectUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.SystemPropertyUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 *
 *
 * qiunet
 * 2019-08-14 22:31
 ***/
public abstract class BaseEntityDefine implements IEntityDefine {
	/***
	 * 对象的类名
	 */
	private String name;
	/***
	 * 主键
	 */
	private String key;
	/***
	 * 包名相对于userDir的路径
	 */
	private String baseDir = "src/main/java";
	/***
	 * 包名 路径
	 */
	private String packageName;
	/***
	 * 表名
	 */
	private String tableName;
	/***
	 * 所有的字段定义
	 */
	private List<FieldDefine> fieldDefines = new ArrayList<>();
	/***
	 * 所有的构造函数定义
	 */
	private List<ConstructorDefine> constructorDefines = new ArrayList<>();

	private EntityType entityType;
	private Class<? extends IEntity> entityClass;

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

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String getKeyName() {
		Optional<FieldDefine> keyField = fieldDefines.stream().filter(f -> f.getName().equals(key)).findFirst();
		FieldDefine fieldDefine = keyField.orElseThrow(() -> new NullPointerException("DoName ["+name+"] have not a field named ["+key+"]"));
		return fieldDefine.getName();
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
		return "DELETE FROM " + realTableName() + " " + buildWhereCondition();
	}

	@Override
	public String getInsertSql() {
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(realTableName()).append(" (")
		.append(fieldDefines.stream().map(FieldDefine::getName).collect(Collectors.joining("`, `", "`", "`")))
		.append(") VALUES (")
		.append(fieldDefines.stream().map(FieldDefine::getName).collect(Collectors.joining("}, #{", "#{", "}")))
		.append(");");
		return sb.toString();
	}

	@Override
	public String getSelectSql() {
		return "SELECT * FROM " + realTableName() + " " + buildWhereCondition();
	}

	@Override
	public String getUpdateSql() {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(realTableName()).append(" SET ");
		for (int i = 0; i < fieldDefines.size(); i++) {
			FieldDefine define = fieldDefines.get(i);
			sb.append("`").append(define.getName()).append("` = #{")
				.append(define.getName()).append("}");
			if (i < fieldDefines.size() - 1) sb.append(", ");
		}
		sb.append(" ").append(buildWhereCondition());
		return sb.toString();
	}

	/**
	 * 真实的 表名
	 * 包含 库信息 分表信息等
	 * @return
	 */
	protected abstract String realTableName();
	/**
	 * 搞定where condition
	 * @return
	 */
	protected abstract String buildWhereCondition();

}
