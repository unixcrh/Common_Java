package com.orange.common.statemachine.test;

import org.junit.Test;

import com.orange.common.statemachine.StateMachine;




public class TestStateMachine {
	@Test
    public void testBuildStateMachine() {
		MyStateMachineBuilder builder = new MyStateMachineBuilder();
		StateMachine sm = builder.buildStateMachine();
		sm.fireEvent(new MyStateMachineBuilder.MyEvent(MyStateMachineBuilder.MyEventKey.EVENT_GAME_CREATE));
		sm.fireEvent(new MyStateMachineBuilder.MyEvent(MyStateMachineBuilder.MyEventKey.EVENT_GAME_START));
    }
}
