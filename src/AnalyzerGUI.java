import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AnalyzerGUI extends JFrame {

	private static final Dimension windowSize = new Dimension(800, 600);
	
	private JPanel contentPane;
	private JButton buttonAnalyze;
	private JScrollPane dataScrollPane;
	private JSlider scaleSlider;
	
	private Analyzer analyzer;
	private Data data;

	/**
	 * Create the frame.
	 */
	public AnalyzerGUI(Analyzer analyzer, Data data) {
		this.analyzer = analyzer;
		this.data = data;
		
		GraphPanel gp = new GraphPanel(data);
		
		buttonAnalyze = new JButton();
		dataScrollPane = new JScrollPane();
		
		dataScrollPane.setViewportView(gp);
		dataScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//dataScrollPane.setAutoscrolls(true);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		gp.setScrollPane(dataScrollPane);
		
		buttonAnalyze.setText("Guess future!");

		buttonAnalyze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gp.setFuture(analyzer.analyze(data));
			}
		});
		
		scaleSlider = new JSlider();
		scaleSlider.setMinimum(1);
		scaleSlider.setMaximum(1000);
		scaleSlider.setMajorTickSpacing(100);
		scaleSlider.setMinorTickSpacing(10);
		scaleSlider.setPaintTicks(true);
		scaleSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				gp.setDataScale(scaleSlider.getValue()*0.001f);
			}
		});
        JLabel scaleLabel = new JLabel();
        scaleLabel.setText("DataScaler");
		
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scaleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scaleSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                        .addComponent(buttonAnalyze)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dataScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scaleSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scaleLabel)
                    .addComponent(buttonAnalyze))
                .addContainerGap())
        );

		this.pack();
		this.setMinimumSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}

}
