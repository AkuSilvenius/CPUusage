import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {
 
	private static final int graphHeight = 400;
	private static final int graphVOffset = 400;
	private float dataScaler = 0.5f;
	
	int maxX = 0;
	
	private Data data;
	private SortedMap<Long, Double> future;
	
	public GraphPanel(Data data) {
		this.data = data;
		Timer myTimer = new Timer();
		
		myTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				try {
					GraphPanel.this.repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 25);
	}
	
	public void setDataScale(float scale){
		this.dataScaler = scale;
	}
	
	public void setFuture(SortedMap<Long, Double> future) {
		this.future = future;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.black);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int lx = 0;
		int ly = 0;
		
		Long t0 = data.data.keySet().iterator().next();
		
		g2.setColor(Color.red);
		for(Long ts : data.data.keySet()){
			Double c = data.data.get(ts);
			int x = (int)((ts - t0)*dataScaler);
			int y = (int)(c*graphHeight);
			g2.drawLine(lx, graphVOffset-ly, x, graphVOffset-y);
			ly = y;
			lx = x;
			//If we draw wider than widest, make wider <3
			if(x > maxX){
				maxX = x;
				setNewWidth();
			}
		}
		
		if(future != null){
			lx = (int) ((future.keySet().iterator().next()-t0)*dataScaler);
			g2.setColor(Color.orange);
			for(Long tsf : future.keySet()){
				Double c = future.get(tsf);
				int x = (int)((tsf - t0)*dataScaler);
				int y = (int)(c*graphHeight);
				g2.drawLine(lx, graphVOffset-ly, x, graphVOffset-y);
				ly = y;
				lx = x;
				//If we draw wider than widest, make wider <3
				if(x > maxX){
					maxX = x;
					setNewWidth();
				}
			}
		}
	
	}

	private void setNewWidth() {
		GraphPanel.this.setPreferredSize(new Dimension(maxX, graphHeight));
		this.getParent().repaint();
	}
	
}
