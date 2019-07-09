/**
 * 
 */
package org.mskcc.juber.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Juber Patel
 *
 */
public class FindHotspotsInNormals
{

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		// file with sample name, tumor/normal and pileup path, tab separated
		// use unfiltered pileups for normals, duplex pileups for tumor
		File pileupList = new File(args[0]);

		File hotspotsFile = new File(args[1]);

		// map of mutation -> mutation name
		Map<String, String> hotspots = new HashMap<String, String>();

		// maps of mutation -> (sample -> count) in normals and tumors
		Map<String, Map<String, Integer[]>> normalHotspots = new LinkedHashMap<String, Map<String, Integer[]>>();
		Map<String, Map<String, Integer[]>> tumorHotspots = new LinkedHashMap<String, Map<String, Integer[]>>();

		// load hotspots

		BufferedReader reader = new BufferedReader(
				new FileReader(hotspotsFile));
		String line = reader.readLine();
		String[] tokens = null;

		while ((line = reader.readLine()) != null)
		{
			tokens = line.split("\t");

			// only look for point mutations
			if (!tokens[2].equals(tokens[3]) || tokens[4].length() > 1
					|| tokens[5].length() > 1)
			{
				continue;
			}

			String mutation = tokens[1].trim() + "\t" + tokens[2].trim() + "\t"
					+ tokens[4].trim() + "\t" + tokens[5].trim();
			String name = tokens[0].trim() + "." + tokens[6].trim();

			hotspots.put(mutation, name);
		}

		reader.close();

		// populate hotspots found in normals from normal pileups
		getSampleHotspots(hotspots.keySet(), pileupList, normalHotspots,
				"Normal");

		// then query and populate those hotspots in tumors
		getSampleHotspots(normalHotspots.keySet(), pileupList, tumorHotspots,
				"Tumor");

		// write output
		BufferedWriter writer = new BufferedWriter(
				new FileWriter("hotspots-in-normals.txt"));
		writer.write("Mutation\tSample\tSampleType\tAltCount\tTotal\tAF\n");

		printTable(normalHotspots, hotspots, "Normal", writer);
		printTable(tumorHotspots, hotspots, "Tumor", writer);

		writer.close();

	}

	private static void printTable(
			Map<String, Map<String, Integer[]>> hotspotCounts,
			Map<String, String> hotspots, String sampleType,
			BufferedWriter writer) throws IOException
	{
		DecimalFormat df = new DecimalFormat("#.####");
		for (String mutation : hotspotCounts.keySet())
		{
			Map<String, Integer[]> counts = hotspotCounts.get(mutation);
			for (String sample : counts.keySet())
			{
				Integer[] pair = counts.get(sample);
				double af = 0;
				if (pair[1] != 0)
				{
					af = (pair[0] * 1.0) / pair[1];
				}

				String mutationName = hotspots.get(mutation);

				writer.write(mutationName + "\t" + sample + "\t" + sampleType
						+ "\t" + pair[0] + "\t" + pair[1] + "\t" + df.format(af)
						+ "\n");
			}
		}
	}

	private static void getSampleHotspots(Set<String> hotspots, File pileupList,
			Map<String, Map<String, Integer[]>> hotspotCounts, String type)
			throws NumberFormatException, IOException
	{
		char[] alleles = new char[] { 'A', 'C', 'G', 'T' };

		String line = null;
		String[] tokens = null;
		BufferedReader reader = new BufferedReader(new FileReader(pileupList));

		// read the pileup list
		while ((line = reader.readLine()) != null)
		{
			tokens = line.split("\t");

			// only look at specified type
			if (!tokens[1].equals(type))
			{
				continue;
			}

			String sample = tokens[0];
			BufferedReader pileupReader = new BufferedReader(
					new FileReader(tokens[2]));
			String pileupLine = null;
			String[] pileupTokens = null;

			// read the current pileup file
			while ((pileupLine = pileupReader.readLine()) != null)
			{
				pileupTokens = pileupLine.split("\t");

				char ref = pileupTokens[2].charAt(0);
				int total = Integer.parseInt(pileupTokens[4])
						+ Integer.parseInt(pileupTokens[5])
						+ Integer.parseInt(pileupTokens[6])
						+ Integer.parseInt(pileupTokens[7])
						+ Integer.parseInt(pileupTokens[9]);

				// check 3 alt alleles for hotspot designation
				for (int i = 0; i < alleles.length; i++)
				{
					if (alleles[i] == ref)
					{
						continue;
					}

					String mutation = pileupTokens[0] + "\t" + pileupTokens[1]
							+ "\t" + ref + "\t" + alleles[i];
					int altCount = Integer.parseInt(pileupTokens[i + 4]);

					if (hotspots.contains(mutation) && altCount >= 3)
					{
						Map<String, Integer[]> counts = hotspotCounts
								.get(mutation);

						if (counts == null)
						{
							counts = new TreeMap<String, Integer[]>();
							hotspotCounts.put(mutation, counts);
						}

						counts.put(sample, new Integer[] { altCount, total });
					}
				}
			}

			pileupReader.close();

		}

		reader.close();
	}

}
