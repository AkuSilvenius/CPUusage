//package EXPERIMENT;

import java.lang.management.ManagementFactory;
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
	
	public CPUusage(Data d) {
		
		this.data = d;

		Timer myTimer2 = new Timer();
		Timer myTimer = new Timer();
		
		try {
			Thread.sleep(1000); //Prevent's a bug where the first two CPU loads come off as "-1"
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					Double cpu = getSystemCpuLoad();
					data.data.put(System.currentTimeMillis(), cpu);
					//System.out.println(cpu);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, run.executionInterval); 
		
		
		myTimer2.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					Data.save(data, run.dataPath);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, run.saveInterval); 
		
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