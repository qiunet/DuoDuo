package org.qiunet.function.test.attr.level;

import org.qiunet.function.attr.manager.IAttrTreeInit;
import org.qiunet.function.attr.tree.AttrTree;

/***
 *
 *
 * @author qiunet
 * 2020-11-20 17:19
 */
public enum LevelService implements IAttrTreeInit {
	instance;

	@Override
	public AttrTree buildAttrTree() {
		return AttrTree.newBuilder(LevelAttrNode.LEVEL_ROOT).build();
	}
}
