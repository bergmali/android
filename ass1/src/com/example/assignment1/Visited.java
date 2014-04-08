/**
 * @author Lia
 * version: 1.0
 * Wrapper-Class for visited Countries
 */
package com.example.assignment1;

public class Visited {
	// Year visited
	private int year;
	// Country visited
	private String country;

	public Visited(int year, String country) {
		this.setYear(year);
		this.setCountry(country);
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
}
