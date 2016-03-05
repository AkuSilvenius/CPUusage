//package EXPERIMENT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;

/**
 * CPU usage monitoring and prediction
 * @author Aku Silvenius
 * @version 1.0 
 * 
 * This class includes the analyis part of the program as well as
 * the visualization of the data.
 * 
 */

public class Analyzer {

	private static final int analyzeWindowSize = 100;
	
	private Vector<Integer> hits = new Vector<>();
	
	public SortedMap<Long, Double> analyze(Data data) {
		
		SortedMap future = new TreeMap<Long, Double>();
		
		for(int i = 0; i < 100; i++){
			double d;
			if(data.data.size() < 100) d = 5; else d = data.data.values().iterator().next();
			future.put(System.currentTimeMillis() + i*1000, d+(Math.random()*0.1f));
		}
			
		return future;
	
	}
	
}

