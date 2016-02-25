//package EXPERIMENT;

import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * CPU usage monitoring and prediction
 * @author Aku Silvenius
 * @version 1.0
 * 
 * This class uses two Timers to get System CPU information and save the information.
 * myTimer takes CPU load in .5s intervals and stores them into a data structure.
 * myTimer2 calculates the average load for the past minute and saves it using the Save() method in class Data.
 * 
 */

public class CPUusage {

	Data data;
	String dataPath = "./data/CPUData.ser";
	
	public CPUusage(Data d) {
		
		this.data = d;

		Timer myTimer2 = new Timer();
		Timer myTimer = new Timer();
		
		myTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				try {
					System.out.println(getSystemCpuLoad());
					data.cpu.add(getSystemCpuLoad());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 500); //executed in 0.5s intervals
		
		myTimer2.schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					Calendar now = Calendar.getInstance();
					if (now.get(Calendar.MINUTE) < 10) {
						System.out.println("Time: " + now.get(Calendar.HOUR_OF_DAY) + ":0" +  now.get(Calendar.MINUTE));
						System.out.println("CPU (%): " + data.cpu.getLast());
					} else {
						System.out.println("Time: " + now.get(Calendar.HOUR_OF_DAY) + ":" +  now.get(Calendar.MINUTE));
						System.out.println("CPU (%): " + data.cpu.getLast());
					}
					if (now.get(Calendar.MINUTE) == 00) {
						double x = 0.0;
						while (!data.cpu.isEmpty()) {
							x += (double)data.cpu.poll();
						}
						x = x / data.cpu.size();
//						int hour = now.get(Calendar.HOUR_OF_DAY);
					}
					
					Data.save(data, dataPath);  //Data.save(data, run.DATAFILE);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 5000); //executed in 1min intervals
		
	}

	public static double getSystemCpuLoad() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
		AttributeList list = mbs.getAttributes(name, new String[]{"SystemCpuLoad"});

		if (list.isEmpty())
			return Double.NaN;

		Attribute att = (Attribute)list.get(0);
		Double value = (Double)att.getValue();

		return ((int)(value*1000)/10.0);
	}
	
}