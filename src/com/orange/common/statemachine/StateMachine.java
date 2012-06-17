package com.orange.common.statemachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class StateMachine {
	
	static Logger log = Logger.getLogger(StateMachine.class.getName());

	Map<Object, State> stateMap = new HashMap<Object, State>();
	State startState;
	State finalState;
//	State currentState;		
	
	
	public State addState(State state){		
		stateMap.put(state.getKey(), state);
//		state.setStateMachine(this);
		return state; 
	}
		
	public State getStartState() {
		return startState;
	}

	public void setStartState(State startState) {
		this.startState = startState;
	}

	public State getFinalState() {
		return finalState;
	}

	public void setFinalState(State finalState) {
		this.finalState = finalState;
	}

	public void fireEvent(Event event){
		handleEvent(event);
	}
	
	public void fireEvent(Object eventKey){
		handleEvent(new Event(eventKey));
	}

	public State nextState(State currentState, Event event, Object context){
		return handleEvent(currentState, event, context);
	}
	
	public State nextState(State currentState, Object eventKey, Object context){
		return handleEvent(currentState, new Event(eventKey), context);
	}
	
	public State handleEvent(State currentState, Event event, Object context){
		if (currentState == null){
			log.warn("<handleEvent> but current state is null?");
			return null;
		}
				
		String id = context.toString();
				
		if (currentState.isEventInEmptyTransition(event.getKey())){
			log.info(id + " <handleEvent> " + currentState.getKey() + " -- " 
					+ event.getKey() + " --> [STAY CURRENT]");
			return currentState;
		}
		
		Object nextStateKey = currentState.nextState(event);
		if (nextStateKey == null){
			// TODO next state for event not found			
			log.warn(id + " <handleEvent> next state for (" + currentState.getKey() + ") event(" 
					+ event.getKey() + ") not found!");
			
			return null;
		}
		
		boolean drivenByEvent = true;
		while (true){			
				State nextState = stateMap.get(nextStateKey);	
				if (nextState == null){
					// TODO state not found by key
					log.warn(id + " <handleEvent> state " + nextStateKey + " not found!");
					return null;
				}
				else{
					if (drivenByEvent){
						log.info(id + " <handleEvent> " + currentState.getKey() + " -- " 
								+ event + " --> " + nextState.getKey());
					}
					else{
						log.info(id + " <handleEvent> " + currentState.getKey() + " -- Decision --> " + nextState.getKey());						
					}
	
					// execute post actions
					currentState.exitAction(event, context);
					executeActions(currentState.postActionList, context, id);
					
					currentState = nextState;
	
					// execute pre actions
					executeActions(currentState.preActionList, context, id);
					currentState.enterAction(event, context);
					
					// check decision points
					if (currentState.decisionPoint != null){
						nextStateKey = currentState.decisionPoint.decideNextState(context);
						if (nextStateKey == null){
							return nextState;
						}
						else{
//							log.info(id + "<handleEvent> goto next state "+ nextStateKey + " by decision");
							drivenByEvent = false;
						}
					}
					else{
						return nextState;
					}
				
//				return nextState;
			}
		}
	}
	
	private void executeActions(List<Action> actionList, Object context, String id) {
		if (actionList == null)
			return;
		
		for (Action a : actionList){
			log.info(id + " <executeAction> " + a.getClass().getSimpleName());
			a.execute(context);
		}
	}

	private void handleEvent(Event event){
//		currentState = handleEvent(currentState, event);
	}
	
	public boolean setStartAndFinalState(Object startStateKey, Object finalStateKey){
		this.startState = stateMap.get(startStateKey);
		this.finalState = stateMap.get(finalStateKey);
//		this.currentState = startState;
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
