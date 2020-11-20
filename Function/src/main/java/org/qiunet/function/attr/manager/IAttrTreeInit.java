package org.qiunet.function.attr.manager;

import org.qiunet.function.attr.tree.AttrTree;

/***
 * 各个模块建立自己的AttrTree
 *
 * @author qiunet
 * 2020-11-20 15:35
 */
public interface IAttrTreeInit {
	/**
	 * 各模块搞定自己的 tree
	 * @return
	 */
	AttrTree buildAttrTree();
}
