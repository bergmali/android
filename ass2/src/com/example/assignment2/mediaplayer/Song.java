package com.example.assignment2.mediaplayer;

/**
 * Helper Class for Song-Entities
 * 
 * @author Lia
 * 
 */
public class Song {

	private String title;
	private String path;

	/**
	 * returns the songs title
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * set the songs title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 * the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
