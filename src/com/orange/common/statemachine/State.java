package com.orange.common.statemachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class State {
	
	public static Logger log = Logger.getLogger(StateMachine.class.getName());

	Object stateKey;	// in general, Object shall be an enum key
	StateMachine stateMachine;
	
	Map<Object, Object> transitionMap = new HashMap<Object, Object>();
	
	public State(Object stateId){
		this.stateKey = stateId;
	}	
	
	public State addTransition(Object eventKey, Object nextStateKey){
		transitionMap.put(eventKey, nextStateKey);
		return this;
	}

	public void exitAction(Event event) {
		// this method is to be override
		log.info("<" + event.getKey() + "> exit state action" );
	}
	
	public void enterAction(Event event) {
		// this method is to be override
		log.info("<" + event.getKey() + "> enter state action" );
	}

	public Object nextState(Event event) {
		Object stateKey = transitionMap.get(event.getKey());
		return stateKey;
	}

	public Object getKey() {		
		return this.stateKey;
	}

	public void printTransition() {
		Set<Object> keySet = transitionMap.keySet();
		for (Object event : keySet){
			log.info("  " + event + " --> " + transitionMap.get(event));
		}
	}

	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;		
	}
	
	public StateMachine getStateMachine(){
		return this.stateMachine;
	}
	
	public String toString(){
		return stateKey.toString();
	}

	
}
