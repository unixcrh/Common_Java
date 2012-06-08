package com.orange.common.statemachine.test;

import org.junit.Test;

import com.orange.common.statemachine.State;
import com.orange.common.statemachine.StateMachine;




public class TestStateMachine {
	@Test
    public void testBuildStateMachine() {
		MyStateMachineBuilder builder = new MyStateMachineBuilder();
		StateMachine sm = builder.buildStateMachine();
		
		State currentState = sm.getStartState();
		
		String context = "test context 1";
		
		currentState = sm.nextState(currentState, 
				new MyStateMachineBuilder.MyEvent(MyStateMachineBuilder.MyEventKey.EVENT_GAME_CREATE),
				context);
		
		context = "test context 2";

		currentState = sm.nextState(currentState, 
				new MyStateMachineBuilder.MyEvent(MyStateMachineBuilder.MyEventKey.EVENT_GAME_UNKNOWN),
				context);
		
		context = "test context 3";

		currentState = sm.nextState(currentState, 
				new MyStateMachineBuilder.MyEvent(MyStateMachineBuilder.MyEventKey.EVENT_GAME_COMPLETE),
				context);
    }
}
