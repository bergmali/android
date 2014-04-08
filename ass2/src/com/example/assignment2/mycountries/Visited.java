package com.example.assignment2.mycountries;

/**
 * @author Lia version: 1.0 Wrapper-Class for visited Countries
 */
public class Visited implements Comparable<Visited> {
	// Country visited
	private String country;
	// Year visited
	private int year;
	// ID
	private long id;

	public Visited(int year, String country) {
		this.setYear(year);
		this.setCountry(country);
	}

	@Override
	public int compareTo(Visited another) {
		return country.compareTo(another.country);
	}

	public int compareYears(Visited another) {
		if (year < another.getYear()) {
			return -1;
		} else if (year == another.getYear()) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param country
	 * the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param year
	 * the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
