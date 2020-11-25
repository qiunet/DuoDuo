package org.qiunet.function.attr.tree;

import com.google.common.collect.Maps;
import org.qiunet.function.attr.AttrValue;
import org.qiunet.function.attr.buff.IAttrBuff;
import org.qiunet.function.attr.buff.IAttrNodeBuff;
import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.listener.observer.IObserver;
import org.qiunet.listener.observer.Observer;
import org.qiunet.listener.observer.ObserverSupport;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/***
 * 属性树根节点包装对象 以及真实的属性数据.
 *
 * @author qiunet
 * 2020-11-16 10:01
 */
public class AttrBox<Attr extends Enum<Attr> & IAttrEnum<Attr>> {
	private static final AttrValue EMPTY_VALUE = new AttrValue();
	Lock readLock;
	/**
	 * 锁
	 */
	Lock writeLock;
	/**
	 * 根树
	 */
	private AttrTree rootTree;
	/**
	 * 观察者
	 */
	private ObserverSupport observer = new ObserverSupport();
	/**
	 * 总的属性
	 */
	private Map<Attr, AttrValue> totalAttrs = Maps.newConcurrentMap();
	/**
	 * 路径上的数据
	 */
	Map<AttrRoad, AttrRoadContent<Attr>> roadContentMap = Maps.newConcurrentMap();

	AttrBox(AttrTree rootTree) {
		this.rootTree = rootTree;
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		this.writeLock = lock.writeLock();
		this.readLock = lock.readLock();
	}

	/**
	 * 得到观察者.
	 * @return
	 */
	public ObserverSupport getObserver() {
		return observer;
	}

	/**
	 * 挂载观察者.
	 * @param clazz
	 * @param o
	 * @param <O>
	 * @return
	 */
	public <O extends IObserver> Observer<O> attach(Class<O> clazz, O o) {
		return getObserver().attach(clazz, o);
	}

	/**
	 * 替换路径的属性
	 * @param road 路径
	 * @param attrs 属性
	 */
	public void replace(AttrRoad road, Map<Attr, Long> attrs) {
		if (! roadContentMap.containsKey(road) && attrs == null) {
			return;
		}

		AttrRoadContent<Attr> roadContent = roadContentMap.computeIfAbsent(road, key -> new AttrRoadContent<>(this, key));
		roadContent.replace(attrs);
	}

	/**
	 * 替换buff
	 * @param road 路径
	 * @param buffType buff类型
	 * @param nodeBuff buff数据
	 */
	public void replace(AttrRoad road, IAttrBuff buffType, IAttrNodeBuff<Attr, ?> nodeBuff) {
		if (! roadContentMap.containsKey(road) && nodeBuff == null) {
			return;
		}

		AttrRoadContent<Attr> roadContent = roadContentMap.computeIfAbsent(road, key -> new AttrRoadContent<>(this, key));
		roadContent.replaceBuff(buffType, nodeBuff);
	}

	/**
	 * 获得指定路径的数据
	 * @param road 路径
	 * @return
	 */
	AttrRoadContent<Attr> getRoadContent(AttrRoad road) {
		return roadContentMap.get(road);
	}

	/**
	 * 获得对应的属性值
	 * @param attr
	 * @return
	 */
	public long get(Attr attr) {
		return totalAttrs.getOrDefault(attr, EMPTY_VALUE).getTotalVal();
	}

	/**
	 * 根据路径获取路径对应的总属性
	 * @return
	 */
	public Map<Attr, Long> getRoadFinalAttrs(AttrRoad road) {
		AttrRoadContent<Attr> content = roadContentMap.get(road);
		if (content != null) {
			return content.getFinalAttrMap();
		}
		return Collections.emptyMap();
	}

	/**
	 * 根据路径获取路径对应的总属性
	 * @return
	 */
	public Map<Attr, Long> getRoadBaseAttrs(AttrRoad road) {
		AttrRoadContent<Attr> content = roadContentMap.get(road);
		if (content != null) {
			return content.getBaseAttrMap();
		}
		return Collections.emptyMap();
	}

	/**
	 * 获得总属性map
	 * @return
	 */
	public Map<Attr, Long> getTotalAttrs() {
		return this.getAllAttrValues().entrySet().stream().collect(
			Collectors.toMap(Map.Entry::getKey, en -> en.getValue().getTotalVal())
		);
	}
	/**
	 * 获得总属性value map
	 * @return
	 */
	Map<Attr, AttrValue> getAllAttrValues(){
		return totalAttrs;
	}

	/**
	 * 清理指定的节点.
	 * @param node 节点
	 */
	public void clearByAttrNode(AttrNode node) {
		writeLock.lock();
		try {
			roadContentMap.forEach((road, content) -> {
				if (node.isParentOfNode(road.getNode())) {
					content.replace(null);
				}
			});
		}finally {
			writeLock.unlock();
		}
	}

	/**
	 * 清理 指定的road
	 * @param road 路径
	 */
	public void clearByAttrRoad(AttrRoad road) {
		this.clearByAttrNode(road.getNode());
	}
}
