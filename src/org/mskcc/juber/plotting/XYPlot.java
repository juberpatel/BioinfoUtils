/**
 * 
 */
package org.mskcc.juber.plotting;

import static org.mskcc.juber.plotting.FunctionsAndDataGeneration.makeData;
import static org.mskcc.juber.util.Constants.plotHeight;
import static org.mskcc.juber.util.Constants.plotWidth;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Function;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.TextAlignment;

/**
 * @author Juber Patel
 *
 */
public class XYPlot
{
	private XYChart chart;

	public XYPlot()
	{
		chart = new XYChartBuilder().width(plotWidth).height(plotHeight)
				.xAxisTitle("X").yAxisTitle("Y").build();
		setup();

	}

	public XYPlot(String title)
	{
		chart = new XYChartBuilder().width(plotWidth).height(plotHeight)
				.title(title).xAxisTitle("X").yAxisTitle("Y").build();
		setup();
	}

	private void setup()
	{
		// Customize Chart
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		chart.getStyler().setMarkerSize(2);
		chart.getStyler().setYAxisDecimalPattern("#,###.##");
		chart.getStyler().setYAxisLabelAlignment(TextAlignment.Right);
		chart.getStyler().setPlotMargin(0);
		chart.getStyler().setPlotContentSize(.95);
	}

	public void setToScatter()
	{
		chart.getStyler()
				.setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
		chart.getStyler().setMarkerSize(10);
	}

	public void addData(String name, double[] x, double[] y)
	{
		if (x == null)
		{
			chart.addSeries(name, x, y);
		}
		else
		{
			chart.addSeries(name, y);
		}
	}

	public void addFunction(String name, Function<Double, Double> f,
			double xStart, double xEnd)
	{
		double step = 0.01;
		double[][] xy = makeData(f, xStart, xEnd, step);
		chart.addSeries(name, xy[0], xy[1]);
	}

	public void addDistribution(String name,
			AbstractRealDistribution distribution, double xStart, double xEnd)
	{
		double step = 0.01;
		double[][] xy = makeData(distribution, xStart, xEnd, step);
		chart.addSeries(name, xy[0], xy[1]);
	}

	public void display()
	{
		new SwingWrapper(chart).displayChart();
	}

	public void saveAsJPG(String path) throws IOException
	{
		BitmapEncoder.saveBitmapWithDPI(chart, path, BitmapFormat.JPG, 500);
	}

	public void saveAsPDF(String path) throws IOException
	{
		BufferedImage image = BitmapEncoder.getBufferedImage(chart);

		PDDocument doc = new PDDocument();
		PDPage page = new PDPage(new PDRectangle(plotWidth, plotHeight));
		doc.addPage(page);
		PDImageXObject imageObject = LosslessFactory.createFromImage(doc,
				image);
		PDPageContentStream stream = new PDPageContentStream(doc, page);
		stream.drawImage(imageObject, 0, 0, plotWidth, plotHeight);
		stream.close();

		doc.save(path);
		doc.close();

	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		double shape = 1e-5;
		double scale = .01;

		WeibullDistribution weibull = new WeibullDistribution(shape, scale);

		XYPlot plot = new XYPlot("Plot");
		plot.addDistribution("Weibull", weibull, 0, 2.5);

		// plot.addFunction("sigmoid", FunctionsAndDataGeneration::sigmoid, -8,
		// 8);
		// plot.addFunction("relu", FunctionsAndDataGeneration::relu, -8, 8);
		// plot.addFunction("tanh", FunctionsAndDataGeneration::tanh, -8, 8);
		// plot.addData("Gaussian Blob", getGaussian(1000, 0, 5),
		// getGaussian(1000, 1, 10));
		// plot.setToScatter();

		plot.display();
		// plot.saveAsJPG("XYPlot.jpg");
	}

}
