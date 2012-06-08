package com.orange.common.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class State {
	
	public static Logger log = Logger.getLogger(StateMachine.class.getName());

	Object stateKey;	// in general, Object shall be an enum key
//	StateMachine stateMachine;
	
//	Object context;		// state running context
	
	Map<Object, Object> transitionMap = new HashMap<Object, Object>();
	List<Action> preActionList = new ArrayList<Action>();
	List<Action> postActionList = new ArrayList<Action>();
	List<Object> emptyTransitionList = new ArrayList<Object>();
	
	DecisionPoint decisionPoint = null;
	
	/*
	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
	}
	*/

	public State(Object stateId){
		this.stateKey = stateId;
	}	
	
	public State addTransition(Object eventKey, Object nextStateKey){
		transitionMap.put(eventKey, nextStateKey);
		return this;
	}
	
	public State addEmptyTransition(Object eventKey){
		emptyTransitionList.add(eventKey);
		return this;
	}
	
	public int validateEvent(Event event, Object context){
		// this method is to be override
		log.info("<" + event.getKey() + "> validate event, wow, you didn't override it yet?" );
		return 0;
	}

	public void exitAction(Event event, Object context) {
		// this method is to be override
		log.debug("<" + event.getKey() + "> exit state action" );
	}
	
	public void enterAction(Event event, Object context) {
		// this method is to be override
		log.info("<" + event.getKey() + "> enter state action, wow, you didn't override it yet?" );
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

//	public void setStateMachine(StateMachine stateMachine) {
//		this.stateMachine = stateMachine;		
//	}
//	
//	public StateMachine getStateMachine(){
//		return this.stateMachine;
//	}
	
	public String toString(){
		return stateKey.toString();
	}

	public State addAction(Action action) {
		if (transitionMap.size() == 0){
			// pre actions
			preActionList.add(action);
		}
		else{
			// post actions
			postActionList.add(action);			
		}
		return this;
	}

	public State setDecisionPoint(DecisionPoint decisionPoint) {		
		this.decisionPoint = decisionPoint;
		return this;
	}

	public boolean isEventInEmptyTransition(Object eventKey) {
		return emptyTransitionList.contains(eventKey);
	}

	
}
