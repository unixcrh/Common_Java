package com.orange.common.statemachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class StateMachine {
	
	static Logger log = Logger.getLogger(StateMachine.class.getName());

	Map<Object, State> stateMap = new HashMap<Object, State>();
	State startState;
	State finalState;
	State currentState;		
	
	public State addState(State state){		
		stateMap.put(state.getKey(), state);
		state.setStateMachine(this);
		return state; 
	}
	
	public void fireEvent(Event event){
		handleEvent(event);
	}
	
	public void fireEvent(Object eventKey){
		handleEvent(new Event(eventKey));
	}

	public void handleEvent(Event event){
		currentState.exitAction(event);
		Object nextStateKey = currentState.nextState(event);
		if (nextStateKey == null){
			// TODO next state for event not found			
			log.warn("<handleEvent> next state for (" + currentState.getKey() + ") event(" 
					+ event + ") not found!");
		}
		else{
			State nextState = stateMap.get(nextStateKey);	
			if (nextState == null){
				// TODO state not found by key
				log.warn("<handleEvent> state " + nextStateKey + " not found!");
			}
			else{
				log.info("<handleEvent> " + currentState.getKey() + " -- " 
						+ event + " --> " + nextState.getKey());
				currentState = nextState;
				currentState.enterAction(event);
			}
		}
	}
	
	public boolean setStartAndFinalState(Object startStateKey, Object finalStateKey){
		this.startState = stateMap.get(startStateKey);
		this.finalState = stateMap.get(finalStateKey);
		this.currentState = startState;
		if (startState == null || finalState == null){
			log.error("Set start/final state for state machine, but state not found by key " 
					+ startStateKey + ", " + finalStateKey);
			return false;
		}
		else{
			return true;
		}
	}
	
	public void printStateMachine(){
		Set<Object> keySet = stateMap.keySet();
		for (Object stateKey : keySet){
			State state = stateMap.get(stateKey);
			log.info(state.getKey());
			state.printTransition();			
		}		
	}
}
