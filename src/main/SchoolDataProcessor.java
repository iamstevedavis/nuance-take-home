package main;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

/**
 * This is the main class for the app and handles all the file processing and output.
 * @author sdavis
 *
 */
public class SchoolDataProcessor {
	/**
	 * A char array used to facilitate MD5 hashing.
	 */
	final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/**
	 * The file this class will be performing processing on.
	 */
	String fileToProcess = "input.txt";
	
	/**
	 * Constructor for the class.
	 * @param fileToProcess the file that will be processed
	 */
	SchoolDataProcessor (String fileToProcess) {
		this.fileToProcess = fileToProcess;
	}
	
	/**
	 * Getter for fileToProcess
	 * @return fileToProcess
	 */
	String getFileToProcess () {
		return this.fileToProcess;
	}
	
	/**
	 * Setter for fileToProcess
	 * @param fileToProcess set fileToProcess
	 */
	void setFileToProcess (String fileToProcess) {
		this.fileToProcess = fileToProcess;
	}
	
	/**
	 * Convert the Map to a List. This allows for easier sorting and data printing.
	 * @param data the map data from the parsed file
	 * @return The map LocationCountData as a list of LocationCountData
	 */
	List<LocationCountData> convertAndSortData(Map<String, LocationCountData> data) {
		System.out.println("Converting data to list and sorting by count, location type, location (alphabetical)");
		List<LocationCountData> list = new ArrayList<LocationCountData>(data.values());
		// Some newer java functionality, this is really handy. It allows us to chain sorting and use lambdas.
		list.sort(Comparator
				.comparing(LocationCountData::getCount)
				.thenComparing(reverseOrder(comparing(LocationCountData::getLocationType)))
				.thenComparing(LocationCountData::getLocation)
		);
		
		return list;
	}
	
	/**
	 * Convert and print the Map data.
	 * @param data the map data from the parsed file
	 */
	void printData (Map<String, LocationCountData> data) {
		if (data.isEmpty()) {
			System.out.println("No data processed from file, nothing to report");
		} else {
			// Convert the data to a list and sort it. Converting it to a list because lists are easy to sort with the thenComparing feature.
			List<LocationCountData> list = convertAndSortData(data);
			System.out.println("\n==== Output =====\n");
			for(LocationCountData model : list) {
				// Pretty print the output
				System.out.printf("%-20.20s  %-20.20s%n", model.getLocation(), model.getCount());
	        }
		}
	}
	
	/**
	 * Process the file that is this.fileToProcess. This method will read the lines and make calls to update the
	 * map that holds the school data.
	 * @param fileToRead the file that contains the school data we need to process
	 * @return a map with the data in it
	 */
	Map<String, LocationCountData> processFile() {
		Map<String, LocationCountData> data = new HashMap<>();
		ReadFileLineByLine fileReader = new ReadFileLineByLine(this.fileToProcess);
		// Perform an initial read line from the file.
		fileReader.readLineFromFile();
		
		// Keep reading lines until there is no more lines
		while(fileReader.getMoreToRead()) {
			String line = fileReader.getLineRead();
			SchoolData schoolDataItem = null;
			
			try {
				System.out.println("Attempting to process line: " + fileReader.getLineRead());
				// Create a new schoolDataItem out of the line we just read
				schoolDataItem = new SchoolData(line);
			}
			// Handle exceptions
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Discarded malformed line: " + fileReader.getLineRead());
				fileReader.readLineFromFile();
				continue;
			}
			catch(NumberFormatException e) {
				System.out.println("Discarded line; malformed count: " + fileReader.getLineRead());
				fileReader.readLineFromFile();
				continue;
			}
			
			// Update the map with the newly found data
			data = SchoolDataProcessor.updateMap(data, schoolDataItem);
			// Read the next line in the file
			fileReader.readLineFromFile();
		}
		// Return the Map that holds the parsed file data
		return data;
	}
	
	/**
	 * Update the data map that we are populating from the file. Uses hashes to keep track of Provinces, Cities and Schools.
	 * The data is stored in the map using a hash as the String key. This allows me to handle situations
	 * where there is the same school in different provinces. For example if you have ON Kitchener School1 and ON Cambridge School1. Those
	 * are different School1's and we should be able to handle that scenario.
	 * 		- I get the hashables from the schoolDataItem class (Province, City, School)
	 * 		- Generate MD5 for Province and update the map at Map<Hash>
	 * 		- Generate MD5 for City and update the map at Map<ProvinceHashCityHash>
	 * 		- Generate MD5 for School and update the map at Map<ProvinceHashCityHashSchoolHash>
	 * @param mapToUpdate the data map we are updating
	 * @param schoolDataItem the school data we are using to do the update
	 * @return the updated data map
	 */
	static Map<String, LocationCountData> updateMap(Map<String, LocationCountData> mapToUpdate, SchoolData schoolDataItem) {
		System.out.println("Updating data in Map");
		// Hashables is basically what values we want to use to hash (Province, City, School)
		String[] hashables = schoolDataItem.getHashables();
		// Append each hashed hashable to the last hash with a stringbuilder
		StringBuilder currentHash = new StringBuilder();
		// This keeps track of what "type" of LocationCountData object it is. Used for sorting on output.
		// 0 is province
		// 1 is city
		// 2 is school
		int index = 0;
		
		for (String hashable: hashables) {
			// For each hashable, append it to the overall hash value. This allows us to be unique for all fields. 
			// There will only be one ON Kitchener School1 but there could also be ON Cambridge School1 and we
			// need to be able to handle that as it is a different School1 and should be displayed. 
			currentHash.append(getStringHashForValue(hashable).toString());
			System.out.println("Checking Map for hash value " + currentHash.toString());
			LocationCountData item = mapToUpdate.get(currentHash.toString());
			
			if (item != null) {
				System.out.println("Hash found, update count");
				// The hash exists, update the count
				item.setCount(item.getCount() + schoolDataItem.getCount());
			} else {
				System.out.println("Hash not in Map, add item with hash " + currentHash.toString());
				// LocationCountData object created and stored.
				LocationCountData lcd = new LocationCountData(hashable, schoolDataItem.getCount(), index);
				mapToUpdate.put(currentHash.toString(), lcd);
			}
			index++;			
		}
		return mapToUpdate;
	}
	
	/**
	 * Convert a byte array to hex so that I can store the hash as a string in the map as the key
	 * @param bytes the bytes to convert
	 * @return a string that represents a hex value
	 */
	static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	/**
	 * Perform an MD5 hash on a value.
	 * @param value the value we need to hash
	 * @return the hashed string
	 */
	static String getStringHashForValue(String value) {
		byte[] bytesOfMessage = null;
		try {
			bytesOfMessage = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unable to get bytes for value: " + value);
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Unable to get hash for value: " + value);
		}
		byte[] thedigest = md.digest(bytesOfMessage);
		return bytesToHex(thedigest);
	}
}
