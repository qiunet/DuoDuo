package org.qiunet.function.attr.manager;

import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.attr.tree.AttrBox;
import org.qiunet.function.attr.tree.AttrRoad;
import org.qiunet.function.attr.tree.AttrTree;
import org.qiunet.function.attr.tree.IAttrNodeType;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.util.Set;

/***
 *
 *
 * @author qiunet
 * 2020-11-20 15:34
 */
enum AttrManager0 implements IApplicationContextAware {
	instance;

	private AttrTree attrTree;
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		attrTree = AttrTree.newBuilder(AttrRoot.ROOT).build();
		Set<Class<? extends IAttrTreeInit>> classes = context.getSubTypesOf(IAttrTreeInit.class);
		classes.forEach(clz -> {
			attrTree.merge(((IAttrTreeInit) context.getInstanceOfClass(clz)).buildAttrTree());
		});
	}

	<Owner extends AbstractUserActor<Owner>, Attr extends Enum<Attr> & IAttrEnum<Attr>> AttrBox<Owner, Attr> buildAttrBox(Owner owner) {
		return attrTree.buildAttrBox(owner);
	}

	enum AttrRoot implements IAttrNodeType {
		ROOT(null, "Root");
		private final Class<?> keyClass;
		private final String desc;

		AttrRoot(Class<?> keyClass, String desc) {
			this.keyClass = keyClass;
			this.desc = desc;
		}

		@Override
		public Class<?> keyClass() {
			return keyClass;
		}

		@Override
		public String desc() {
			return desc;
		}
	}

	/**
	 * 构建属性路径
	 * @param keys
	 * @return
	 */
	AttrRoad builderRoad(IAttrNodeType nodeType, Object ... keys){
		return attrTree.getNode(nodeType).buildRoad(keys);
	}

	/**
	 * 打印attrTree
	 */
	void printAttrTree(){
		attrTree.printTree();
	}
}
