package com.example.assignment2.mediaplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.assignment2.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Basic Mp3-Player class
 * 
 * @author Lia
 * 
 */
public class MediaPlay extends ListActivity {

	private List<Song> allSongs = new ArrayList<Song>();
	private Song currentSong;
	private ImageButton back;
	private ImageButton pause;
	private ImageButton next;
	private ImageButton play;
	private MusicInterface mInterface;
	private ListView lv;
	private MyListAdapter adapter;
	private Intent connectionIntent;
	private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_play);

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		/**
		 * Connect to service
		 */
		connectionIntent = new Intent(this, MusicService.class);
		this.startService(connectionIntent);
		doBindService(connectionIntent);

	}

	private void doBindService(Intent in) {
		this.bindService(in, musicPlayerConnection, BIND_AUTO_CREATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media_play, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * create alerdialog to exit application only way service is destroyed.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit_app:
			new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.exit_music_app))
					.setMessage(getResources().getString(R.string.dialog_exit))
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							destroyAll();
						}
					})
					.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).show();

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * unbinds and stops service, finishes application
	 */
	protected void destroyAll() {
		onDestroy();
		unbindService(musicPlayerConnection);
		try {
			mInterface.stop();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		finish();
	}

	/**
	 * Update SongList by filtering files from sdCard to end on .mp3
	 */
	public void updateSongList() {

		List<File> files = getListFiles(new File(MEDIA_PATH + "/"));

		for (File f : files) {
			if (f.getName().endsWith(".mp3") || f.getName().endsWith(".MP3")) {
				Song song = new Song();
				song.setTitle(f.getName());
				song.setPath(f.getPath());
				allSongs.add(song);
			}
		}
	}

	/**
	 * Getting all files from sdCard and subfolders (recursive)
	 * 
	 * @param parentDir
	 * directory to search for files
	 * @return all files in directory
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
	 * registers clicklisteners for control buttons
	 */
	private void registerClickListeners() {

		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentSong == null)
					try {
						mInterface.playSong(allSongs.get(0).getPath());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				else
					try {
						mInterface.playSong(currentSong.getPath());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
			}

		});

		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					mInterface.pauseSong();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					mInterface.previousSong();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		});

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					mInterface.nextSong();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * ArrayAdapter for showing List of available Songs
	 * 
	 * @author Lia
	 * 
	 */
	public class MyListAdapter extends ArrayAdapter<Song> {

		public MyListAdapter(Context context, int resource, List<Song> objects) {
			super(context, resource, objects);
		}

		@Override
		// Called when updating the ListView
		public View getView(int position, View convertView, ViewGroup parent) {
			View row;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.music_list_item, parent, false);
			} else {
				row = convertView;
			}
			TextView title = (TextView) row.findViewById(R.id.music_item_title);
			title.setText(allSongs.get(position).getTitle());
			return row;
		}

	}

	/**
	 * ServiceConnection to connect Activity with Service
	 */
	private ServiceConnection musicPlayerConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mInterface = MusicInterface.Stub.asInterface((IBinder) service);
			updateSongList();
			lv = (ListView) findViewById(android.R.id.list);
			adapter = new MyListAdapter(getApplicationContext(), R.layout.music_list_item, allSongs);
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					currentSong = allSongs.get(arg2);
					System.out.println("OnClick: " + currentSong.getTitle());
					try {
						System.out.println("trying to play song via interface");
						MusicService.setCurrentSong(currentSong);
						MusicService.setCurrentPosition(arg2);
						mInterface.playSong(currentSong.getPath());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}

			});

			play = (ImageButton) findViewById(R.id.play_button);
			pause = (ImageButton) findViewById(R.id.pause_button);
			next = (ImageButton) findViewById(R.id.next_button);
			back = (ImageButton) findViewById(R.id.back_button);
			registerClickListeners(); // register clickListeners for Buttons
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	};
}
