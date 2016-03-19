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
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.text.AbstractDocument.BranchElement;

/**
 * CPU usage monitoring and prediction
 * @author Aku Silvenius
 * @version 1.0 
 * 
 * This class includes the analysis part of the program as well as
 * the visualization of the data.
 * 
 */

public class Analyzer {

	private static final int analyzeWindowSize = 500; //How far do we analyze into the past and how far we predict into the future.
	private static final int maximumOffsetFromPresent = 1000;
	private static final int analyzeSourceWindowSize = 2000; //How far do we analyze into the past and how far we predict into the future.
	
	//Takes an OTTS array as an parameter and then return the OTTS with the lowest(best) similarity value.
	public ArrayList<OTTS> findOutBestSimilarities(List<OTTS> otts, int numOTTSs) {
		ArrayList<OTTS> bestOTTSs = new ArrayList<>();
		
		Collections.sort(otts);
		
		bestOTTSs.addAll(otts.subList(0, numOTTSs));
		
		return bestOTTSs;
	}
	
	//Reverses the values from data.data. Only returns the processor load values
	public List<Double> reverseData(Data data) {
		
		Iterator<Double> iteratorCurrentData = data.data.values().iterator();
		List<Double> temp = new ArrayList<Double>();
		
		while (iteratorCurrentData.hasNext()) {
			temp.add(iteratorCurrentData.next()); 
		}
		Collections.reverse(temp);
		
		return temp;
	}
	
	//Takes in the best offsets timestamp and the data from database. Returns a map of predicted timestamps and cpu loads
	public SortedMap<Long, Double> calculateFuture(Long bestOffSetTimestamp, ConcurrentMap<Long, Double> loadedData) {
		
		Iterator<Double> CPULoad = loadedData.values().iterator(); 
		Iterator<Long> TS = loadedData.keySet().iterator();	
		SortedMap<Long, Double> future = new TreeMap<Long, Double>();
		
		Long time = System.currentTimeMillis();
		
		while (CPULoad.hasNext()) {
			Long nextTS = TS.next();
			//CPULoad.next();
			if (bestOffSetTimestamp.equals(nextTS)) {
				for (int i = 0; i < analyzeWindowSize; i++) {
					time = time + new Long(run.executionInterval);
					future.put(time, CPULoad.next()); //CPULoad doesn't always have a next value. "java.util.NoSuchElementException"
				}
				return future;
			}
		}
		return null;
	}
	
	public SortedMap<Long, Double> analyze(Data data) {
		
		//ReverseData because we want to predict from the end of the saved data, not from the beginning. First data vs last data.
		//List<Double> reversedData = reverseData(data);
		
		Double[] nearPastDataValues = (Double[])data.data.values().toArray();
		Long[] nearPastDataTimes = (Long[])data.data.values().toArray();
		
		Data oldData = Data.loadClass(run.oldDataPath);
		Double[] loadedDataValues = (Double[])oldData.data.values().toArray();
		Long[] loadedDataTimes = (Long[])oldData.data.values().toArray();
		
		Long timestamp = 0L;
		ArrayList<OTTS> bestOTTSs = null;
		
		Long bestTimestamp = 0L;
		ConcurrentMap<Long, Double> guessedData = new ConcurrentSkipListMap<>();
		
		if (loadedDataValues.length >= maximumOffsetFromPresent && nearPastDataValues.length >= analyzeWindowSize) {
			
			ArrayList<OTTS> ottsCandidates = new ArrayList<OTTS>();
			for (int offset = 0; offset < maximumOffsetFromPresent; offset++) {
				
				timestamp = loadedDataTimes[offset];
				Double similarity = 0.0;
				for (int i = 0; i < analyzeWindowSize; i++){
					//This made sense when it was made <3 Don't alter much.
					similarity += Math.abs(loadedDataValues[loadedDataValues.length-1-analyzeWindowSize-offset+i] - nearPastDataValues[i]);
				}
				//Offset is most probably useless here.
				ottsCandidates.add(new OTTS(offset, timestamp ,similarity));
			}
			
			bestOTTSs = findOutBestSimilarities(ottsCandidates, 5);
			
			
			
			//TODO: Laske keskiarvot bestOTTSs:tä ja survo keskiarvo guessedData taulukkoon
			
			
			
			//System.out.println("BesTOTTS timestamp: " + bestOTTS.timestamp + " BestOTTS similarity: " + bestOTTS.similarity + " BestOTTS offset: " + bestOTTS.offset);
			
		} else {
			double neededRunTime = (double) ( (double) analyzeWindowSize * ((double) run.executionInterval/1000))*2;
			System.out.println("not enough data. Software needs to run for: " + neededRunTime + " seconds. Reduce analyzeWindowSize to reduce time.");
			return null;
		}
		
		return calculateFuture(bestTimestamp, guessedData);
	}
}

