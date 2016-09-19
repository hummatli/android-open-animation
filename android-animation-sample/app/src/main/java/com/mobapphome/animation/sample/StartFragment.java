package com.mobapphome.animation.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class StartFragment extends Fragment implements View.OnClickListener {

	RelativeLayout lytMain;
	ImageView imgView;
	Button btn1;
	Button btn2;
	ImageView btnSound;
	ImageView btnInfo;

	int scrWidth;
	int scrHeight;

	float imgViewInitX;
	float btn1InitX;
	float btn2InitX;
	float btnSoundInitX;
	float btnInfoInitX;

	float imgViewInitY;
	float btn1InitY;
	float btn2InitY;
	float btnSoundInitY;
	float btnInfoInitY;

	float imgViewNewY;
	float btn1NewY;
	float btn2NewY;
	float btnSoundNewY;
	float btnInfoNewY;

	boolean firstTime = true;
	boolean startOpenAnimately = false;
	boolean startEnded = false;

	SecondFragment secondFragment;


	private void playWhoosh(){
		new Thread(new Runnable() {

			@Override
			public void run() {

				MediaPlayer mp = MediaPlayer.create(lytMain.getContext(),
						R.raw.whoosh);
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mp.start();
			}
		}).start();
	}
	
	public void enableButtons(boolean enable){
		btn1.setEnabled(enable);
		btn2.setEnabled(enable);
		btnInfo.setEnabled(enable);
		btnSound.setEnabled(enable);
	}
	
	private void startAnimation(){	
		//set initial coordinates

		enableButtons(false);

		imgView.setX(UIMethods.mapToLocCordX((scrWidth - imgView.getWidth()) / 2, imgView));
		imgView.setY(UIMethods.mapToLocCordY((scrHeight - imgView.getHeight()) / 2, imgView));
		btn1.setX(UIMethods.mapToLocCordX(-btn1.getWidth(), btn1));
		btn2.setX(UIMethods.mapToLocCordX(scrWidth, btn2));
		btnSound.setY(UIMethods.mapToLocCordX(-btnSound.getHeight(), btnSound));
		btnInfo.setY(UIMethods.mapToLocCordX(-btnInfo.getHeight(), btnInfo));
		
		//Animation first 
		ObjectAnimator animScaleX = ObjectAnimator.ofFloat(imgView, "scaleX", 3, 1);
		ObjectAnimator animScaleY = ObjectAnimator.ofFloat(imgView, "scaleY", 3, 1);
		ObjectAnimator animAlpha = ObjectAnimator.ofFloat(imgView, "alpha", 0, 1);				
		
		AnimatorSet animSetFirst = new AnimatorSet();
		animSetFirst.setDuration(500);
		animSetFirst.playTogether(animScaleX, animScaleY, animAlpha);
		animSetFirst.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {

				try{
					if (((MainActivity)getActivity()).getSoundOn()) {
						playWhoosh();
					}
				}catch(NullPointerException npe){
					return;
				}
				super.onAnimationStart(animation);
			}
		
		});
		
		//Animation second
		ObjectAnimator animShakeX = ObjectAnimator.ofFloat(imgView, "x", imgView.getX() - 10, imgView.getX() + 10);
		animShakeX.setRepeatCount(17);
		animShakeX.setRepeatMode(ObjectAnimator.RESTART);
		animShakeX.setInterpolator(new LinearInterpolator());
		
		
		ObjectAnimator animShakeY = ObjectAnimator.ofFloat(imgView, "y",imgView.getY() -10, imgView.getY() +10);
		animShakeY.setRepeatCount(16);
		animShakeY.setRepeatMode(ObjectAnimator.REVERSE);
		animShakeX.setInterpolator(new LinearInterpolator());
		
		AnimatorSet animSetSecond = new AnimatorSet();
		animSetSecond.playTogether(animShakeX, animShakeY);
		animSetSecond.setDuration(50);
		long datest = 0;
		animSetSecond.addListener(new AnimatorListenerAdapter() {
			MediaPlayer mp = null;
			Vibrator v = null;
			@Override
			public void onAnimationStart(Animator animation) {

				try{
					if (((MainActivity)getActivity()).getSoundOn()) {
						new Thread(new Runnable() {
	
							@Override
							public void run() {
								mp = MediaPlayer.create(lytMain.getContext(), R.raw.explosion);
								mp.setVolume(0.3f, 0.3f);
								mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
								mp.start();
								v = (Vibrator) lytMain.getContext().getSystemService(Context.VIBRATOR_SERVICE);
								v.vibrate(5000);
							}
						}).start();
					}
				}catch(NullPointerException npe){
					return;
				}
				super.onAnimationStart(animation);
			}	
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				if (mp != null && mp.isPlaying()) mp.stop();
				if (v !=null) v.cancel();
				super.onAnimationEnd(animation);
			}
		});
		
		//Animation third
		ObjectAnimator animThird = ObjectAnimator.ofFloat(imgView, "y",  imgView.getY(), imgViewInitY);
		animThird.setDuration(500);
		animThird.setInterpolator(new OvershootInterpolator());
		animThird.setStartDelay(500);
		animThird.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {

				try{
					if (((MainActivity)getActivity()).getSoundOn()) {
						playWhoosh();
					}
				}catch(NullPointerException npe){
					return;
				}
				super.onAnimationStart(animation);
			}
		});
		
		
		//Animation forth
		ObjectAnimator animForth = ObjectAnimator.ofFloat(btn1, "x",  btn1.getX(), btn1InitX);
		animForth.setDuration(300);
		animForth.setInterpolator(new DecelerateInterpolator());
		animForth.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {

				try{
					if ((( MainActivity)getActivity()).getSoundOn()) {
						playWhoosh();
					}
				}catch(NullPointerException npe){
					return;
				}
				super.onAnimationStart(animation);
			}
		
		});
						
		//Animation fifth
		ObjectAnimator animFifth = ObjectAnimator.ofFloat(btn2, "x",  btn2.getX(), btn2InitX);
		animFifth.setDuration(300);
		animFifth.setInterpolator(new DecelerateInterpolator());
		animFifth.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {

				try{
					if (((MainActivity)getActivity()).getSoundOn()) {
						playWhoosh();
					}
				}catch(NullPointerException npe){
					return;
				}
				super.onAnimationStart(animation);
			}
		
		});
										
		//Animation sixth
		ObjectAnimator animSound = ObjectAnimator.ofFloat(btnSound, "y",  btnSound.getY(), btnSoundInitY);
		animSound.setInterpolator(new DecelerateInterpolator());

		ObjectAnimator animInfo = ObjectAnimator.ofFloat(btnInfo, "y",  btnInfo.getY(), btnInfoInitY);
		animInfo.setInterpolator(new DecelerateInterpolator());

		AnimatorSet animSixth = new AnimatorSet();
		animSixth.playTogether(animSound, animInfo);
		animSixth.setDuration(300);
		animSixth.addListener(new AnimatorListenerAdapter() {
			//Listineri sonuncu animasiyaya atmaqda veziyyet donmain
			//qarshisin almaqdir. animSetAll -a atanda gecikme bas veririr
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				enableButtons(true);
				startEnded = true;
				super.onAnimationStart(animation);
			}
		
		});
										
										
		//Animation all starter
		AnimatorSet animSetAll = new AnimatorSet();	
		animSetAll.playSequentially(animSetFirst, animSetSecond, animThird, animForth, animFifth, animSixth);
		animSetAll.start();					

	}

	private void openAnimation() {
		startOpenAnimately = true;
		enableButtons(false);
		imgViewNewY = imgView.getY() - UIMethods.getRelativeTop(imgView)
				- imgView.getHeight();
		btn1NewY = scrHeight;
		btn2NewY = scrHeight + btn2.getY() - btn1.getY();
		btnSoundNewY = btnSound.getY() - UIMethods.getRelativeTop(btnSound)
				- btnSound.getHeight();
		btnInfoNewY = btnInfo.getY() - UIMethods.getRelativeTop(btnInfo)
				- btnInfo.getHeight();

		ObjectAnimator animImgViewY = ObjectAnimator.ofFloat(imgView, "y",
				imgView.getY(), imgViewNewY);
		ObjectAnimator animBtn1 = ObjectAnimator.ofFloat(btn1, "y",
				btn1.getY(), btn1NewY);
		ObjectAnimator animBtn2 = ObjectAnimator.ofFloat(btn2, "y",
				btn2.getY(), btn2NewY);
		ObjectAnimator animBtnSound = ObjectAnimator.ofFloat(btnSound, "y",
				btnSound.getY(), btnSoundNewY);
		ObjectAnimator animBtnInfo = ObjectAnimator.ofFloat(btnInfo, "y",
				btnInfo.getY(), btnInfoNewY);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(500);
		animatorSet.setInterpolator(new AnticipateInterpolator(1));
		animatorSet.playTogether(animImgViewY, animBtnSound, animBtnInfo,
				animBtn1, animBtn2);
		animatorSet.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub

				Log.i("test", "Open animation end");
				((MainActivity) getActivity()).replaceFragments(R.id.container,
						secondFragment, MainActivity.SECOND_FARGMENT, R.anim.a2_fade_in, 0, 0, R.anim.a2_fade_out);
				enableButtons(true);
			}

		});
		animatorSet.start();

	}

	private void closeAnimation() {
		if(!startOpenAnimately){
			return;
		}
		startOpenAnimately = false;
		enableButtons(false);
		
		ObjectAnimator animImgViewY = ObjectAnimator.ofFloat(imgView, "y",
				imgViewNewY, imgViewInitY);
		ObjectAnimator animBtnSound = ObjectAnimator.ofFloat(btnSound, "y",
				btnSoundNewY, btnSoundInitY);
		ObjectAnimator animBtnInfo = ObjectAnimator.ofFloat(btnInfo, "y",
				btnInfoNewY, btnInfoInitY);
		ObjectAnimator animBtn1 = ObjectAnimator.ofFloat(btn1, "y",
				btn1NewY, btn1InitY);
		ObjectAnimator animBtn2 = ObjectAnimator.ofFloat(btn2, "y",
				btn2NewY, btn2InitY);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(500);
		animatorSet.setInterpolator(new OvershootInterpolator(1));
		animatorSet.playTogether(animImgViewY, animBtnSound, animBtnInfo,
				animBtn1, animBtn2);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				enableButtons(true);
				super.onAnimationEnd(animation);
			}
		});
		
		animatorSet.start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn1) {
			openAnimation();
		} else if (v.getId() == R.id.btn2) {
			openAnimation();
		} else if (v.getId() == R.id.btnSound) {

			try{
				if (((MainActivity)getActivity()).getSoundOn()) {
					btnSound.setBackgroundResource(R.drawable.button_sound_muted);
				} else {
					btnSound.setBackgroundResource(R.drawable.button_sound_normal);
				}
				((MainActivity)getActivity()).soundOn(!((MainActivity)getActivity()).getSoundOn());
			}catch(NullPointerException npe){
				return;
			}
		
		} else if (v.getId() == R.id.btnInfo) {
			final AboutFragment fragment = new AboutFragment();
			((MainActivity) getActivity()).replaceFragments(R.id.container, fragment,
					MainActivity.ABOUT_FARGMENT, R.anim.a3_slide_up_fade, R.anim.a1_fade_out_scale, R.anim.a1_fade_in_scale, R.anim.a3_slide_down_fade);
		}

	}


    public static StartFragment newInstance(){
        StartFragment myFragment = new StartFragment();
        return  myFragment;
    }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		firstTime = true;
    	secondFragment = SecondFragment.newInstance();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i("test", "Start frag cretate view ---");

		setHasOptionsMenu(true);
		lytMain = (RelativeLayout) inflater.inflate(R.layout.fragment_start,container, false);

		imgView = (ImageView) lytMain.findViewById(R.id.imgStart);
		btn1 = (Button) lytMain.findViewById(R.id.btn1);
		btn2 = (Button) lytMain.findViewById(R.id.btn2);
		btnSound = (ImageView) lytMain.findViewById(R.id.btnSound);
		btnInfo = (ImageView) lytMain.findViewById(R.id.btnInfo);
