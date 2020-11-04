package org.qiunet.profile.test;

import org.qiunet.profile.IProColumn;
import org.qiunet.profile.ProStrategy;

/***
 *
 *
 * @author qiunet
 * 2020-11-04 16:45
 */
public enum ProfileType implements IProColumn {
	Test1 {
		@Override
		public ProStrategy getStrategy() {
			return ProStrategy.avg;
		}
	},
	Test2 {
		@Override
		public ProStrategy getStrategy() {
			return ProStrategy.sum;
		}
	},
	;
}
