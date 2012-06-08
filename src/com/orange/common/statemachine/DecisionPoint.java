package com.orange.common.statemachine;

public class DecisionPoint {
	
	protected final Condition condition;
	
	public DecisionPoint(Condition c){
		super();		
		this.condition = c;
	}

	public Object decideNextState(Object context){
		System.out.println("################ You Must Override <decideNextState> method ###########");
		return 0;
	}
}
