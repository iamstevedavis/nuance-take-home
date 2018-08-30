package main;

import java.util.HashMap;
import java.util.Map;

/**
 * How it works:
 * 1. Read in the file line by line
 * 
 * 2. Use the SchoolData class to break down the line that has been read in
 * 		- Breaks it down into Province, City, School, Grade, Count
 * 
 * 3. The data is stored in the map using a hash as the String key. This allows me to handle situations
 * 	where there is the same school in different provinces.
 * 		- I get the hashables from the schoolDataItem class (Province, City, School)
 * 		- Generate MD5 for Province and update the map at Map<Hash>
 * 		- Generate MD5 for Province, City and update the map at Map<Hash>
 * 		- Generate MD5 for Province, City, School and update the map at Map<Hash>
 * 
 *  4. The data is stored as a LocationCountData object. This object keeps track of the location, the count and
 *  the type of location the object is. This allows for sorting later.
 *  
 *  5. The data map is sent to be printed where it is converted into a list for easier sorting
 *  	- The list is sorted by count and location.
 *  
 * @author sdavis
 *
 */
public class main {
	public static void main(String[] args) {
		// Holds the hashed data
		Map<String, LocationCountData> data = new HashMap<>();
		// File that we will send to the app
		String fileToRead = "goodInput";
		
		// If a file was passed in via command line, use that instead
		if (args.length != 0) {
			fileToRead = args[0];
		}
		
		// Instantiate a new object of schoolDataProcessor and pass the constructor the fileToRead
		SchoolDataProcessor schoolDataProcessor = new SchoolDataProcessor(fileToRead);
		
		// Process the file
		data = schoolDataProcessor.processFile();

		// Output the data
		schoolDataProcessor.printData(data);
	}
}
