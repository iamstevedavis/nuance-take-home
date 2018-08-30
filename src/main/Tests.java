package main;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

import org.junit.Test;

public class Tests {
	/**
	 * Test the app with a valid file. Uses goodInput file. 
	 * @throws FileNotFoundException
	 */
	@Test
	public void TestWithGoodFile() throws FileNotFoundException {
		Object[] expected = new Object[6];
		expected[0] = new LocationCountData("School3", 108, 2);
		expected[1] = new LocationCountData("Kitchener", 108, 1);
		expected[2] = new LocationCountData("School1", 3000, 2);
		expected[3] = new LocationCountData("School2", 3000, 2);
		expected[4] = new LocationCountData("Waterloo", 6000, 1);
		expected[5] = new LocationCountData("ON", 6108, 0);
		
		Map<String, LocationCountData> data = new HashMap<>();
		String fileToRead = "goodInput";
		SchoolDataProcessor schoolDataProcessor = new SchoolDataProcessor(fileToRead);
		data = schoolDataProcessor.processFile();
		List<LocationCountData> list = schoolDataProcessor.convertAndSortData(data);
		
		Object[] testOutput = list.toArray();
		assertArrayEquals(expected, testOutput);
	}
	
	/**
	 * Test the app with an invalid file. Uses badInput file.
	 * @throws FileNotFoundException
	 */
	@Test
	public void TestWithBadFile() throws FileNotFoundException {
		Object[] expected = new Object[0];
		
		Map<String, LocationCountData> data = new HashMap<>();
		String fileToRead = "badInput";
		SchoolDataProcessor schoolDataProcessor = new SchoolDataProcessor(fileToRead);
		data = schoolDataProcessor.processFile();
		List<LocationCountData> list = schoolDataProcessor.convertAndSortData(data);
		
		Object[] testOutput = list.toArray();
		assertArrayEquals(expected, testOutput);
	}
}
