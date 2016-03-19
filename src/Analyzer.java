//package EXPERIMENT;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;

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
	private static final int maximumOffsetFromPresent = 1000; //How far do we analyze into the past and how far we predict into the future.
	private static final int numAverages = 5;

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

	//TODO: This will crash if "Guess future" is pressed too many times in a second as the files are still open from the last time and threading and such.
	public SortedMap<Long, Double> analyze(Data data) {

		if(!(new File(run.oldDataPath)).exists()){
			System.out.println("No data saved yet. Not gonna guess anything.");
			return null;
		}
		
		Long startingTimestamp = System.currentTimeMillis();
		
		Data oldData = Data.loadClass(run.oldDataPath);

		ArrayList<Double> nearPastDataValues = new ArrayList<>();
		nearPastDataValues.addAll(data.data.values());
		ArrayList<Long> nearPastDataTimes = new ArrayList<>();
		nearPastDataTimes.addAll(data.data.keySet());
		
		ArrayList<Double> loadedDataValues = new ArrayList<>(); 
		loadedDataValues.addAll(oldData.data.values());
		ArrayList<Long> loadedDataTimes = new ArrayList<>();
		loadedDataTimes.addAll(oldData.data.keySet());

		ArrayList<OTTS> bestOTTSs = null;

		if (loadedDataValues.size() >= maximumOffsetFromPresent && nearPastDataValues.size() >= analyzeWindowSize) {

			ArrayList<OTTS> ottsCandidates = new ArrayList<OTTS>();

			for (int offset = 0; offset < maximumOffsetFromPresent; offset++) {
				Long timestamp = loadedDataTimes.get(loadedDataTimes.size()-1-analyzeWindowSize-offset);
				Double similarity = 0.0;
				for (int i = 0; i < analyzeWindowSize; i++){
					//This made sense when it was made <3 Don't alter much.
					similarity += Math.abs(loadedDataValues.get(loadedDataValues.size()-1-analyzeWindowSize-offset+i) - nearPastDataValues.get(i));
				}
				//Offset is most probably useless here.
				ottsCandidates.add(new OTTS(offset, timestamp ,similarity));
			}

			bestOTTSs = findOutBestSimilarities(ottsCandidates, numAverages);
			
			ConcurrentMap<Long, Double> guessedData = new ConcurrentSkipListMap<>();

			for(int i=0; i < numAverages; i++){
				OTTS otts = bestOTTSs.get(i);
				oldData.data.forEach(new BiConsumer<Long, Double>() {
					boolean record = false;
					int j=0;
					@Override
					public void accept(Long t, Double u) {
						if(j >= analyzeWindowSize){
							return;
						} else if(t.equals(otts.timestamp)){
							record = true;
							System.out.println("kissat kissat");
						}
						if(record){
							Long targetTimestamp = startingTimestamp+j*150;
							double avg = (1/(double)numAverages)*u;
							if(guessedData.containsKey(targetTimestamp)){
								avg += guessedData.get(targetTimestamp);
							}
							guessedData.put(targetTimestamp, avg);
							j++;
						}
					}
				});
			}

			//System.out.println("BesTOTTS timestamp: " + bestOTTS.timestamp + " BestOTTS similarity: " + bestOTTS.similarity + " BestOTTS offset: " + bestOTTS.offset);
			System.out.println("Analyzed something: " + guessedData.size());
			return calculateFuture(startingTimestamp, guessedData);
		} else {
			double neededRunTime = (double) ( (double) analyzeWindowSize * ((double) run.executionInterval/1000))*2;
			System.out.println("not enough data. Software needs to run for: " + neededRunTime + " seconds. Reduce analyzeWindowSize to reduce time.");
			return null;
		}


	}
}

