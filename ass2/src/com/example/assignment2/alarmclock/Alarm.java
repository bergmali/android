package com.example.assignment2.alarmclock;

import java.io.Serializable;

/**
 * Wrapper Class for Alarm-entities
 * 
 * @author Lia
 * 
 */
public class Alarm implements Serializable {

	private static final long serialVersionUID = 4300109843865572057L;
	private String hour; // int in db
	private String minute; // int in db
	private String second; // int in db
	private int id;
	private boolean active; // int in db

	/**
	 * @return the hour
	 */
	public String getHour() {
		return hour;
	}

	/**
	 * @param hour
	 * the hour to set
	 */
	public void setHour(String hour) {
		if (hour.length() < 2)
			this.hour = "0" + hour;
		else
			this.hour = hour;
	}

	/**
	 * @return the minute
	 */
	public String getMinute() {
		return minute;
	}

	/**
	 * @param minute
	 * the minute to set
	 */
	public void setMinute(String minute) {
		if (minute.length() < 2)
			this.minute = "0" + minute;
		else
			this.minute = minute;
	}

	/**
	 * @return the second
	 */
	public String getSecond() {
		return second;
	}

	/**
	 * @param second
	 * the second to set
	 */
	public void setSecond(String second) {
		this.second = second;
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

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 * the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
