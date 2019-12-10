/**
 * 
 */
package org.mskcc.juber.util;

import java.io.IOException;

import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.util.FastMath;
import org.mskcc.juber.plotting.XYPlot;

/**
 * @author Juber Patel
 *
 */
public class Weibull
{

	public static double[] estimateParametersMLE(double[] x)
	{
		// BrentSolver solver = new BrentSolver(1e-100);
		BisectionSolver solver = new BisectionSolver(1e-10);
		// IllinoisSolver solver = new IllinoisSolver();
		WeibullShapeMLEFunction function = new WeibullShapeMLEFunction(x);
		double shape = solver.solve(10000000, function, 1e-10, 1000000);

		// from method of moments
		// double scale = StatUtils.mean(y) / Gamma.gamma(1 / shape + 1);

		// calculate scale
		double scale = 0;
		for (int i = 0; i < x.length; i++)
		{
			scale += FastMath.pow(x[i], shape);
		}

		scale = scale / x.length;
		scale = FastMath.pow(scale, 1 / shape);

		return new double[] { shape, scale };
	}

	public static void main(String[] args) throws IOException
	{
		double shape = 1;
		double scale = 1;
		WeibullDistribution weibullOriginal = new WeibullDistribution(shape,
				scale);

		double[] x = weibullOriginal.sample(100);

		
		// estiamte Weibull parameters from data points and make new
		// distribution
		double[] params = estimateParametersMLE(x);
		WeibullDistribution weibullEstimated = new WeibullDistribution(
				params[0], params[1]);

		// compare distributions

		System.out.println("Shape: " + params[0] + "; Scale: " + params[1]);
		
		XYPlot plot = new XYPlot("Plot");
		plot.addDistribution(
				"Weibull-original (shape: " + shape + "; scale: " + scale + ")",
				weibullOriginal, 0, 2.5);
		plot.addDistribution("Weibull-estimated", weibullEstimated, 0, 2.5);
		//plot.display();
		plot.saveAsJPG("weibull-2.jpg");

	}
}
