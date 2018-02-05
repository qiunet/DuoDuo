package org.qiunet.utils.nonSyncQuene;

/**
 * @author qiunet
 *         Created on 17/3/14 11:46.
 */
public class IndexElement implements QueueElement {

	private int index;

	public IndexElement (int index) {
		this.index = index;
	}

	@Override
	public boolean handler() {
		String name = Thread.currentThread().getName();
		int nameIndex = Integer.parseInt(name);
		// 这里的2 是指
		if (nameIndex != index % TestIndexNonSyncQueue.THREAD_COUNT) {
			throw new IllegalStateException("nameIndex ["+nameIndex+"] index ["+index+"]");
		}
		return true;
	}

	@Override
	public String toStr() {
		return Thread.currentThread().getName();
	}
}
