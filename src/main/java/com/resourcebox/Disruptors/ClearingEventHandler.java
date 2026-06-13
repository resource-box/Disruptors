package com.resourcebox.Disruptors;

public class ClearingEventHandler<T> extends Handler<T> {

	@Override
	protected void process(Event<T> event) {
		event.clear();
	}

}