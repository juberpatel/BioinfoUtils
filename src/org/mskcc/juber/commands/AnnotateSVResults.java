/**
 * 
 */
package org.mskcc.juber.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
public class AnnotateSVResults
{

	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args)
			throws NumberFormatException, IOException
	{
		File iCallSVFile = new File(args[0]);
		File intragenicFile = new File(args[1]);
		File intergenicFile = new File(args[2]);

		File outFile = new File(iCallSVFile.getParent(),
				iCallSVFile.getName().replace(".txt", "-annotated.txt"));

		BufferedReader reader = new BufferedReader(new FileReader(iCallSVFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

		Map<String, Integer> intragenic = load(intragenicFile, true);
		Map<String, Integer> intergenic = load(intergenicFile, false);

		String line = null;
		boolean firstLine = true;
		while ((line = reader.readLine()) != null)
		{
			String[] tokens = line.split("\t");

			if (firstLine)
			{
				writer.write(line + "\tDMPCount\tSplitReadAF\n");
				firstLine = false;
				continue;
			}

			String eventType = tokens[6];
			String gene1 = tokens[7];
			String gene2 = tokens[8];
			double splitRefCount = Double.parseDouble(tokens[25]);
			double splitAltCount = Double.parseDouble(tokens[27]);

			double num1 = getExonNumber(tokens[11]);
			double num2 = getExonNumber(tokens[12]);

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

			// get count
			Integer count = null;

			// intragenic
			if (gene1.equals(gene2))
			{
				// make sure event type names match
				if (eventType.equals("DEL"))
				{
					eventType = "DELETION";
				}
				else if (eventType.equals("DUP"))
				{
					eventType = "DUPLICATION";
				}
				else if (eventType.equals("INV"))
				{
					eventType = "INVERSION";
				}

				String event = gene1 + "\t" + eventType + "\t" + range;
				count = intragenic.get(event);
			}
			else
			{
				// intergenic
				String event = gene1 + "\t" + gene2;
				if (gene1.compareTo(gene2) > 0)
				{
					event = gene2 + "\t" + gene1;
				}

				count = intergenic.get(event);

			}

			// no filtering
			/*
			if (count == null && splitAltCount == 0)
			{
				continue;
			}
			*/

			String c = "0";
			if (count != null)
			{
				c = count.toString();
			}

			double splitAF = 0;
			if (splitRefCount + splitAltCount != 0)
			{
				splitAF = splitAltCount / (splitRefCount + splitAltCount);
			}

			writer.write(line + "\t" + c + "\t" + splitAF + "\n");
		}

		reader.close();
		writer.close();
	}

	private static Map<String, Integer> load(File whiteListFile,
			boolean intragenic) throws IOException
	{
		Map<String, Integer> counts = new HashMap<String, Integer>();

		BufferedReader reader = new BufferedReader(
				new FileReader(whiteListFile));

		String line = null;

		while ((line = reader.readLine()) != null)
		{
			String[] tokens = line.split("\t");
			String event = null;
			Integer count = null;
			if (intragenic)
			{
				event = tokens[0] + "\t" + tokens[1] + "\t" + tokens[2];
				count = Integer.parseInt(tokens[3]);
			}
			else
			{
				event = tokens[0] + "\t" + tokens[1];
				count = Integer.parseInt(tokens[2]);

			}

			counts.put(event, count);
		}

		reader.close();

		return counts;
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
