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
package org.mskcc.juber.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Juber Patel
 * 
 *         Convert the UCSC refflat file into an exon bed file that my programs
 *         use.
 *
 */
public class UCSCRefFlatToExonBed
{

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		String refFlatFile = "/Users/patelj1/resources/gene-list/hg19_refFlat.canonical_2013Apr8th";
		String bedFile = refFlatFile + ".bed";

		BufferedReader reader = new BufferedReader(new FileReader(refFlatFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(bedFile));
		String line = null;
		String[] parts = null;

		// ignore first line
		reader.readLine();

		while ((line = reader.readLine()) != null)
		{
			parts = line.split("\t");
			int exonCount = Integer.parseInt(parts[8]);
			String[] exonStarts = parts[9].split(",");
			String[] exonEnds = parts[10].split(",");

			if (exonCount != exonStarts.length
					|| exonStarts.length != exonEnds.length)
			{
				System.err.println(line);
				System.err.println("problem with exon count!!!");
				reader.close();
				writer.close();
				System.exit(0);
			}

			// if strand is negative, flip the exonStarts and exonEnds arrays
			if (parts[3].equals("-"))
			{
				for (int i = 0, j = exonStarts.length - 1; i < j; i++, j--)
				{
					String t = exonStarts[i];
					exonStarts[i] = exonStarts[j];
					exonStarts[j] = t;

					t = exonEnds[i];
					exonEnds[i] = exonEnds[j];
					exonEnds[j] = t;
				}
			}

			// write one line per exon of the gene
			for (int i = 1; i <= exonCount; i++)
			{
				String description = parts[0] + "_exon" + i;
				String chr = parts[2].substring(3);
				writer.write(
						chr + "\t" + exonStarts[i - 1] + "\t" + exonEnds[i - 1]
								+ "\t" + parts[3] + "\t" + description + "\n");
			}

		}

		reader.close();
		writer.close();
	}

}
