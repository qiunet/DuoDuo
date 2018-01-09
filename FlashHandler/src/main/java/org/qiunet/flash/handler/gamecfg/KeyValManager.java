package org.qiunet.flash.handler.gamecfg;

import org.qiunet.utils.collection.safe.SafeHashMap;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.data.KeyValueData;

import java.io.DataInputStream;

/***
 * 读取一个key value的结构
 * Key Val 主要是基本类型. 也可以枚举.可以与基本类型转换的那种数据格式
 *
 */
public abstract class KeyValManager<Key, Val> extends BaseGameCfgManager implements IKeyValueData<Key, Val> {
	private KeyValueData<Key, Val> keyValueData;

	protected void loadVars(String filePath)throws Exception{
		SafeHashMap<Key, Val> vars = new SafeHashMap<>();
		int num = loadXdFileToDataInputStream(filePath);
		for(int i = 0 ; i < num; i++){
			vars.put(readKey(dis), readVal(dis));
		}
		vars.safeLock();
		this.keyValueData = new KeyValueData<>(vars);
	}

	protected abstract Key readKey(DataInputStream dis) throws Exception;
	protected abstract Val readVal(DataInputStream dis) throws Exception;

	@Override
	public boolean containKey(Key key) {
		return keyValueData.containKey(key);
	}

	@Override
	public Val getValue(Key key) {
		return keyValueData.getValue(key);
	}

	@Override
	public String getString(Key key, String defaultVal) {
		return keyValueData.getString(key, defaultVal);
	}

	@Override
	public String getString(Key key) {
		return keyValueData.getString(key);
	}

	@Override
	public short getShort(Key key, int defaultVal) {
		return keyValueData.getShort(key, defaultVal);
	}

	@Override
	public short getShort(Key key) {
		return keyValueData.getShort(key);
	}

	@Override
	public int getInt(Key key, int defaultVal) {
		return keyValueData.getInt(key, defaultVal);
	}

	@Override
	public int getInt(Key key) {
		return keyValueData.getInt(key);
	}

	@Override
	public byte getByte(Key key, int defaultVal) {
		return keyValueData.getByte(key, defaultVal);
	}

	@Override
	public byte getByte(Key key) {
		return keyValueData.getByte(key);
	}

	@Override
	public float getFloat(Key key, float defaultVal) {
		return keyValueData.getFloat(key, defaultVal);
	}

	@Override
	public float getFloat(Key key) {
		return keyValueData.getFloat(key);
	}

	@Override
	public long getLong(Key key, long defaultVal) {
		return keyValueData.getLong(key, defaultVal);
	}

	@Override
	public long getLong(Key key) {
		return keyValueData.getLong(key);
	}

	@Override
	public double getDouble(Key key, double defaultVal) {
		return keyValueData.getDouble(key, defaultVal);
	}

	@Override
	public double getDouble(Key key) {
		return keyValueData.getDouble(key);
	}
}
