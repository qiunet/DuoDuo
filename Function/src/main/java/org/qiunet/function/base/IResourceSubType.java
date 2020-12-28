package org.qiunet.function.base;

import org.qiunet.function.consume.AbstractConsume;
import org.qiunet.function.consume.ConsumeConfig;

/***
 * 资源的子类型枚举.
 *
 * @author qiunet
 * 2020-12-28 11:49
 */
public interface IResourceSubType {
	/**
	 * 根据子类型. 创建对应的消耗实例
	 * @param consumeConfig 消耗配置
	 * @param <T> 消耗实例泛型
	 * @return 消耗实例
	 */
	<T extends AbstractConsume> T createConsume(ConsumeConfig consumeConfig);
}
