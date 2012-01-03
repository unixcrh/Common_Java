package com.orange.common.statemachine;

public class Event {

	Object eventKey; // in general this is an enum Object
	
	public Event(Object eventKey){
		this.eventKey = eventKey;
	}
	
	public Object getKey(){
		return eventKey;
	}
	
	public String toString(){
		return eventKey.toString();
	}
}
