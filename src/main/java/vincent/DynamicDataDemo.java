/*
 * =========================================================== JFreeChart : a free chart library for
 * the Java(tm) platform =========================================================== (C) Copyright
 * 2000-2004, by Object Refinery Limited and Contributors. Project Info:
 * http://www.jfree.org/jfreechart/index.html This library is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA. [Java is a
 * trademark or registered trademark of Sun Microsystems, Inc. in the United States and other
 * countries.] -------------------- DynamicDataDemo.java -------------------- (C) Copyright
 * 2002-2004, by Object Refinery Limited. Original Author: David Gilbert (for Object Refinery
 * Limited). Contributor(s): -; $Id: DynamicDataDemo.java,v 1.12 2004/05/07 16:09:03 mungady Exp $
 * Changes ------- 28-Mar-2002 : Version 1 (DG);
 */

package vincent;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import vincent.serial3.TempSerialReader;

/**
 * A demonstration application showing a time series chart where you can dynamically add (random)
 * data by clicking on a button.
 */
public class DynamicDataDemo extends ApplicationFrame implements FloatPrinter {

	private static final long serialVersionUID = 3960766362937292369L;

	/** The time series data. */
	private TimeSeries series;

	private TempSerialReader tempSerialReader;

	/**
	 * Constructs a new demonstration application.
	 * 
	 * @param title the frame title.
	 */
	public DynamicDataDemo(final String title) {

		super(title);
		this.series = new TimeSeries("Random Data");
		final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
		final JFreeChart chart = createChart(dataset);

		final ChartPanel chartPanel = new ChartPanel(chart);

		final JPanel content = new JPanel(new BorderLayout());
		content.add(chartPanel);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(content);

		tempSerialReader = new TempSerialReader(this);
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset the dataset.
	 * @return A sample chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart("", "Temps", "Température", dataset, true, true,
				false);
		final XYPlot plot = result.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(60000.0); // 60 seconds
		axis = plot.getRangeAxis();
		axis.setRange(10.0, 50.0);// 50°C
		return result;
	}

	/**
	 * Starting point for the demonstration application.
	 * 
	 * @param args ignored.
	 */
	public static void main(final String[] args) {
		final DynamicDataDemo demo = new DynamicDataDemo("Dynamic Data Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

	public void windowClosed(WindowEvent event) {
		tempSerialReader.close();
	}

	public void printFloat(float floatToPrint) {
		this.series.add(new Millisecond(), floatToPrint);
	}

}
