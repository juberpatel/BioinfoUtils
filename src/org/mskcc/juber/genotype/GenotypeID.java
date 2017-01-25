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
	 * genomic position
	 */
	public final int position;

	/**
	 * ref is null for insertion and alt is null for deletion
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
