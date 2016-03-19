
public class OTTS implements Comparable<OTTS>{
	int offset;
	Long timestamp;
	Double similarity;
	
	OTTS (int offset, long timestamp, Double similarity) {
		this.offset = offset;
		this.timestamp = timestamp;
		this.similarity = similarity;
	}

	@Override
	public int compareTo(OTTS o) {
		
		if(this.similarity > o.similarity){
			return 1;
		} else if (this.similarity < o.similarity){
			return -1;
		} else {
			return 0;
		} 
	}
	
}
