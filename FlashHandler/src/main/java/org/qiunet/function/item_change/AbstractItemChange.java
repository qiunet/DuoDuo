package org.qiunet.function.item_change;

import org.qiunet.cfg.base.ICfgDelayLoadData;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.Collections;
import java.util.List;

public abstract class AbstractItemChange<
	C extends AbstractItemChange<C, Obj, Context, Element>,
	Obj extends IThreadSafe & IPlayer,
	Context extends ItemChangeContext<Obj>,
	Element extends ItemChangeElement<Element, Context>>
	implements ICfgDelayLoadData {

	protected List<Element> elements;

	private boolean unmodifiable;

	public AbstractItemChange(List<Element> elements) {
		this.elements = elements;
	}

	protected List<Element> elements() {
		return elements;
	}

	/**
	 * 添加一个元素到变更列表中
	 * @param element
	 */
	public void add(Element element) {
		if (unmodifiable) {
			throw new IllegalStateException("Cannot add element to unmodifiable ItemChange.");
		}

		for (Element e : elements()) {
			if (e.canMerge(element)) {
				e.doMerge(element);
				return;
			}
		}
		elements().add(element.copy());
	}

	public C multi(int multi) {
		if (multi <= 0) {
			throw new IllegalArgumentException("Multi must be greater than 0");
		}
		C instance = this.newInstance();
		for (Element element : this.elements()) {
			instance.add(element.copy(multi));
		}
		return instance;
	}

	protected abstract C newInstance();

	protected abstract Context newContext(ItemChangeVerifyParams<Obj> params);
	/**
	 * 获取元素列表数量
	 * @return
	 */
	public int size() {
		if (elements == null) {
			return 0;
		}
		return elements().size();
	}

	/**
	 * 检查 并执行.
	 * @param params
	 * @return
	 */
	public Context verifyAndAct(ItemChangeVerifyParams<Obj> params) {
		Context context = verify(params);
		context.failThrowExceptionOrAct();
		return context;
	}
	/**
	 * 检查元素列表
	 * @param params 参数
	 * @return 上下文
	 */
	public Context verify(ItemChangeVerifyParams<Obj> params) {
		if (! params.getObj().inSelfThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		Context context = newContext(params);
		for (Element element : elements()) {
			StatusResult result = element.verify(context);
			if (result .isFail()) {
				context.status = result;
				break;
			}
		}
		return context;
	}
	/**
	 * 获取元素列表
	 * @return
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 *
	 */
	public void setUnmodifiable() {
		this.elements = Collections.unmodifiableList(elements());
		for (Element element : this.elements()) {
			element.setUnmodifiable();
		}
		this.unmodifiable = true;
	}

	public void act(Context context) {
		if (! context.getObj().inSelfThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		for (Element element : this.elements()) {
			element.act(context);
		}
	}

	@Override
	public void loadData() {

	}
}
