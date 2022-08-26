package org.qiunet.profile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.utils.async.LazyLoader;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/***
 * 性能记录的主要类
 *
 * @author qiunet
 * 2020-11-04 11:13
 */
public class Profile<Key, Column extends Enum<Column> & IProColumn> {
	private final LazyLoader<ProfilePrinter<Key, Column>> lazyLoader = new LazyLoader<>(() -> ProfilePrinter.valueOf(this));

	private final Map<Key, ProRowInfo<Key, Column>> rowDatas = Maps.newConcurrentMap();

	private long startDt = System.currentTimeMillis();


	private final  Column[] columns;

	public Profile(Class<Column> cls) {
		this.columns = cls.getEnumConstants();
	}

	Column[] getColumns() {
		return columns;
	}

	List<ProRowInfo<Key, Column>> toList() {
		return Lists.newArrayList(rowDatas.values());
	}

	public void reset(){
		this.startDt = System.currentTimeMillis();
		this.rowDatas.clear();
	}

	public Row createRow(Key key) {
		return new Row(key);
	}

	public void print(PrintStream printer) {
		lazyLoader.get().print(printer);
	}

	public long getStartDt() {
		return startDt;
	}

	public class Row {
		private Key key;
		private long [] values;
		Row(Key key) {
			this.values = new long[columns.length];
			this.key = key;
		}

		public Row add(Column column, long val) {
			values[column.ordinal()] = val;
			return this;
		}

		public Row add1(Column column) {
			return add(column, 1);
		}

		long[] getValues() {
			return values;
		}

		public void submit(){
			rowDatas.computeIfAbsent(key, k -> new ProRowInfo<>(k, columns))
				.record(this);
		}
	}
}
