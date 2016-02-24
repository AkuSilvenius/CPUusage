//package EXPERIMENT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;

/**
 * CPU usage monitoring and prediction
 * @author Aku Silvenius
 * @version 1.0 
 * 
 * This class includes the analyis part of the program as well as
 * the visualization of the data.
 * 
 */

public class Analyzer extends JFrame {

	private Data data;
	
	public Analyzer(Data data) {
		this.data = data;
		
		//Analyysiosa
		
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		g = (Graphics2D)g;
		g.setColor(Color.red);
		g.fillRect(40, 40, 3, 3);
	}
	
}

