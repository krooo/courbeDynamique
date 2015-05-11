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
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.IOException;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger LOG = LoggerFactory.getLogger(DynamicDataDemo.class);

    private Label temperatureCouranteLabel;

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
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
		setContentPane(content);

        // Zone de saisie de commande vers le port série :
        TextField zoneSaisie = new TextField(10);
        chartPanel.add(zoneSaisie);
        zoneSaisie.setLocation(100, 300);
        zoneSaisie.validate();
        zoneSaisie.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    TextField textField = (TextField) e.getComponent();

                    try {
                        BufferedWriter writer = tempSerialReader.getEcrivainPortSerie();
                        writer.write(textField.getText());
                        writer.newLine();
                        writer.flush();
                    } catch (IOException l_ex) {
                        LOG.error("erreur d'écriture sur le port série: ", l_ex);
                    }
                }
            }
        });

        temperatureCouranteLabel = new Label("XX.X°C");
        chartPanel.add(temperatureCouranteLabel);

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

	public void afficherDerniereTemperature(float floatToPrint) {
		this.series.add(new Millisecond(), floatToPrint);
        temperatureCouranteLabel.setText(floatToPrint + "°C");
	}

}
