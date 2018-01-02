/*******************************************************************************
 *
 * @author Juber Patel
 *
 *         Copyright (c) 2017 Innovation Lab, CMO, MSKCC.
 *
 *         This software was developed at the Innovation Lab, Center for
 *         Molecular Oncology,
 *         Memorial Sloan Kettering Cancer Center, New York, New York.
 *
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 *******************************************************************************/
/**
 * 
 */
package org.mskcc.juber.genotype;

/**
 * @author Juber Patel
 * 
 *         A simple struct-like class to hold a genotype and supporting
 *         information.
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

	public Genotype(String name)
	{
		this.name = name;
		this.id = null;
	}

	public String toString()
	{
		String idString = null;
		if (id == null)
		{
			// contig position ref alt type
			idString = "-\t-\t-\t-\tCOMPOSITE";

		}
		else
		{
			idString = id.toString();
		}

		return idString + "\t" + name + "\t" + totalSupportingCoverage + "\t"
				+ totalCoverage + "\t" + uniqueSupportingCoverage + "\t"
				+ uniqueCoverage;
	}

	public String toString(GenotypeID genotypeID, String genotypeName)
	{
		return genotypeID.toString() + "\t" + genotypeName + "\t"
				+ totalSupportingCoverage + "\t" + totalCoverage + "\t"
				+ uniqueSupportingCoverage + "\t" + uniqueCoverage;
	}

}
