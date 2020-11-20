package org.qiunet.function.attr.manager;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.attr.tree.AttrBox;
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
public enum AttrManager0 implements IApplicationContextAware {
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

	<Attr extends Enum<Attr> & IAttrEnum<Attr>> AttrBox<Attr> buildAttrBox() {
		return attrTree.buildAttrBox();
	}

	public enum AttrRoot implements IAttrNodeType {
		ROOT(null, "Root");
		private Class<?> keyClass;
		private String desc;

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
	 * 打印attrTree
	 */
	void printAttrTree(){
		attrTree.printTree();
	}
}
