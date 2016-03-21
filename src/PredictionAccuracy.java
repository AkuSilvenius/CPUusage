import java.util.ArrayList;
import java.util.SortedMap;

public class PredictionAccuracy implements Runnable {

	Data oldData;
	SortedMap<Long, Double> futureData;
	
	public PredictionAccuracy(Data oldData, SortedMap<Long, Double> futureData) {
		this.oldData = oldData;
		this.futureData = futureData;
	}
	
	public void run() {
		try {
			Thread.sleep(run.executionInterval * run.analyzeWindowSize); //Wait until the prediction has been fulfilled.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ArrayList<Double> oldDataList = new ArrayList<>(); 
		oldDataList.addAll(oldData.data.values());
		
		ArrayList<Double> futureDataList = new ArrayList<>();
		futureDataList.addAll(futureData.values());
		
		Double accuracySum = 0.0;
		
		int i = 0;
		for (Double futureCPU : futureDataList) {
			accuracySum = accuracySum + (1 - Math.abs(futureCPU - oldDataList.get(oldDataList.size() - i - 1)));
			i++;
		}
		System.out.println("accuracy: " + accuracySum/run.analyzeWindowSize*100); //How to get this number to UI?
	}
}