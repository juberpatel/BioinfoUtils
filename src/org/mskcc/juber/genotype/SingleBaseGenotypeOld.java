/**
 * 
 */
package org.mskcc.juber.genotype;

/**
 * @author Juber Patel
 * 
 *         simple bean class to hold a 'genotype'
 *
 */
public class SingleBaseGenotypeOld
{
	public final String name;
	public final String chr;
	public final int position;
	public final byte ref;
	public final byte alt;
	public long totalCoverage;
	public long totalSupportingCoverage;
	public long uniqueCoverage;
	public long uniqueSupportingCoverage;

	public SingleBaseGenotypeOld(String chr, int position, byte ref, byte alt,
			String name)
	{
		this.chr = chr;
		this.position = position;
		this.ref = ref;
		this.alt = alt;
		this.name = name;
	}

	public String toString()
	{
		return chr + "\t" + position + "\t" + ref + "\t" + alt + "\t" + name
				+ "\t" + totalSupportingCoverage + "\t" + totalCoverage + "\t"
				+ uniqueSupportingCoverage + "\t" + uniqueCoverage;
	}

	public void clear()
	{
		totalCoverage = 0;
		totalSupportingCoverage = 0;
		uniqueCoverage = 0;
		uniqueSupportingCoverage = 0;
	}

}
