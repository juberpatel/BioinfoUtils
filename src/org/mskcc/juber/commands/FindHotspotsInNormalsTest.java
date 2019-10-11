/**
 * 
 */
package org.mskcc.juber.commands;

import java.io.IOException;

/**
 * @author Juber Patel
 *
 */
public class FindHotspotsInNormalsTest
{

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		 //String pileupList =
		 //"/Users/patelj1/resources/cancer-hotspots/pileup-list.txt";
		// String pileupList = "pileup-list.txt";
		String pileupList = "/Users/patelj1/workspace/BioinfoUtils/hotspots/pileups_tmp.txt";

		String hotspotsFile = "/Users/patelj1/resources/cancer-hotspots/hotspot-list-union-v1-v2_with_TERT.txt";

		FindHotspotsInNormals.main(new String[] { pileupList, hotspotsFile });
	}

}
