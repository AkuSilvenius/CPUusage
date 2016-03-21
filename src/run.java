//package EXPERIMENT;

/**
 * CPU usage monitoring and prediction 
 * @author Aku Silvenius
 * @version 1.0
 * 
 * This class executes the program and initiates the data file.
 */

public class run {
	
	
	public static final String dataPath = "./data/CPUData.ser";
	
	public static final int executionInterval = 500; //CPULoad only changes every 0.5 seconds or so.
	public static final int saveInterval = 15000;
	public static final int analyzeWindowSize = 50; //How far do we analyze into the past and how far we predict into the future.
	public static void main(String [] args) throws Exception {

		Data d = Data.loadClass(dataPath);

		if(d==null) d = new Data();
		
		Analyzer a = new Analyzer();
		new CPUusage(d);
		
		new AnalyzerGUI(a, d);

	}
}
