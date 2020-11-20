package org.qiunet.function.test.attr.equip;

import org.qiunet.function.attr.manager.IAttrTreeInit;
import org.qiunet.function.attr.tree.AttrTree;

/***
 *
 *
 * @author qiunet
 * 2020-11-20 17:08
 */
public enum EquipService implements IAttrTreeInit {
	instance;

	@Override
	public AttrTree buildAttrTree() {
		return AttrTree.newBuilder(EquipAttrNode.EQUIP_ROOT)
			.addLeaf(EquipAttrNode.BASE)
			.addLeaf(EquipAttrNode.GEM)
			.build();
	}
}
