/**
 * 
 */
package org.mskcc.juber.commands;

import java.io.IOException;

/**
 * @author Juber Patel
 *
 */
public class AnnotateSVResultsTest
{

	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args)
			throws NumberFormatException, IOException
	{
		String iCallSVFile = "/Users/patelj1/workspace/SV-Testing/filtering/"
				+ "iAnnotateSVExample.txt";
		String intragenicWhiteList = "/Users/patelj1/workspace/SV-Testing/filtering/dmp-intragenic-white-list.txt";
		String intergenicWhiteList = "/Users/patelj1/workspace/SV-Testing/filtering/dmp-intergenic-white-list.txt";

		AnnotateSVResults.main(new String[] { iCallSVFile, intragenicWhiteList,
				intergenicWhiteList });
	}

}
