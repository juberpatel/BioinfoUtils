/**
 * 
 */
package org.mskcc.juber.genotype;

/**
 * @author Juber Patel
 * 
 *         A simple struct-like class to hold a genotype and supporting information.
 *         Genotypes could be substitutions, deletions, insertions.
 *
 */
public class Genotype
{
	public final String name;
	public final GenotypeID id;

	/**
	 * total and unique coverage may be 0 depending on the use of this class
	 */
	public int totalCoverage;
	public int totalSupportingCoverage;
	public int uniqueCoverage;
	public int uniqueSupportingCoverage;

	public Genotype(String name, GenotypeID id)
	{
		this.name = name;
		this.id = id;
	}

	public String toString()
	{
		return id.toString() + "\t" + name + "\t" + totalSupportingCoverage
				+ "\t" + totalCoverage + "\t" + uniqueSupportingCoverage + "\t"
				+ uniqueCoverage;
	}

	public String toString(GenotypeID genotypeID, String genotypeName)
	{
		return genotypeID.toString() + "\t" + genotypeName + "\t"
				+ totalSupportingCoverage + "\t" + totalCoverage + "\t"
				+ uniqueSupportingCoverage + "\t" + uniqueCoverage;
	}

}
