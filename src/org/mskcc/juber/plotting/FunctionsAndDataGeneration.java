package org.mskcc.juber.plotting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.FastMath;

public class FunctionsAndDataGeneration
{

	public static final double leakyReluConstant = 0.01D;

	public static double sigmoid(double a)
	{
		return 1.0D / (1.0D + FastMath.exp(-a));
	}

	public static double tanh(double a)
	{
		return FastMath.tanh(a);
	}

	public static double relu(double a)
	{
		return FastMath.max(0D, a);
	}

	public static double leakyRelu(double a)
	{
		return FastMath.max(leakyReluConstant * a, a);
	}

	/**
	 * in terms of sigmoid!
	 * 
	 * @param sigmoid
	 * @return
	 */
	public static double sigmoidPrime(double sigmoid)
	{
		return sigmoid * (1.0D - sigmoid);
	}

	/**
	 * in terms of tanh!
	 * 
	 * @param a
	 * @return
	 */
	public static double tanhPrime(double tanh)
	{
		return 1.0D - (tanh * tanh);
	}

	/**
	 * in terms of relu!
	 * 
	 * @param a
	 * @return
	 */
	public static double reluPrime(double relu)
	{
		return relu < 0 ? 0 : 1D;
	}

	/**
	 * in terms of leakyRelu!
	 * 
	 * @param a
	 * @return
	 */
	public static double leakyReluPrime(double leakyRelu)
	{
		return leakyRelu < 0 ? leakyReluConstant : 1D;
	}

	public static void main(String[] args) throws IOException
	{

	}

	/**
	 * make data points with the given function f(x) = y, start, end and step
	 * 
	 * @param f
	 * @param start
	 * @param end
	 * @param step
	 * @return
	 */
	public static double[][] makeData(Function<Double, Double> f, double xStart,
			double xEnd, double step)
	{
		List<Double> xValues = new ArrayList<Double>();
		List<Double> yValues = new ArrayList<Double>();

		for (double x = xStart; x <= xEnd; x += step)
		{
			double y = f.apply(x);

			if (y != Double.NaN)
			{
				xValues.add(x);
				yValues.add(y);
			}
		}

		double[][] xy = new double[2][xValues.size()];
		for (int j = 0; j < xy[0].length; j++)
		{
			xy[0][j] = xValues.get(j);
			xy[1][j] = yValues.get(j);

		}

		return xy;
	}

	/**
	 * make data points with the given a distribution, start, end and step
	 * 
	 * @param f
	 * @param start
	 * @param end
	 * @param step
	 * @return
	 */
	public static double[][] makeData(AbstractRealDistribution distribution,
			double xStart, double xEnd, double step)
	{
		List<Double> xValues = new ArrayList<Double>();
		List<Double> yValues = new ArrayList<Double>();

		for (double x = xStart; x <= xEnd; x += step)
		{
			double y = distribution.density(x);

			if (y != Double.NaN)
			{
				xValues.add(x);
				yValues.add(y);
			}
		}

		double[][] xy = new double[2][xValues.size()];
		for (int j = 0; j < xy[0].length; j++)
		{
			xy[0][j] = xValues.get(j);
			xy[1][j] = yValues.get(j);

		}

		return xy;
	}

	public static double[] getGaussian(int num, double mean, double sd)
	{

		// Create (and possibly seed) a PRNG (could use any of the CM-provided
		// generators).
		long seed = 17399225432L; // Fixed seed means same results every time

		// TODO decide how you want to generate random numbers!!

		RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

		double[] values = new double[num];
		for (int i = 0; i < values.length; i++)
		{
			values[i] = randomDataGenerator.nextGaussian(mean, sd);
		}

		return values;
	}

}
