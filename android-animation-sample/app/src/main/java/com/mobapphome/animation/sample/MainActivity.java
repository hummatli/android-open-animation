package com.mobapphome.animation.sample;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	static public final String START_FARGMENT = "startFragment";
	static public final String SECOND_FARGMENT = "secondFragment";
	static public final String ABOUT_FARGMENT = "aboutFragment";

	final public static String PREF_KEY_SOUND = "sound";

	public MediaPlayer mpFonStart;
	
	SharedPreferences prefs; 
	private boolean soundOn  = false;

    FragmentTransaction transactionForCommitOnPostResume = null;
    FragmentTransaction transactionForCommitOnPostResumeForAdd = null;

	public void soundOn(boolean soundOn){
		this.soundOn = soundOn;
		if (soundOn) {
			setVolume(1);
		} else {
			setVolume(0);
		}
		saveSoundOnPref();
	}
	
	public void setVolume(float volume){
		if(mpFonStart != null) mpFonStart.setVolume(volume, volume);
	}

	public boolean getSoundOn(){
		return soundOn;
	}


	static public void setFontTextView(Context context, TextView tv, String fontName) {
		try{
			Typeface font = Typeface.createFromAsset(context.getAssets(),fontName);
			tv.setTypeface(font);
		}catch(RuntimeException r){
			Log.e("test", "Error " + r.getMessage());
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.i("test", "Main act on create called");

		setContentView(R.layout.activity_main);		

		prefs = getSharedPreferences(
			      getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
		soundOn = prefs.getBoolean(PREF_KEY_SOUND, false);

		if (savedInstanceState == null) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			StartFragment fragment = StartFragment.newInstance();

			transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in, android.R.anim.fade_out);
			transaction.add(R.id.container, fragment, START_FARGMENT);
			transaction.commit();
		}
	}


	public void startAudioFonStart(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				stopAudioFonStart();
				mpFonStart = MediaPlayer.create(MainActivity.this, R.raw.space_drifter);
				mpFonStart.setLooping(true);
				mpFonStart.setAudioStreamType(AudioManager.STREAM_MUSIC);

				if(soundOn){
					mpFonStart.setVolume(1, 1);
				}else{
					mpFonStart.setVolume(0, 0);
				}
				mpFonStart.start();
			}
		}).start();
	}
	
	public void stopAudioFonStart(){
		if(mpFonStart != null && mpFonStart.isPlaying()){
			mpFonStart.stop();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		if(soundOn){
			setVolume(1);	
		}
		Log.i("test", "-----Main act on start  called");
		super.onStart();
	}


	@Override
	protected void onPostResume() {
		super.onPostResume();
        if(transactionForCommitOnPostResume != null){
            transactionForCommitOnPostResume.commit();
        }
        transactionForCommitOnPostResume = null;
        Log.i("test", "On resume post 1 = transactionForCommitOnPostResume = " + transactionForCommitOnPostResume);
        if(transactionForCommitOnPostResumeForAdd != null){
            Log.i("test", "On resume post 2");
            transactionForCommitOnPostResumeForAdd.commit();
        }
        transactionForCommitOnPostResumeForAdd = null;
	}

	public void saveSoundOnPref(){
		prefs.edit().putBoolean(PREF_KEY_SOUND, soundOn).apply();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		setVolume(0);
		saveSoundOnPref();
		Log.i("test", "----Main act on stop called");
		super.onStop();
	}



	public void replaceFragments(int conatinerId, Fragment fragment, String fragmentTag, int enter, int exit, int popEnter, int popExit){
		replaceFragments(conatinerId, fragment, fragmentTag, enter, exit, popEnter, popExit, false);
	}

	public void replaceFragments(int conatinerId, Fragment fragment, String fragmentTag, int enter, int exit, int popEnter, int popExit, boolean commitOnPostResume){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(enter, exit, popEnter, popExit);
		transaction.replace(conatinerId, fragment, fragmentTag);
		transaction.addToBackStack(null);
		if(!commitOnPostResume) {
 			transaction.commit();
			transactionForCommitOnPostResume = null;
		}else{
			transactionForCommitOnPostResume = transaction;
		}
	}

	
	@Override
	public void onBackPressed() {
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		Log.i("test", "Fragment size = " + fragments.size());
		for(int i = 0;i < fragments.size() ; ++i){
			Fragment frag = fragments.get(i);
			if(frag != null){
				Log.i("test", "frag " + i + " = " + fragments.get(i).getTag() + "  isvisible = " + fragments.get(i).isVisible());
				if(frag.isVisible()){
					if(frag instanceof AboutFragment){
						Log.i("test", "Current frag is About fragment");
					}else if(frag instanceof StartFragment){
						Log.i("test", "Current frag is Start fragment");
						finish();
					}else if(frag instanceof SecondFragment){
						Log.i("test", "Current frag is Second fragment");
					}
				}
			}else{
				Log.i("test", "frag " + frag);		
			}
		}

		super.onBackPressed();
		

	}
}