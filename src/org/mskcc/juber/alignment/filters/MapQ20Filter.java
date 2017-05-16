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
package org.mskcc.juber.alignment.filters;

import htsjdk.samtools.SAMRecord;

/**
 * @author Juber Patel
 *
 */
public class MapQ20Filter
{
	private static final int minMappingQuality = 20;

	
	public boolean isGoodAlignment(SAMRecord record)
	{
		// TODO what are we doing with chimeric/supplementary alignments??

		// this gives 100% concordance with igvtools!
		// as opposed to filtering out supplementary and non-primary alignments
		if (record.getReadUnmappedFlag())
		{
			return false;
		}

		if (record.getMappingQuality() < minMappingQuality)
		{
			return false;
		}

		return true;
	}

}
