package main;

/**
 * This class parses lines read from the file into a SchoolData object.
 * @author sdavis
 *
 */
public class SchoolData {
	String province = null;
	String city = null;
	String school = null;
	String grade = null;
	int count = 0;
	
	SchoolData(String schoolDataRow) throws ArrayIndexOutOfBoundsException, NumberFormatException {
		String[] dataElements = schoolDataRow.split("\t");
		this.province = dataElements[0];
		this.city = dataElements[1];
		this.school = dataElements[2];
		this.grade = dataElements[3].substring(0, 1);
		this.count = Integer.parseInt(dataElements[3].substring(1, dataElements[3].length()));
		System.out.println("Got " + this.province + " " + this.city + " " + this.school + " " + this.grade + " " + this.count);
	}
	
	String[] getHashables() {
		return new String[] {this.getProvince(), this.getCity(), this.getSchool()};
	}
	
	String getProvince() {
		return this.province;
	}
	
	String getCity() {
		return this.city;
	}
	
	String getSchool() {
		return this.school;
	}
	
	String getGrade() {
		return this.grade;
	}
	
	int getCount() {
		return this.count;
	}
}
