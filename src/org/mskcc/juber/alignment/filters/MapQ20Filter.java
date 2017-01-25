/**
 * 
 */
package org.mskcc.juber.alignment.filters;

import htsjdk.samtools.SAMRecord;

/**
 * @author Juber Patel
 *
 */
public class MapQ20Filter implements AlignmentFilter
{
	private static final int minMappingQuality = 20;

	@Override
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
