package com.orange.common.statemachine.test;

import com.orange.common.statemachine.Condition;

public class TestDecisions {
	
	public static class Condition1 implements Condition{

		@Override
		public int decide(Object context) {
			// TODO Auto-generated method stub
			return 1;
		}
		
	}

	public static class Condition2 implements Condition{

		@Override
		public int decide(Object context) {
			// TODO Auto-generated method stub
			return 2;
		}
		
	}

	public static class Condition3 implements Condition{

		@Override
		public int decide(Object context) {
			// TODO Auto-generated method stub
			return 3;
		}
		
	}
	
}
