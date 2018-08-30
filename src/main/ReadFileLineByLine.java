package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class handles reading the file one line at a time. It also checks if the file exists.
 * lineRead contains the line that was last read and moreToRead will be false when the end of
 * the file has been reached.
 * @author sdavis
 *
 */
public class ReadFileLineByLine {
	// The location of the file
	String fileLocation = null;
	// File handle
	File file = null;
	// FileReader handle
	FileReader fileReader = null;
	// BufferedReader to read the file
	BufferedReader bufferedReader = null;
	// The last line read from the file
	String lineRead = null;
	// Use this to check file existence 
	boolean fileHasBeenChecked = false;
	// Set if I am currently reading a file
	boolean isReading = false;
	// Set if there is more to read
	boolean moreToRead = false;
	
	ReadFileLineByLine(String fileToRead) {
		this.fileLocation = fileToRead;
	}
	
	boolean getMoreToRead() {
		return this.moreToRead;
	}
	
	boolean getIsReading() {
		return this.isReading;
	}
	
	String getLineRead() {
		return this.lineRead;
	}
	
	/**
	 * Check to see if the file exists. Basically prevents an exception being thrown so I have more control
	 * of the error.
	 * @param file the file path
	 * @return a boolean indicating if the file exists
	 */
	Boolean checkIfFileExists(String file) {
		System.out.println("Checking if file " + file + " exists");
		File f = new File(file);
		if(f.exists() && !f.isDirectory()) {
			System.out.println("Found " + file);
		    return true;
		}
		System.out.println("File " + file + " did not exist");
		return false;
	}
	
	/**
	 * Reads a line from the file and sets the lineRead variable to the line that was read.
	 * If there is no line read or the file does not exists, lineRead is null and so is moreToRead.
	 * This allows my calling function to terminate gracefully because I can check if there is moreToRead and
	 * end if not.
	 */
	void readLineFromFile()  {
		// Check if the file exists. If the file does not exist, it sets the line read to null.
		// If lineread is null, more to read will be false and the app will output that there is nothing to report.
		// This is handled in SchoolDataProcessor.processFile while loop while(fileReader.getMoreToRead())
		if(!this.fileHasBeenChecked) {
			if(!checkIfFileExists(this.fileLocation)) {
				this.lineRead = null;
			} else {
				this.fileHasBeenChecked = true;
			}
		}
		
		// Validate if we have a file handle.
		if(this.file == null) {
			this.file = new File(this.fileLocation);
		}
		
		// Read a line from the file
		try {
			if(this.fileReader == null) {
				this.fileReader = new FileReader(this.file);
			}
			if(this.bufferedReader == null) {
				this.bufferedReader = new BufferedReader(this.fileReader);
			}
			System.out.println("\nReading line from file");
			this.lineRead = bufferedReader.readLine();
			if(this.lineRead == null) {
				System.out.println("No more lines in file");
				this.isReading = false;
				this.moreToRead = false;
				this.fileReader.close();
			} else {
				System.out.println("Read line from file");
				this.isReading = true;
				this.moreToRead = true;
			}
		} catch (IOException e) {
			this.lineRead = null;
		}
	}
}
