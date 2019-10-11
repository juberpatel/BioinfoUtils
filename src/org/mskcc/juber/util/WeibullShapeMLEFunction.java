/**
 * 
 */
package org.mskcc.juber.util;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.util.FastMath;

/**
 * @author Juber Patel
 *
 */
public class WeibullShapeMLEFunction implements UnivariateFunction
{
	private double[] y;
	private final double minPositive = 1e-15;

	public WeibullShapeMLEFunction(double[] y)
	{
		this.y = new double[y.length];
		for (int i = 0; i < y.length; i++)
		{
			// Weibull doesn't work with 0 values. Replace 0's with minPositive
			if (y[i] <= 0)
			{
				this.y[i] = minPositive;
			}
			else
			{
				this.y[i] = y[i];
			}
		}
	}

	@Override
	public double value(double x)
	{
		double a = 0;
		double b = 0;
		double c = 0;
		for (int i = 0; i < y.length; i++)
		{
			double p = FastMath.pow(y[i], x);
			double ln = FastMath.log(y[i]);
			a += (p * ln);
			b += p;
			c += ln;
		}

		return (a / b - 1 / x) - (c / y.length);

	}

}
