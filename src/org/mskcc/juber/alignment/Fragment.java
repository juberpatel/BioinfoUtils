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
package org.mskcc.juber.alignment;

import org.mskcc.juber.util.JuberUtilException;

import htsjdk.samtools.SAMRecord;

/**
 * @author Juber Patel
 * 
 *         a fragment sequenced by the sequencer. Usually represented by read1 +
 *         insert + read2
 *
 */
public class Fragment implements Comparable<Fragment>
{
	private String fragmentName;
	private String leftContig;
	private String rightContig;
	private int start;
	private int end;

	public Fragment(SAMRecord leftRecord)
	{
		fragmentName = leftRecord.getReadName();
		leftContig = leftRecord.getReferenceName();
		start = leftRecord.getAlignmentStart();
	}

	public void addRightRecord(SAMRecord rightRecord) throws JuberUtilException
	{
		if (!fragmentName.equals(rightRecord.getReadName()))
		{
			throw new JuberUtilException(
					"Left and Right read names don't match: " + fragmentName
							+ " and " + rightRecord.getReadName());
		}

		rightContig = rightRecord.getReferenceName();

		// make them point to same string
		if (leftContig.equals(rightContig))
		{
			rightContig = leftContig;
		}

		// end = rightRecord.getUnclippedStart();
		end = rightRecord.getAlignmentEnd();
		// end = rightRecord.getAlignmentStart();
	}

	public boolean hasCompleteInformation()
	{
		return (leftContig == rightContig && start != 0 && end != 0);

	}

	/**
	 * must only be called if {@link #hasCompleteInformation()} is true
	 * 
	 * @return
	 */
	public int getTemplateLength()
	{
		if (leftContig != rightContig)
		{
			return -1;
		}

		return end - start + 1;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result
				+ ((leftContig == null) ? 0 : leftContig.hashCode());
		result = prime * result + start;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fragment other = (Fragment) obj;

		// only implementing equals for when left contig is same as right contig
		if (leftContig != rightContig || other.leftContig != other.rightContig)
			return false;

		// must have contig information
		if (leftContig == null || rightContig == null
				|| other.leftContig == null || other.rightContig == null)
		{
			return false;
		}

		// if (end != other.end)
		// return false;

		// if (start != other.start)
		// return false;

		if (Math.abs(end - other.end) > 2)
		{
			return false;
		}

		if (Math.abs(start - other.start) > 2)
		{
			return false;
		}

		if (!leftContig.equals(other.leftContig))
			return false;

		return true;
	}

	@Override
	public int compareTo(Fragment o)
	{
		if (this.equals(o))
		{
			return 0;
		}

		int result = leftContig.compareTo(o.leftContig);
		if (result == 0)
		{
			result = start - o.start;
		}

		if (result == 0)
		{
			result = end - o.end;
		}

		if (result == 0)
		{
			return -1;
		}
		else
		{
			return result;
		}
	}

	public String toString()
	{
		return fragmentName + "\t" + leftContig + "\t" + start + "\t" + end;
	}

}
