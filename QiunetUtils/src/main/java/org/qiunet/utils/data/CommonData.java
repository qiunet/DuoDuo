package org.qiunet.utils.data;
/**
 * 通用的 key val类
 * @author qiunet
 *
 * @param <K>
 * @param <V>
 */
public class CommonData <K, V>{
	private K key;
	private V val;

	public CommonData(K key , V val){
		this.key = key;
		this.val = val;
	}

	public K getKey(){
		return key;
	}

	public V getVal(){
		return val;
	}

	public void setVal(V val){
		this.val = val;
	}
}
