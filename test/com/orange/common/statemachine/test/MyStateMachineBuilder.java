package com.orange.common.statemachine.test;

import com.orange.common.statemachine.Event;
import com.orange.common.statemachine.State;
import com.orange.common.statemachine.StateMachine;
import com.orange.common.statemachine.StateMachineBuilder;

public class MyStateMachineBuilder extends StateMachineBuilder {

	public enum MyStateKey {
		STATE_GAME_INIT (1),
		STATE_GAME_WAIT (2),
		STATE_GAME_SUSPEND (3),
		STATE_GAME_ONGOING (4),
		STATE_GAME_FINISH (5);
		
		final int value;
		
		MyStateKey(int value){
			this.value = value;
		}
	}
	
	public enum MyEventKey {
		EVENT_GAME_CREATE(1),
		EVENT_GAME_START(2),
		EVENT_GAME_COMPLETE(3),
		EVENT_GAME_TERMINATE(4),
		EVENT_GAME_PAUSE(5);
		
		final int value;
		
		MyEventKey(int value){
			this.value = value;
		}	
	}
	
	public static class MyEvent extends Event {
		public MyEvent(Object eventKey){
			super(eventKey);
		}
	}
	
	
	@Override
	public StateMachine buildStateMachine() {
		StateMachine sm = new MyStateMachine();
		
		sm.addState(new State(MyStateKey.STATE_GAME_INIT)).
			addTransition(MyEventKey.EVENT_GAME_CREATE, MyStateKey.STATE_GAME_WAIT);
			
		sm.addState(new MyState(MyStateKey.STATE_GAME_WAIT)).
			addTransition(MyEventKey.EVENT_GAME_START, MyStateKey.STATE_GAME_ONGOING).
			addTransition(MyEventKey.EVENT_GAME_TERMINATE, MyStateKey.STATE_GAME_FINISH);
		
		sm.addState(new MyState(MyStateKey.STATE_GAME_ONGOING)).
			addTransition(MyEventKey.EVENT_GAME_COMPLETE, MyStateKey.STATE_GAME_FINISH).
			addTransition(MyEventKey.EVENT_GAME_TERMINATE, MyStateKey.STATE_GAME_FINISH);

		sm.addState(new State(MyStateKey.STATE_GAME_FINISH));
		
		sm.setStartAndFinalState(MyStateKey.STATE_GAME_INIT, MyStateKey.STATE_GAME_FINISH);
		
		sm.printStateMachine();		
		return sm;
	}

}
