//package EXPERIMENT;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Data implements Serializable {

	public ConcurrentLinkedDeque<Integer> timestamp = new ConcurrentLinkedDeque<Integer>();
	public ConcurrentLinkedDeque<Double> cpu = new ConcurrentLinkedDeque<Double>();
	
	public static void save(Data d, String file){
		
		//Serialisoi levylle
		
	}

	public static Data load(String file){
		return null;
		
		//lataa ja palauta d tai null jos ei löydy
		
	}
	
}