//		btnSound.bringToFront();
//		btnInfo.bringToFront();

		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btnSound.setOnClickListener(this);
		btnInfo.setOnClickListener(this);


		try{
			if (((MainActivity)getActivity()).getSoundOn()) {
				btnSound.setBackgroundResource(R.drawable.button_sound_normal);
			} else {
				btnSound.setBackgroundResource(R.drawable.button_sound_muted);
			}
		}catch(NullPointerException npe){
			return null;
		}

		lytMain.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// now we can retrieve the width and height
						Display display = null; 								
						try{
							display = getActivity().getWindowManager()
								.getDefaultDisplay();
						}catch(NullPointerException npe){
							return;
						}
						
						Point size = new Point();
						display.getSize(size);
						scrWidth = size.x;
						scrHeight = size.y;
						// Getting actual coordinates
						imgViewInitX = imgView.getX();
						btn1InitX = btn1.getX();
						btn2InitX = btn2.getX();
						btnSoundInitX = btnSound.getX();
						btnInfoInitX = btnInfo.getX();

						imgViewInitY = imgView.getY();
						btn1InitY = btn1.getY();
						btn2InitY = btn2.getY();
						btnSoundInitY = btnSound.getY();
						btnInfoInitY = btnInfo.getY();

						Log.i("test", "StartFrag First time = " + firstTime);
						if (firstTime) {
							firstTime = false;
							startAnimation();
						} else {
							closeAnimation();
						}

						// ...
						// do whatever you want with them
						// ...
						// this is an important step not to keep receiving
						// callbacks:
						// we should remove this listener
						// I use the function to remove it based on the api
						// level!

						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
							imgView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						else
							imgView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
					}
				});
		try{
			((MainActivity)getActivity()).startAudioFonStart();
		}catch(NullPointerException npe){
			return null;
		}
		return lytMain;
	}



	@Override
	public void onDestroyView() {

		try{
			((MainActivity)getActivity()).stopAudioFonStart();
		}catch(NullPointerException npe){
			return;
		}
		Log.i("test", "Start frag destroyed view ------");	
		super.onDestroyView();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		// if (id == R.id.action_capitaine_frag) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

}