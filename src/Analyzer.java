//package EXPERIMENT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;

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

	private static final int analyzeWindowSize = 50;
	
	private Vector<Integer> hits = new Vector<>();
	
	public OTTS findOutBestSimilarity(ArrayList<OTTS> otts) {
		
		OTTS bestOTTS = otts.iterator().next();
		
		for (OTTS ots : otts) {
			if (ots.similarity < bestOTTS.similarity) {
				bestOTTS = ots;
			}
		}
		
		return bestOTTS;
	}
	
	public SortedMap<Long, Double> analyze(Data data) {

		ConcurrentMap<Long, Double> loadedData = Data.load("./data/CPUData.ser");
		
		System.out.println(loadedData.size());
		System.out.println(data.data.size());
		
		if (data.data.size() >= analyzeWindowSize && loadedData.size() >= analyzeWindowSize*2) {
			
			ArrayList<OTTS> otts = new ArrayList<OTTS>();
			
			int offset=0;
			
			for (int j = 0; j < analyzeWindowSize; j++) {
				Long timestamp = 0L;
				
				List<Double> similarityValueList = new ArrayList<Double>();
				
				Iterator<Double> iteratorLoadedData = loadedData.values().iterator(); //Tähän vaikuttaa offset
				Iterator<Long> iteratorLoadedDataTS = loadedData.keySet().iterator();
				Iterator<Double> iteratorCurrentData = data.data.values().iterator();
				
				for (int i = 0; i < offset; i++) { //Skip values from loadedData according to offset
					iteratorLoadedData.next();
					iteratorLoadedDataTS.next();
				}
				
				for (int i = 0; i < analyzeWindowSize - 5; i++){
					 similarityValueList.add(Math.abs(iteratorLoadedData.next() - iteratorCurrentData.next()));
					 timestamp = iteratorLoadedDataTS.next();
				}
				
				Double similarity = 0.0;
				
				for (Double d : similarityValueList) {
					similarity = similarity + d;
				}
				offset++;
				otts.add(new OTTS(offset, timestamp ,similarity));
			}
			
			
			for (OTTS ots : otts) {
				System.out.println(ots.offset);
				System.out.println(ots.similarity);
				System.out.println(ots.timestamp);
			}
			
			OTTS bestOTTS = findOutBestSimilarity(otts);
			System.out.println("BesTOTTS timestamp: " + bestOTTS.timestamp + " BestOTTS similarity: " + bestOTTS.similarity + " BestOTTS offset: " + bestOTTS.offset);
			
		} else {
			System.out.println("not enough data");
		}
		
		
		//Tähän pitäisi sitten vielä laittaa että hakee mapista mikä oli paras offset, ja sitten sen offsetin mukaan hakee tietokannan arraystä sopivat arvot.
		
		SortedMap future = new TreeMap<Long, Double>();
		
		for(int i = 0; i < 100; i++){
			double d;
			if(data.data.size() < 100) d = 5; else d = data.data.values().iterator().next();
			future.put(System.currentTimeMillis() + i*1000, d+(Math.random()*0.1f));
		}
		return future;
	}
	
	
}

