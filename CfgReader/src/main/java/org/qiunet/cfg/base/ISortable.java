package org.qiunet.cfg.base;

/***
 * 定义排序规则
 *
 * @author qiunet
 * 2022/8/31 17:57
 */
public interface ISortable<ID, Cfg extends ICfg<ID>> extends Comparable<Cfg> {
}
