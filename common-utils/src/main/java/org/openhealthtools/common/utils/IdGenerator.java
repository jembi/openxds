package org.openhealthtools.common.utils;

import java.util.Random;

public class IdGenerator {
	
	private static IdGenerator instance = null;
	
	public IdGenerator(){
		
	}
	public synchronized static IdGenerator getInstance() {
	        if (instance == null) {
	            instance = new IdGenerator();
	        }
	        return instance;
	    }
	 public String createId() {
		   Random random =new Random();
		   String id = Integer.toString(random.nextInt());
	       return id;
	 }  

}
