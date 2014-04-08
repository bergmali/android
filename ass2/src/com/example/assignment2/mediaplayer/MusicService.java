package com.example.assignment2.mediaplayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;

/**
 * MusicService to keep music running in background
 * 
 * @author Lia
 * 
 */
public class MusicService extends Service {

	private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath();
	private List<Song> allSongs;
	private static int currentPosition;
	private MediaPlayer player = new MediaPlayer();
	private static Song currentSong;
	private int pos;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		allSongs = getAudioFiles();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return musicBinder;
	}

	/**
	 * Implementing Interface for music controls
	 */
	private final MusicInterface.Stub musicBinder = new MusicInterface.Stub() {

		/**
		 * Play given song or paused song
		 * 
		 * @param song
		 * to play
		 */
		@Override
		public void playSong(String song) throws RemoteException {
			if (allSongs == null) {
				allSongs = getAudioFiles();
			}
			if (getCurrentSong() == null) {
				setCurrentSong(allSongs.get(currentPosition));
			}

			buildNotification();

			player.reset();
			try {
				player.setDataSource(getCurrentSong().getPath());
				player.prepare();
				if (pos != 0) {
					player.seekTo(pos);
					pos = 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.start();

			player.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					try {
						nextSong();
					} catch (RemoteException e) {
						e.printStackTrace();
					}

				}

			});

		}

		/**
		 * Pause currently playing song
		 */
		@Override
		public void pauseSong() throws RemoteException {
			if (allSongs == null) {
				allSongs = getAudioFiles();
			}
			setCurrentSong(allSongs.get(currentPosition));
			pos = player.getCurrentPosition();
			player.pause();

		}

		/**
		 * Play previous song, if first in playlist, play last song
		 */
		@Override
		public void previousSong() throws RemoteException {
			if (allSongs == null) {
				allSongs = getAudioFiles();
			}
			if (--currentPosition < 0) {
				currentPosition = allSongs.size() - 1;
			}
			setCurrentSong(allSongs.get(currentPosition));
			pos = 0;
			playSong(getCurrentSong().getPath());

		}

		/**
		 * play next song, also called when song finishes
		 */
		@Override
		public void nextSong() throws RemoteException {
			if (allSongs == null) {
				allSongs = getAudioFiles();
			}
			if (++currentPosition >= allSongs.size()) {
				currentPosition = 0;
			}
			setCurrentSong(allSongs.get(currentPosition));
			setCurrentPosition(currentPosition);
			pos = 0;
			playSong(getCurrentSong().getPath());

		}

		/**
		 * stop player
		 */
		@Override
		public void stop() throws RemoteException {
			stopService();
		}

	};

	/**
	 * reads all .mp3 files from sd card and creates a list of Songs
	 * 
	 * @return
	 */
	public List<Song> getAudioFiles() {
		ArrayList<Song> s = new ArrayList<Song>();

		List<File> files = getListFiles(new File(MEDIA_PATH + "/"));

		for (File f : files) {
			if (f.getName().endsWith(".mp3") || f.getName().endsWith(".MP3")) {
				Song song = new Song();
				song.setTitle(f.getName());
				song.setPath(f.getPath());
				s.add(song);
			}
		}
		return s;
	}

	protected void stopService() {
		System.out.println("stopService");
		this.stopSelf();

	}

	@Override
	public void onDestroy() {
		stopForeground(true);
		if (player != null) {
			player.stop();
			player.release();
		}

		super.onDestroy();
	}

	protected static void setCurrentPosition(int pos) {
		currentPosition = pos;

	}

	/**
	 * Building ongoing notification
	 */
	protected void buildNotification() {
		System.out.println("buildNotification");

		NotificationCompat.Builder notBuild = new NotificationCompat.Builder(this);
		notBuild.setContentTitle("Currently Playing: ");
		notBuild.setContentText(getCurrentSong().getTitle());
		notBuild.setSmallIcon(android.R.drawable.ic_media_play);
		notBuild.setOngoing(true);

		Intent intent = new Intent(getApplicationContext(), MediaPlay.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0, intent, 0);
		notBuild.setContentIntent(pendInt);

		Notification not = notBuild.build();
		startForeground(1, not);

	}

	/**
	 * searching sdCard and all subdirectories for files
	 * 
	 * @param parentDir
	 * @return
	 */
	public List<File> getListFiles(File parentDir) {
		ArrayList<File> allFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();

		for (File f : files) {
			if (f.isDirectory()) {
				if (f.listFiles() != null)
					allFiles.addAll(getListFiles(f));
			} else {
				allFiles.add(f);
			}
		}

		return allFiles;
	}

	/**
	 * @return the currentSong
	 */
	public Song getCurrentSong() {
		return currentSong;
	}

	/**
	 * @param currentSong
	 * the currentSong to set
	 */
	public static void setCurrentSong(Song s) {
		currentSong = s;
	}

	/**
	 * Binder Class for binding service to activity
	 * 
	 * @author Lia
	 * 
	 */
	public class MusicBinder extends Binder {
		MusicService getService() {
			return this.getService();
		}
	}

}
