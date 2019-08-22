package org.qiunet.entity2table.controller;

import org.qiunet.data.core.enums.ColumnJdbcType;
import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.entity2table.command.Columns;
import org.qiunet.entity2table.command.CreateTableParam;
import org.qiunet.entity2table.service.CreateTableService;
import org.qiunet.entity2table.utils.ClassTools;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 数据对象和表同步控制
 * 19/04/22
 */
class CreateTableController implements IApplicationContextAware {
	private static final Logger logger = LoggerType.DUODUO.getLogger();
	private volatile static CreateTableController instance;

	private CreateTableService createTableService = CreateTableService.getInstance();
	private CreateTableController() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}
	/**
	 * 构建出全部表的增删改的map
	 *
	 * @param classes               从包package中获取所有的Class
	 * @param newTableMap           用于存需要创建的表名+结构
	 * @param modifyTableMap        用于存需要更新字段类型等的表名+结构
	 * @param addTableMap           用于存需要增加字段的表名+结构
	 */
	private void allTableMapConstruct(Set<Class<?>> classes,
									  Map<String, List<Object>> newTableMap, Map<String, List<Object>> modifyTableMap,
									  Map<String, List<Object>> addTableMap) {
		for (Class<?> clas : classes) {

			Table table = clas.getAnnotation(Table.class);
			/*// 没有打注解不需要创建表
			if (null == table) {
				continue;
			}*/
			// 用于存新增表的字段
			List<Object> newFieldList = new ArrayList<>();
			// 用于存新增的字段
			List<Object> addFieldList = new ArrayList<>();
			// 用于存修改的字段
			List<Object> modifyFieldList = new ArrayList<>();

			// 迭代出所有model的所有fields存到newFieldList中
			tableFieldsConstruct(clas, newFieldList);

			// 先查该表是否存在
			int exist = createTableService.findTableCountByTableName(table.name());

			// 不存在时
			if (exist == 0) {
				newTableMap.put(table.name() + ";" + table.comment(), newFieldList);
			} else {
				// 已存在时理论上做修改的操作，这里查出该表的结构
				List<Columns> tableColumnList = createTableService.findTableEnsembleByTableName(table.name());

				// 从sysColumns中取出我们需要比较的列的List
				// 先取出name用来筛选出增加和删除的字段
				List<String> columnNames = ClassTools.getPropertyValueList(tableColumnList, Columns.COLUMN_NAME_KEY);

				// 验证对比从model中解析的fieldList与从数据库查出来的columnList
				// 1. 找出增加的字段
				// 3. 找出更新的字段
				buildAddAndRemoveAndModifyFields(modifyTableMap, addTableMap, table, newFieldList,
					addFieldList, modifyFieldList, tableColumnList, columnNames);

			}
		}
	}

	/**
	 * 根据传入的map创建或修改表结构
	 *
	 * @param newTableMap        用于存需要创建的表名+结构
	 * @param modifyTableMap     用于存需要更新字段类型等的表名+结构
	 * @param addTableMap        用于存需要增加字段的表名+结构
	 */
	private void createOrModifyTableConstruct(Map<String, List<Object>> newTableMap,
											  Map<String, List<Object>> modifyTableMap, Map<String, List<Object>> addTableMap) {
		// 1. 创建表
		createTableByMap(newTableMap);
		// 4. 添加新的字段
		addFieldsByMap(addTableMap);
		// 6. 修改字段类型等
		modifyFieldsByMap(modifyTableMap);
	}

	/**
	 * 构建增加的删除的修改的字段
	 *
	 * @param modifyTableMap        用于存需要更新字段类型等的表名+结构
	 * @param addTableMap           用于存需要增加字段的表名+结构
	 * @param table                 表
	 * @param newFieldList          用于存新增表的字段
	 * @param addFieldList          用于存新增的字段
	 * @param modifyFieldList       用于存修改的字段
	 * @param tableColumnList       已存在时理论上做修改的操作，这里查出该表的结构
	 * @param columnNames           从sysColumns中取出我们需要比较的列的List
	 */
	private void buildAddAndRemoveAndModifyFields(Map<String, List<Object>> modifyTableMap, Map<String, List<Object>> addTableMap,
												  Table table, List<Object> newFieldList,
												  List<Object> addFieldList, List<Object> modifyFieldList,
												  List<Columns> tableColumnList, List<String> columnNames) {
		// 1. 找出增加的字段
		// 根据数据库中表的结构和model中表的结构对比找出新增的字段
		buildNewFields(addTableMap, table, newFieldList, addFieldList, columnNames);

		// 将fieldList转成Map类型，字段名作为主键
		Map<String, CreateTableParam> fieldMap = new HashMap<>();
		for (Object obj : newFieldList) {
			CreateTableParam createTableParam = (CreateTableParam) obj;
			fieldMap.put(createTableParam.getFieldName(), createTableParam);
		}

		// 3. 找出更新的字段
		buildModifyFields(modifyTableMap, table,
				modifyFieldList, tableColumnList, fieldMap);
	}

	/**
	 * 根据数据库中表的结构和model中表的结构对比找出修改类型默认值等属性的字段
	 *
	 * @param modifyTableMap        用于存需要更新字段类型等的表名+结构
	 * @param table                 表
	 * @param modifyFieldList       用于存修改的字段
	 * @param tableColumnList       已存在时理论上做修改的操作，这里查出该表的结构
	 * @param fieldMap              从sysColumns中取出我们需要比较的列的List
	 */
	private void buildModifyFields( Map<String, List<Object>> modifyTableMap,
								   Table table,
								   List<Object> modifyFieldList,
								   List<Columns> tableColumnList, Map<String, CreateTableParam> fieldMap) {
		for (Columns sysColumn : tableColumnList) {
			// 数据库中有该字段时
			CreateTableParam createTableParam = fieldMap.get(sysColumn.getColumn_name());
			if (createTableParam != null) {
				// 验证是否有更新
				// 1.验证类型
				if (!sysColumn.getData_type().toLowerCase().equals(createTableParam.getFieldType().toLowerCase())) {
					modifyFieldList.add(createTableParam);
					continue;
				}
				String typeAndLength = createTableParam.getFieldType().toLowerCase();
				if (!sysColumn.getColumn_type().toLowerCase().equals(typeAndLength)) {
					modifyFieldList.add(createTableParam);
					continue;
				}


				// 5.验证自增
				if ("auto_increment".equals(sysColumn.getExtra()) && !createTableParam.isFieldIsAutoIncrement()) {
					modifyFieldList.add(createTableParam);
					continue;
				}

				// 6.验证默认值
				if (sysColumn.getColumn_default() == null || sysColumn.getColumn_default().equals("")) {
					// 数据库默认值是null，model中注解设置的默认值不为NULL时，那么需要更新该字段
					if (!"NULL".equals(createTableParam.getFieldDefaultValue())) {
						modifyFieldList.add(createTableParam);
						continue;
					}
				} else if (!sysColumn.getColumn_default().equals(createTableParam.getFieldDefaultValue())) {
					// 两者不相等时，需要更新该字段
					modifyFieldList.add(createTableParam);
					continue;
				}

				// 7.验证是否可以为null(主键不参与是否为null的更新)
				if (sysColumn.getIs_nullable().equals("NO") && !createTableParam.isFieldIsKey()) {
					if (createTableParam.isFieldIsNull()) {
						// 一个是可以一个是不可用，所以需要更新该字段
						modifyFieldList.add(createTableParam);
						continue;
					}
				} else if (sysColumn.getIs_nullable().equals("YES") && !createTableParam.isFieldIsKey()) {
					if (!createTableParam.isFieldIsNull()) {
						// 一个是可以一个是不可用，所以需要更新该字段
						modifyFieldList.add(createTableParam);
						continue;
					}
				}
			}
		}

		if (modifyFieldList.size() > 0) {
			modifyTableMap.put(table.name(), modifyFieldList);
		}
	}

	/**
	 * 根据数据库中表的结构和model中表的结构对比找出新增的字段
	 *
	 * @param addTableMap  用于存需要增加字段的表名+结构
	 * @param table        表
	 * @param newFieldList model中的结构
	 * @param addFieldList 用于存新增的字段
	 * @param columnNames  数据库中的结构
	 */
	private void buildNewFields(Map<String, List<Object>> addTableMap, Table table, List<Object> newFieldList,
								List<Object> addFieldList, List<String> columnNames) {
		for (Object obj : newFieldList) {
			CreateTableParam createTableParam = (CreateTableParam) obj;
			// 循环新的model中的字段，判断是否在数据库中已经存在
			if (!columnNames.contains(createTableParam.getFieldName())) {
				// 不存在，表示要在数据库中增加该字段
				addFieldList.add(obj);
			}
		}
		if (addFieldList.size() > 0) {
			addTableMap.put(table.name(), addFieldList);
		}
	}

	/**
	 * 迭代出所有model的所有fields存到newFieldList中
	 *
	 * @param clas                  准备做为创建表依据的class
	 * @param newFieldList          用于存新增表的字段
	 */
	private void tableFieldsConstruct(Class<?> clas,
									  List<Object> newFieldList) {
		Field[] fields = clas.getDeclaredFields();

		for (Field field : fields) {
			// 判断方法中是否有指定注解类型的注解
			boolean hasAnnotation = field.isAnnotationPresent(Column.class);
			if (hasAnnotation) {
				// 根据注解类型返回方法的指定类型注解
				Column column = field.getAnnotation(Column.class);
				CreateTableParam param = new CreateTableParam();
				param.setFieldName(field.getName());
				ColumnJdbcType columnJdbcType = ColumnJdbcType.parse(field.getType(), column.jdbcType());
				param.setFieldType(columnJdbcType.getJdbcTypeDesc());
				// 主键或唯一键时设置必须不为null
				if (column.isKey()) {
					param.setFieldIsNull(false);
				} else {
					param.setFieldIsNull(column.isNull());
				}
				param.setFieldIsKey(column.isKey());
				param.setFieldIsAutoIncrement(column.isAutoIncrement());
				param.setFieldDefaultValue(column.defaultValue());
				param.setFieldComment(column.comment());

				newFieldList.add(param);
			}
		}
	}

	/**
	 * 根据map结构修改表中的字段类型等
	 *
	 * @param modifyTableMap 用于存需要更新字段类型等的表名+结构
	 */
	private void modifyFieldsByMap(Map<String, List<Object>> modifyTableMap) {
		// 做修改字段操作
		if (modifyTableMap.size() > 0) {
			for (Entry<String, List<Object>> entry : modifyTableMap.entrySet()) {
				for (Object obj : entry.getValue()) {
					Map<String, Object> map = new HashMap<>();
					map.put(entry.getKey(), obj);
					CreateTableParam fieldProperties = (CreateTableParam) obj;
					logger.info("\n\n========开始修改表" + entry.getKey() + "中的字段" + fieldProperties.getFieldName());
					createTableService.modifyTableField(map);
					logger.info("\n\n========完成修改表" + entry.getKey() + "中的字段" + fieldProperties.getFieldName());
				}
			}
		}
	}

	/**
	 * 根据map结构对表中添加新的字段
	 *
	 * @param addTableMap 用于存需要增加字段的表名+结构
	 */
	private void addFieldsByMap(Map<String, List<Object>> addTableMap) {
		// 做增加字段操作
		if (addTableMap.size() > 0) {
			for (Entry<String, List<Object>> entry : addTableMap.entrySet()) {
				for (Object obj : entry.getValue()) {
					Map<String, Object> map = new HashMap<>();
					map.put(entry.getKey(), obj);
					CreateTableParam fieldProperties = (CreateTableParam) obj;
					logger.info("开始为表" + entry.getKey() + "增加字段" + fieldProperties.getFieldName());
					createTableService.addTableField(map);
					logger.info("完成为表" + entry.getKey() + "增加字段" + fieldProperties.getFieldName());
				}
			}
		}
	}

	/**
	 * 根据map结构创建表
	 *
	 * @param newTableMap 用于存需要创建的表名+结构
	 */
	private void createTableByMap(Map<String, List<Object>> newTableMap) {
		// 做创建表操作
		if (newTableMap.size() > 0) {
			for (Entry<String, List<Object>> entry : newTableMap.entrySet()) {
				Map<String, List<Object>> map = new HashMap<>();
				map.put(entry.getKey().split(";")[0], entry.getValue());
				logger.info("开始创建表：" + entry.getKey());
				createTableService.createTable(map, entry.getKey().split(";")[1]);
				logger.info("完成创建表：" + entry.getKey());
			}
		}
	}
	@Override
	public void setApplicationContext(IApplicationContext context) {

		Set<Class<?>> classes = context.getTypesAnnotatedWith(Table.class);

		// 用于存需要创建的表名+结构
		Map<String, List<Object>> newTableMap = new HashMap<>();

		// 用于存需要更新字段类型等的表名+结构
		Map<String, List<Object>> modifyTableMap = new HashMap<>();

		// 用于存需要增加字段的表名+结构
		Map<String, List<Object>> addTableMap = new HashMap<>();


		// 构建出全部表的增删改的map
		allTableMapConstruct(classes, newTableMap, modifyTableMap, addTableMap);

		// 根据传入的map，分别去创建或修改表结构
		createOrModifyTableConstruct(newTableMap, modifyTableMap, addTableMap);
	}
}
