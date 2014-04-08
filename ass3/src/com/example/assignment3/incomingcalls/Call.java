package com.example.assignment3.incomingcalls;

/**
 * @author: Lia
 * @version: 1.0 Wrapper class for incoming calls, saving id, phone number and date & time of
 * incoming call
 */
public class Call {

	private String date;
	private String number;
	private int id;

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 * the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 * the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 * the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
