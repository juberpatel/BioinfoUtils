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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Juber Patel
 *
 */
public class ComputeAffectedExonList
{

	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args)
			throws NumberFormatException, IOException
	{
		File intragenicFile = new File(
				"/Users/patelj1/workspace/SV-Testing/filtering/dmp-intragenic.txt");

		BufferedReader reader = new BufferedReader(
				new FileReader(intragenicFile));

		TreeMap<String, Set<String>> patientsByEvent = new TreeMap<String, Set<String>>();

		// read the intragenic file and accumulate patients per event
		String line = reader.readLine();
		while ((line = reader.readLine()) != null)
		{
			// Verify that this entire code is correct !!!!
			
			String[] tokens = line.split("\t");
			String patient = tokens[0].split("-")[1];
			String eventType = tokens[1];
			String gene = tokens[6];
			double num1 = getExonNumber(tokens[8]);
			double num2 = getExonNumber(tokens[9]);

			// put them in order
			if (num1 > num2)
			{
				double t = num1;
				num1 = num2;
				num2 = t;
			}

			// adjust range
			int start = (int) (num1 + 0.6);
			int end = (int) (num2 + 0.1);

			String startString = Integer.toString(start);
			String endString = null;
			if (end == 1000000)
			{
				endString = "";
			}
			else
			{
				endString = Integer.toString(end);
			}

			String range = "[" + startString + ", " + endString + "]";

			String event = gene + "\t" + eventType + "\t" + range;

			Set<String> patients = patientsByEvent.get(event);
			if (patients == null)
			{
				patients = new HashSet<String>();
				patientsByEvent.put(event, patients);
			}

			patients.add(patient);
		}

		reader.close();

		// make event counts map, sorted by count
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for (String event : patientsByEvent.keySet())
		{
			counts.put(event, patientsByEvent.get(event).size());
		}

		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(
				counts.entrySet());
		entries.sort((Comparator<? super Entry>) Entry.comparingByValue()
				.reversed());

		Map<String, Integer> sortedCounts = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : entries)
		{
			sortedCounts.put(entry.getKey(), entry.getValue());
		}

		patientsByEvent = null;
		counts = null;
		entries = null;

		// write out events and corresponding patient count
		// use the count-sorted map of events
		File intragenicWhiteListFile = new File(
				"/Users/patelj1/workspace/SV-Testing/filtering/dmp-intragenic-white-list.txt");
		BufferedWriter writer = new BufferedWriter(
				new FileWriter(intragenicWhiteListFile));
		for (String event : sortedCounts.keySet())
		{
			writer.write(event);
			writer.write("\t");
			writer.write(Integer.toString(sortedCounts.get(event)));
			writer.write("\n");
		}

		writer.close();
	}

	private static double getExonNumber(String description)
	{
		description = description.toLowerCase();
		description = description.replace("before coding start",
				"before exon 1");
		description = description.replace("from tx start", "before exon 1");
		description = description.replace("after coding stop",
				"after exon 1000000");

		if (description.contains("before exon"))
		{
			String[] tokens = description.split("\\s+");
			for (int i = 2; i < tokens.length; i++)
			{
				if (tokens[i - 2].equals("before")
						&& tokens[i - 1].equals("exon"))
				{
					int num = Integer.parseInt(tokens[i]);
					return new Double(num) - 0.5;
				}
			}
		}
		else if (description.contains("after exon"))
		{
			String[] tokens = description.split("\\s+");
			for (int i = 2; i < tokens.length; i++)
			{
				if (tokens[i - 2].equals("after")
						&& tokens[i - 1].equals("exon"))
				{
					int num = Integer.parseInt(tokens[i]);
					return new Double(num) + 0.5;
				}
			}
		}
		else if (description.startsWith("exon"))
		{
			String[] tokens = description.split("\\s+");
			for (int i = 1; i < tokens.length; i++)
			{
				if (tokens[i - 1].equals("exon"))
				{
					int num = Integer.parseInt(tokens[i]);
					return new Double(num);
				}
			}
		}
		else if (description.contains("before"))
		{
			return 0.5;
		}
		else if (description.contains("after"))
		{
			return 1000000.5;
		}

		return -100;

	}

}
