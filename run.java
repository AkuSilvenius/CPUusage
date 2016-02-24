//package EXPERIMENT;

public class run {

	public static final String DATAFILE = "data.dat";
	
	public static void main(String [] args) throws Exception {
		
		Data d = Data.load(DATAFILE);
		
		if(d==null) d = new Data();
		
		new CPUusage(d);
//		new Analyzer(d);
	}


	
}
