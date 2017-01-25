/**
 * 
 */
package org.mskcc.juber.util;

/**
 * @author Juber Patel
 * 
 *         A simple mutable class representing a genomic interval on a
 *         chromosome
 *
 */
public class Interval
{
	public final String contig;
	public int start;
	public int end;

	public Interval(String contig)
	{
		this.contig = contig;
	}

}
