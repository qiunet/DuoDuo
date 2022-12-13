package org.qiunet.utils.test.listener;

import org.qiunet.utils.listener.event.IListenerEvent;

public record LoginEvent(long uid) implements IListenerEvent {


}
