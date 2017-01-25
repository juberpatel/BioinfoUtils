/*******************************************************************************
 *
 * @author Juber Patel
 *
 * Copyright (c) 2017 Innovation Lab, CMO, MSKCC.
 *
 * This software was developed at the Innovation Lab, Center for Molecular Oncology, 
 * Memorial Sloan Kettering Cancer Center, New York, New York.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
