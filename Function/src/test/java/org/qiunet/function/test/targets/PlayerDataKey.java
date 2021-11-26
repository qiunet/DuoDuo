package org.qiunet.function.test.targets;

import org.qiunet.function.targets.TargetContainer;
import org.qiunet.utils.args.ArgumentKey;

/***
 *
 * @author qiunet
 * 2021/11/26 14:20
 */
public interface PlayerDataKey {

	ArgumentKey<TargetContainer<TargetType>> targetContainer = new ArgumentKey<>();
}
