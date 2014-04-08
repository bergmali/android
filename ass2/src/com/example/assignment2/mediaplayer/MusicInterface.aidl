package com.example.assignment2.mediaplayer;
/**
* Interface for mediaplay control actions
* @author: Lia
*/
interface MusicInterface{
	void playSong(String song);
	void pauseSong();
	void previousSong();
	void nextSong();
	void stop();
}