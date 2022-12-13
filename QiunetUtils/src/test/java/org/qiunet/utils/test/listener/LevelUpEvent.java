package org.qiunet.utils.test.listener;

import org.qiunet.utils.listener.event.IListenerEvent;

public record LevelUpEvent(long uid, int oldLevel, int newLevel) implements IListenerEvent {


}
