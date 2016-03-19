
//package EXPERIMENT;

import java.io.*;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * CPU usage monitoring and prediction
 * 
 * @author Aku Silvenius
 * @version: 1.0
 * 
 *           Handles the data coming from the class CPUusage. Save() method
 *           serializes the information into the disk Load() method loads the
 *           information to make the analysis possible
 */

public class Data implements Serializable {
	
	public ConcurrentMap<Long, Double> data;
	
	public Data() {
		data = new ConcurrentSkipListMap<>();
	}

	public static void save(Data d, String filePath) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(d);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /data/data.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	/*
	public static ConcurrentMap<Long, Double> load(String filePath) {

		// lataa ja palauta d tai null jos ei loydy

		Data d = null;
		
		try {
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			d = (Data) in.readObject();
			in.close();
			fileIn.close();

		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.out.println("Data class not found");
			c.printStackTrace();
			return null;
		} 
		 
		return d.data;

	}
	*/

	public static Data loadClass(String filePath) {

		// lataa ja palauta d tai null jos ei l�ydy

		Data d = null;

		try {
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			d = (Data) in.readObject();
			in.close();
			fileIn.close();

			//System.out.println("d.cpu: " + d.cpu);
			return d;

		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.out.println("Data class not found");
			c.printStackTrace();
			return null;
		}

	}
	
}
