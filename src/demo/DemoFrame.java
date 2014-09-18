package demo;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class DemoFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	JFreeChart jfc;
	XYDataset ds;
	ChartPanel cp;
	XYSeries sheepSeries, wolfSeries;
	
	ModelPanel mp;
	
	JButton goButton;
	boolean running = false;
	Timer timer;
	
	public DemoFrame() {
		super("Demo of JFC for Modeling in Levels");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ds = createDataset();
		jfc = createChart(ds);
		cp = new ChartPanel(jfc);
		cp.setPreferredSize( new Dimension(640,460) );
		this.setLayout( new BorderLayout()  );
		this.add(cp, BorderLayout.CENTER );
		
		
		mp = new ModelPanel(460,460);
		mp.openModel("WSP.nlogo");
		mp.runCommand("setup");
		this.add(mp, BorderLayout.EAST );
		
		
		goButton = new JButton("Start running");
		goButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			     if ( running ) {
			    	 goButton.setText("Start running");
			    	 running = false;
			    	 timer.cancel();
			     } else {
			    	 goButton.setText("Stop running");
			    	 running = true;
			    	 timer = new Timer();
				     timer.schedule(new RunOneTick(), 0, 50);
			     }
			}} );
		
		this.add(goButton, BorderLayout.SOUTH );
		
		this.pack();
	}
	
	
	public void goAndHarvestData( ) {
		mp.runCommand("go");
		mp.repaint();
		double t = mp.runReporter("ticks");
		double s = mp.runReporter("count sheep");
		double w = mp.runReporter("count wolves");
		sheepSeries.add(t,s);
		wolfSeries.add(t,w);
	}
	
	
	private XYDataset createDataset() {
        sheepSeries = new XYSeries("sheep");
        wolfSeries = new XYSeries("wolves");
      
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(sheepSeries);
        dataset.addSeries(wolfSeries);
                
        return dataset;   
    }
    
    /**
		Copied from the JFreeChart Demo
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "WSP JFreeChart Demo",      // chart title
            "Time",                      // x axis label
            "Population",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            false,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        //renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }

	
	
    private class RunOneTick extends TimerTask {
        public void run() {
        	goAndHarvestData(); 
        }
     }

   
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DemoFrame bt = new DemoFrame();
		bt.setVisible(true);
	}

}
