package com.orange.common.statemachine.test;

import com.orange.common.statemachine.Action;

public class TestActions  {

	public static class TestAction1 implements Action{
		@Override
		public void execute(Object context) {
			// TODO Auto-generated method stub
			System.out.println("TestAction1");
		}
		
	}
	
	public static class TestAction2 implements Action{
		@Override
		public void execute(Object context) {
			// TODO Auto-generated method stub
			System.out.println("TestAction2");
		}
		
	}

	public static class TestAction3 implements Action{
		@Override
		public void execute(Object context) {
			// TODO Auto-generated method stub
			System.out.println("TestAction3");
		}
		
	}
	
}
