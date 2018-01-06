package org.qiunet.flash.handler.gamecfg;

import org.qiunet.utils.collection.safe.SafeHashMap;

import java.util.HashMap;
import java.util.Map;

/***
 * 读取一个key value的结构
 */
public abstract class KeyValManager extends BaseGameCfgManager {
	private Map<Integer, String> map = new HashMap<>();

	protected void loadVars(String filePath)throws Exception{
		SafeHashMap<Integer, String> vars = new SafeHashMap<>();
		int num = loadXdFileToDataInputStream(filePath);
		for(int i = 0 ; i < num; i++){
			vars.put(dis.readInt(), dis.readUTF());
		}
		vars.safeLock();
		this.map = vars;
	}

	public String getValue(int id){
		return map.get(id);
	}

	public int getIntValue(int id){
		return Integer.parseInt(getValue(id));
	}

	public float getFloatValue(int id){
		return Float.parseFloat(getValue(id));
	}

	public double getDoubleValue(int id){
		return Double.parseDouble(getValue(id));
	}

	public short getShortValue(int id){
		return Short.parseShort(getValue(id));
	}

	public byte getByteValue(int id){
		return Byte.parseByte(getValue(id));
	}
}
