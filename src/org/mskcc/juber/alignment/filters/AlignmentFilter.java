/**
 * 
 */
package org.mskcc.juber.alignment.filters;

import htsjdk.samtools.SAMRecord;

/**
 * @author Juber Patel
 *
 */
public interface AlignmentFilter
{
	public boolean isGoodAlignment(SAMRecord record);
}
