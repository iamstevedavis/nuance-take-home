package main;

/**
 * A custom data type that will be populated with what will eventually be the output of this application.
 * The Location is the location, the Count is the numerical count of how many have brown eyes and the
 * locType is the location type of the object (Province, City, School) and allows for easier sorting.
 * @author sdavis
 *
 */
public class LocationCountData {
	String location = "";
	int count = 0;
	int locType;
	
	LocationCountData (String location, Integer count, int locType) {
		this.location = location;
		this.count = count;
		// This is a number that indicates what type of location the object is.
		// 0 is Province
		// 1 is City
		// 2 is School
		// This isn't the best solution but it works for this.
		this.locType = locType;
	}
	
	int getLocationType () {
		return this.locType;
	}
	
	String getLocation () {
		return this.location;
	}
	
	Integer getCount () {
		return this.count;
	}

	void setLocationType(int locType) {
		this.locType = locType;
	}
	
	void setLocation(String location) {
		this.location = location;
	}
	
	void setCount(Integer count) {
		this.count = count;
	}
	
	/**
	 * This is a custom comparator for this object. Used in the tests to compare expected results to
	 * what the app is sending back.
	 * @param obj the object to compare to this
	 */
	public boolean equals(Object obj){
		LocationCountData lcd = (LocationCountData) obj;
        boolean status = false;
        if(this.location.equalsIgnoreCase(lcd.location)
                && this.count == lcd.count
                && this.locType == lcd.locType)
        {
            status = true;
        }
        return status;
    }
}
