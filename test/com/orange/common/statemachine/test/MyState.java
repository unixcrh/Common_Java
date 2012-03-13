package com.orange.common.statemachine.test;

import com.orange.common.statemachine.Event;
import com.orange.common.statemachine.State;

public class MyState extends State {

	public MyState(Object stateId) {
		super(stateId);
	}

	@Override
	public void enterAction(Event event, Object context) {
		log.info("Do somthing..., context = " + context.toString());
	}
}
