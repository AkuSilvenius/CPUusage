//package EXPERIMENT;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * CPU usage monitoring and prediction
 * @author Aku Silvenius
 * @version: 1.0
 * 
 * Handles the data coming from the class CPUusage.
 * Save() method serializes the information into the disk
 * Load() method loads the information to make the analysis possible
 */

public class Data implements Serializable {

	// has to be Concurrent in order to serialize
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
