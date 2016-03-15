//package EXPERIMENT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
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

	private static final int analyzeWindowSize = 500;
	
	public OTTS findOutBestSimilarity(ArrayList<OTTS> otts) {
		
		OTTS bestOTTS = otts.iterator().next();
		
		for (OTTS ots : otts) {
			if (ots.similarity < bestOTTS.similarity) {
				bestOTTS = ots;
			}
		}
		
		return bestOTTS;
	}
	
	public List<Double> reverseData(Data data) {
		
		Iterator<Double> iteratorCurrentData = data.data.values().iterator();
		List<Double> temp = new ArrayList<Double>();
		
		while (iteratorCurrentData.hasNext()) {
			temp.add(iteratorCurrentData.next()); 
		}
		
		Collections.reverse(temp);
		
		return temp;
	}
	
	public SortedMap<Long, Double> calculateFuture(Long timestamp, ConcurrentMap<Long, Double> loadedData) {
		
		Iterator<Double> CPULoad = loadedData.values().iterator(); 
		Iterator<Long> TS = loadedData.keySet().iterator();	
		SortedMap<Long, Double> future = new TreeMap<Long, Double>();
		
		Long futureTime = System.currentTimeMillis();
		
		while (CPULoad.hasNext()) {
			Long nextTS = TS.next();
			CPULoad.next();
			if (timestamp.equals(nextTS)) {
				for (int i = 0; i < analyzeWindowSize; i++) {
					futureTime = futureTime + new Long(run.executionInterval);
					future.put(futureTime, CPULoad.next());
				}
				return future;
			}
		}
		return null;
	}
	
	public SortedMap<Long, Double> analyze(Data data) {
		
		List<Double> reversedData = reverseData(data);
		
		ConcurrentMap<Long, Double> loadedData = Data.load("./data/CPUData.ser");
		Long timestamp = 0L;
		OTTS bestOTTS = null;
		
		if (data.data.size() >= analyzeWindowSize && loadedData.size() >= analyzeWindowSize*2) {
			
			ArrayList<OTTS> otts = new ArrayList<OTTS>();
			int offset=0;
			for (int j = 0; j < analyzeWindowSize; j++) {
				List<Double> similarityValueList = new ArrayList<Double>();
				
				Iterator<Double> iteratorLoadedData = loadedData.values().iterator(); //T�h�n vaikuttaa offset
				Iterator<Long> iteratorLoadedDataTS = loadedData.keySet().iterator(); //ja t�h�n
				
				for (int i = 0; i < offset; i++) { //Skip values from loadedData according to offset
					iteratorLoadedData.next();
					iteratorLoadedDataTS.next();
				}
				for (int i = 0; i < analyzeWindowSize; i++){
					 similarityValueList.add(Math.abs(iteratorLoadedData.next() - reversedData.get(i)));
					 timestamp = iteratorLoadedDataTS.next();
				}
				Double similarity = 0.0;
				for (Double d : similarityValueList) {
					similarity = similarity + d;
				}
				offset++;
				otts.add(new OTTS(offset, timestamp ,similarity));
			}
			bestOTTS = findOutBestSimilarity(otts);
			System.out.println("BesTOTTS timestamp: " + bestOTTS.timestamp + " BestOTTS similarity: " + bestOTTS.similarity + " BestOTTS offset: " + bestOTTS.offset);
			
		} else {
			System.out.println("not enough data");
			return null;
		}
		
		return calculateFuture(bestOTTS.timestamp, data.data);
	}
}

