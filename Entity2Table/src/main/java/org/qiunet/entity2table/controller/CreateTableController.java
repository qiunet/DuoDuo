package org.qiunet.entity2table.controller;

import org.qiunet.data.core.enums.ColumnJdbcType;
import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.entity2table.command.Columns;
import org.qiunet.entity2table.command.FieldParam;
import org.qiunet.entity2table.command.TableAlterParam;
import org.qiunet.entity2table.command.TableCreateParam;
import org.qiunet.entity2table.service.CreateTableService;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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
	 * 构建增加的删除的修改的字段
	 *
	 * @param newFieldList            用于存新增表的字段
	 * @param clazz                  表
	 */
	private void handlerAddAndModifyFields(List<FieldParam> newFieldList, Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		// 已存在时理论上做修改的操作，这里查出该表的结构
		List<Columns> tableColumnList = createTableService.findTableEnsembleByTableName(table.name());

		// 从sysColumns中取出我们需要比较的列的List
		// 先取出name用来筛选出增加和删除的字段
		Set<String> columnNames = tableColumnList.stream().map(Columns::getColumn_name).collect(Collectors.toSet());

		// 1. 找出增加的字段
		// 根据数据库中表的结构和model中表的结构对比找出新增的字段
		this.handlerAddFields(clazz, newFieldList, columnNames);

		Map<String, FieldParam> fieldMap = newFieldList.stream().collect(Collectors.toMap(FieldParam::getFieldName, f -> f));
		// 3. 找出更新的字段
		this.handlerModifyFields(clazz, tableColumnList, fieldMap);
	}

	/**
	 * 根据数据库中表的结构和model中表的结构对比找出修改类型默认值等属性的字段
	 *
	 * @param tableColumnList       已存在时理论上做修改的操作，这里查出该表的结构
	 * @param clazz              Do class
	 */
	private void handlerModifyFields(Class<?> clazz, List<Columns> tableColumnList, Map<String, FieldParam> fieldMap) {
		Table table = clazz.getAnnotation(Table.class);

		List<FieldParam> modifyFieldList = new ArrayList<>();
		for (Columns sysColumn : tableColumnList) {
			// 数据库中有该字段时
			FieldParam fieldParam = fieldMap.get(sysColumn.getColumn_name());
			if (fieldParam == null) continue;

			// 验证是否有更新
			if (sysColumn.getJdbcType()  != fieldParam.getColumnJdbcType()) {
				if (sysColumn.getJdbcType().canAlterTo(fieldParam.getColumnJdbcType())) {
					// 1.验证类型
					modifyFieldList.add(fieldParam);
				}else {
					throw new IllegalArgumentException("Can not change jdbcType ["+sysColumn.getJdbcType()+"] to ["+fieldParam.getColumnJdbcType()+"]");
				}
				continue;
			}

			// 5.验证自增
			if ("auto_increment".equals(sysColumn.getExtra()) && !fieldParam.isFieldIsAutoIncrement()) {
				modifyFieldList.add(fieldParam);
				continue;
			}

			// 6.验证默认值
			if (sysColumn.getColumn_default() == null || sysColumn.getColumn_default().equals("")) {
				// 数据库默认值是null，model中注解设置的默认值不为NULL时，那么需要更新该字段
				if (!"NULL".equals(fieldParam.getFieldDefaultValue())) {
					modifyFieldList.add(fieldParam);
					continue;
				}
			} else if (!sysColumn.getColumn_default().equals(fieldParam.getFieldDefaultValue())) {
				// 两者不相等时，需要更新该字段
				modifyFieldList.add(fieldParam);
				continue;
			}

			// 7.验证是否可以为null(主键不参与是否为null的更新)
			if (sysColumn.getIs_nullable().equals("NO") && !fieldParam.isFieldIsKey()) {
				if (fieldParam.isFieldIsNull()) {
					// 一个是可以一个是不可用，所以需要更新该字段
					modifyFieldList.add(fieldParam);
					continue;
				}
			} else if (sysColumn.getIs_nullable().equals("YES") && !fieldParam.isFieldIsKey()) {
				if (!fieldParam.isFieldIsNull()) {
					// 一个是可以一个是不可用，所以需要更新该字段
					modifyFieldList.add(fieldParam);
					continue;
				}
			}
		}

		modifyFieldList.forEach(f -> this.modifyTableField(new TableAlterParam(table.name(), f, table.splitTable())));
	}

	/**
	 * 根据数据库中表的结构和model中表的结构对比找出新增的字段
	 *
	 * @param clazz  Do class
	 * @param newFieldList model中的结构
	 * @param columnNames  数据库中的结构
	 */
	private void handlerAddFields(Class<?> clazz, List<FieldParam> newFieldList, Set<String> columnNames) {
		Table table = clazz.getAnnotation(Table.class);

		List<FieldParam> addFieldList = newFieldList.stream()
			.filter(f -> ! columnNames.contains(f.getFieldName()))
			.collect(Collectors.toList());

		addFieldList.forEach(fieldParam -> this.addTableFields(new TableAlterParam(table.name(), fieldParam, table.splitTable())));
	}

	/**
	 * 迭代出所有model的所有fields存到newFieldList中
	 *
	 * @param clas                  准备做为创建表依据的class
	 */
	private List<FieldParam> tableFieldsConstruct(Class<?> clas) {
		Field[] fields = clas.getDeclaredFields();
		List<FieldParam> list = new ArrayList<>();
		for (Field field : fields) {
			// 判断方法中是否有指定注解类型的注解
			boolean hasAnnotation = field.isAnnotationPresent(Column.class);
			if (hasAnnotation) {
				// 根据注解类型返回方法的指定类型注解
				Column column = field.getAnnotation(Column.class);
				FieldParam param = new FieldParam();
				param.setFieldName(field.getName());
				ColumnJdbcType columnJdbcType = ColumnJdbcType.parse(field.getType(), column.jdbcType());
				param.setFieldType(columnJdbcType.getJdbcTypeDesc());
				param.setColumnJdbcType(columnJdbcType);
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

				list.add(param);
			}
		}
		return list;
	}

	/**
	 * 根据map结构修改表中的字段类型等
	 *
	 * @param alterParam 用于存需要更新字段类型等的表名+结构
	 */
	private void modifyTableField(TableAlterParam alterParam) {
		logger.info("\n\n========开始修改表" + alterParam.getTableName() + "中的字段" + alterParam.getField().getFieldName());
		createTableService.modifyTableField(alterParam);
		logger.info("\n\n========完成修改表" + alterParam.getTableName() + "中的字段" + alterParam.getField().getFieldName());
	}

	/**
	 * 根据m结构对表中添加新的字段
	 *
	 * @param tableParam 用于存需要增加字段的表名+结构
	 */
	private void addTableFields(TableAlterParam tableParam) {
		// 做增加字段操作
		logger.info("开始为表" + tableParam.getTableName() + "增加字段" + tableParam.getField().getFieldName());
		createTableService.addTableField(tableParam);
		logger.info("完成为表" + tableParam.getTableName() + "增加字段" + tableParam.getField().getFieldName());
	}

	/**
	 * 根据map结构创建表
	 *
	 * @param tableParam 用于存需要创建的表名+结构
	 */
	private void createTable(TableCreateParam tableParam) {
		// 做创建表操作
		logger.info("开始创建表：" + tableParam.getTableName());
		createTableService.createTable(tableParam);
		logger.info("完成创建表：" + tableParam.getTableName());
	}

	@Override
	public void setApplicationContext(IApplicationContext context) {
		context.getTypesAnnotatedWith(Table.class).forEach(this::handlerTable);
	}

	/***
	 * 处理Entity类.
	 * 判断是否新建表,  有字段改动等
	 * @param clazz
	 */
	private void handlerTable(Class<?> clazz){
		Table table = clazz.getAnnotation(Table.class);

		// 迭代出所有model的所有fields存到newFieldList中
		List<FieldParam> newFieldList = tableFieldsConstruct(clazz);

		int tableExist = createTableService.findTableCountByTableName(table.name());
		// 不存在时
		if (tableExist == 0) {
			TableCreateParam tableParam = new TableCreateParam(table.name(), table.comment(), newFieldList, table.splitTable());
			createTable(tableParam);
			//TODO
		}else {
			// 验证对比从model中解析的fieldList与从数据库查出来的columnList
			// 1. 找出增加的字段
			// 3. 找出更新的字段
			handlerAddAndModifyFields(newFieldList, clazz);
		}
	}
}
