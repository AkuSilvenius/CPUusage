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
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		myTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					Double cpu = getSystemCpuLoad();
				//	System.out.println(cpu);
					data.data.put(System.currentTimeMillis(), cpu);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 150); //executed in 0.15s intervals
		
		
		myTimer2.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					Calendar now = Calendar.getInstance();
					System.out.println("Saved data");
					Data.save(data, dataPath);  //Data.save(data, run.DATAFILE);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 15000); //Divide by 1000 to get execution schedule in s
		
	}

	public static double getSystemCpuLoad() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
		AttributeList list = mbs.getAttributes(name, new String[]{"SystemCpuLoad"});

		if (list.isEmpty())
			return Double.NaN;

		Attribute att = (Attribute)list.get(0);
		Double value = (Double)att.getValue();
		return value;
	}
	
}