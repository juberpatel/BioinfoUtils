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
package org.mskcc.juber.intervals;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

/**
 * @author Juber Patel
 * 
 *         a mapping from genomic intervals to corresponding names that can be
 *         checked for intervals that intersect with a given
 *         interval
 *
 */
public class IntervalNameMap
{
	// map of contig name to the corresponding list of range maps
	private Map<String, List<TreeRangeMap<Integer, String>>> contigIntervals;

	public IntervalNameMap()
	{
		contigIntervals = new TreeMap<String, List<TreeRangeMap<Integer, String>>>();
	}

	public List<String> getIntersecting(String contig, int start, int end)
	{
		List<String> nameList = new ArrayList<String>();
		Range<Integer> range = Range.closed(start, end);

		// get the contig-specific list of range maps
		List<TreeRangeMap<Integer, String>> contigRanges = contigIntervals
				.get(contig);

		if (contigRanges == null)
		{
			return nameList;
		}

		// check each map in the list for intersection
		for (int i = 0; i < contigRanges.size(); i++)
		{
			TreeRangeMap<Integer, String> map = contigRanges.get(i);
			Map<Range<Integer>, String> intersecting = map.subRangeMap(range)
					.asMapOfRanges();

			// add all intersecting ranges from the current map to the return
			// list
			for (Range<Integer> key : intersecting.keySet())
			{
				nameList.add(intersecting.get(key));
			}
		}

		return nameList;
	}

	public void add(String contig, int start, int end, String name)
	{
		List<TreeRangeMap<Integer, String>> contigRanges = contigIntervals
				.get(contig);

		// if the contig-specific list does not exist, add it
		if (contigRanges == null)
		{
			contigRanges = new ArrayList<TreeRangeMap<Integer, String>>();
			contigIntervals.put(contig, contigRanges);
		}

		Range<Integer> range = Range.closed(start, end);
		add(contigRanges, range, name);
	}

	private void add(List<TreeRangeMap<Integer, String>> contigRanges,
			Range<Integer> range, String name)
	{
		TreeRangeMap<Integer, String> map = null;

		// find the first tree range map that has no range conflicting
		// with the given range
		// this is because Guava range maps don't accommodate intersecting
		// ranges
		int index = 0;
		for (; index < contigRanges.size(); index++)
		{
			map = contigRanges.get(index);
			if (map.subRangeMap(range).asMapOfRanges().size() == 0)
			{
				break;
			}
		}

		// did not break, requires adding new map
		if (index == contigRanges.size())
		{
			map = TreeRangeMap.create();
			contigRanges.add(map);
		}

		// add the current range to the selected map
		map.put(range, name);
	}

	public static void main(String[] args) throws IOException
	{
		IntervalNameMap nameMap = new IntervalNameMap();

		BufferedReader reader = new BufferedReader(new FileReader(
				"/Users/patelj1/resources/gene-list/genelist.with_aa.interval_list"));

		// load the name map
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			String[] parts = line.split("\t");
			int start = Integer.parseInt(parts[1]);
			int end = Integer.parseInt(parts[2]);
			nameMap.add(parts[0], start, end, parts[4]);
		}

		reader.close();
		
		// try to search some things
		List<String> overlapping = nameMap.getIntersecting("1", 8075444,
				8075444);
		for (String name : overlapping)
		{
			System.out.print(name + "\t");
		}

		int a = 5;
	}
}
