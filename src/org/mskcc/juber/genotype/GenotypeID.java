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

import java.util.Arrays;

/**
 * @author Juber Patel
 * 
 *         Unique ID of a genotype
 *
 */
public class GenotypeID
{
	public final GenotypeEventType type;
	public final String contig;
	/**
	 * genomic start position of the genotype
	 */
	public final int position;

	/**
	 * genomic end position of the genotype
	 */
	public final int endPosition;

	/**
	 * 
	 * ref.length gives the length of the deletion while alt.length gives the
	 * length of the insertion
	 **/
	public final byte[] ref;
	public final byte[] alt;

	public GenotypeID(GenotypeEventType type, String contig, int position,
			byte[] ref, byte[] alt)
	{
		this.type = type;
		this.contig = contig;
		this.position = position;
		this.ref = ref;
		this.alt = alt;

		// set end position
		if (type == GenotypeEventType.SNV)
		{
			endPosition = position;
		}
		else if (type == GenotypeEventType.MNV)
		{
			endPosition = position + alt.length - 1;
		}
		else if (type == GenotypeEventType.INSERTION)
		{
			// insertion is like a string of bases hanging between 2 reference
			// bases
			endPosition = position + 1;
		}
		else if (type == GenotypeEventType.DELETION)
		{
			endPosition = position + ref.length - 1;
		}
		else
		{
			endPosition = position;
		}
	}

	/**
	 * construct a partial genotype that is contained in the given span on the
	 * same contig, inclusive.
	 * 
	 * start must be <= end
	 * 
	 * @return
	 */
	public GenotypeID partialGenotype(String contig, int start, int end)
	{
		// no overlap at all
		if (!this.contig.equals(contig) || endPosition < start
				|| position > end)
		{
			return null;
		}

		// genotype is fully contained in the given span
		if (contig.equals(this.contig) && start <= this.position
				&& end >= this.endPosition)
		{
			return this;
		}

		start = start > position ? start : position;
		end = end < endPosition ? end : endPosition;

		byte[] nRef = null;
		byte[] nAlt = null;

		if (type == GenotypeEventType.SNV)
		{
			// no change at all
			return this;
		}
		else if (type == GenotypeEventType.MNV)
		{
			nRef = new byte[end - start + 1];
			nAlt = new byte[end - start + 1];
			System.arraycopy(ref, start - position, nRef, 0, nRef.length);
			System.arraycopy(alt, start - position, nAlt, 0, nAlt.length);

			if (start == end)
			{
				return new GenotypeID(GenotypeEventType.SNV, contig, start,
						nRef, nAlt);
			}
			else
			{
				return new GenotypeID(type, contig, start, nRef, nAlt);
			}

		}
		else if (type == GenotypeEventType.DELETION)
		{
			// 2 special cases of single base overlap. These complications have
			// to be handled because of the way we represent deletion.
			if (start == end && start == position)
			{
				// only the preceding base is covered, no overlap
				return null;
			}
			else if (start == end && start == endPosition)
			{
				// the last deleted base overlaps with the given region
				// this deletion would be recorded for the read because of how
				// we are parsing deletions. So start one base back.
				start--;
			}

			// eg.
			// GTCC - ref
			// G - alt

			nRef = new byte[end - start + 1];
			nAlt = new byte[1];

			System.arraycopy(ref, start - position, nRef, 0, nRef.length);
			nAlt[0] = nRef[0];

			return new GenotypeID(type, contig, start, nRef, nAlt);

		}
		else if (type == GenotypeEventType.INSERTION)
		{
			// not doing partials for insertions
			return null;
		}

		return null;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(alt);
		result = prime * result + ((contig == null) ? 0 : contig.hashCode());
		result = prime * result + position;
		result = prime * result + Arrays.hashCode(ref);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		GenotypeID other = (GenotypeID) obj;

		// check type
		if (type != other.type)
			return false;

		// check contig
		if (contig == null)
		{
			if (other.contig != null)
				return false;
		}
		else if (!contig.equals(other.contig))
			return false;

		// check position
		if (position != other.position)
			return false;

		// check alt
		if (!Arrays.equals(alt, other.alt))
			return false;

		// check ref
		if (!Arrays.equals(ref, other.ref))
			return false;

		return true;
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(contig).append("\t");
		builder.append(position).append("\t");

		for (int i = 0; i < ref.length; i++)
		{
			builder.append((char) ref[i]);
		}

		builder.append("\t");

		for (int i = 0; i < alt.length; i++)
		{
			builder.append((char) alt[i]);
		}

		builder.append("\t");

		builder.append(type);
		return builder.toString();
	}

}
