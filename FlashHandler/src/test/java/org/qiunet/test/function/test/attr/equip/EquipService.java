package org.qiunet.test.function.test.attr.equip;

import org.qiunet.function.attr.manager.IAttrTreeInit;
import org.qiunet.function.attr.tree.AttrTree;
import org.qiunet.test.function.test.attr.AttrBuffType;

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
			.addChildNode(EquipAttrNode.BASE, builder -> {
				builder.addLeaf(EquipAttrNode.EQUIP_BASE_POSITION);
			}, AttrBuffType.allEquipBaseRct)
			.addChildNode(EquipAttrNode.GEM, builder -> {
				builder.addLeaf(EquipAttrNode.GEM_POSITION, AttrBuffType.EquipGemLegRct);
			})
			.build();
	}
}
