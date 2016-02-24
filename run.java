//package EXPERIMENT;

/**
 * CPU usage monitoring and prediction 
 * @author Aku Silvenius
 * @version 1.0
 * 
 * This class executes the program and initiates the data file.
 */

public class run {

	public static final String DATAFILE = "data.dat";
	
	public static void main(String [] args) throws Exception {
		
		Data d = Data.load(DATAFILE);
		
		if(d==null) d = new Data();
		
		new CPUusage(d);
		new Analyzer(d);
	}


	
}
