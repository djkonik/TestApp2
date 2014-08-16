package com.example.testapp2;

import java.io.IOException;
import java.util.Random;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button but1;
	TextView txt1;
	MediaPlayer media;
	ImageView wormImg;
	Handler handler;
	Context self;
	
	boolean bVibrationOn = true;
	boolean bSoundOn = true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        but1 = (Button)findViewById(R.id.button1);
        txt1 = (TextView)findViewById(R.id.textView1);
        media = MediaPlayer.create(this, R.raw.wegorz);
        media.setLooping(true);
        wormImg = (ImageView)findViewById(R.id.wormImg);
        
        wormImg.setVisibility(View.INVISIBLE);
        self = this;

        but1.setOnTouchListener(new OnTouchListener() {
        	RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        	int iScreenFuckSpeed = 50;
        	int iVibratorFuckSpeed = 1000;
        	int iWormAnimationSpeed = 200;
        	Runnable rScreenFuck = new Runnable() {
        		@Override public void run() {
        			int[] colors = self.getResources().getIntArray(R.array.rainbow);
        			layout.setBackgroundColor(colors[new Random().nextInt(colors.length-1)]);
        			handler.postDelayed(this, iScreenFuckSpeed);
        		}
        	};
        	Runnable rVibratorFuck = new Runnable() {
        		@Override public void run() {
        			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        			vibrator.vibrate(iVibratorFuckSpeed/2);
        			handler.postDelayed(this, iVibratorFuckSpeed);
        		}
        	};
        	Runnable rUpdateTime = new Runnable() {
        		@Override public void run() {
        			Log.d("time",""+media.getCurrentPosition());
        			txt1.setText("" + media.getCurrentPosition());
        			handler.postDelayed(this, 0);
        		}
        	};
        	
        	boolean bWormInit = true;
        	Runnable rWormAnimation = new Runnable() {
        		@Override public void run() {
        			if (bWormInit) wormImg.setImageResource(R.raw.worm0);
        			else wormImg.setImageResource(R.raw.worm1);
        			bWormInit = !bWormInit;
        			handler.postDelayed(this, iWormAnimationSpeed);
        		}
        	};
        	
			@Override
            public boolean onTouch(View v, MotionEvent event) {
            	if(event.getAction() == MotionEvent.ACTION_DOWN){
            		if (handler != null) return true;
            		handler = new Handler();
            		handler.postDelayed(rScreenFuck, iScreenFuckSpeed);
            		handler.postDelayed(rUpdateTime, 0);
            		if (bVibrationOn) handler.postDelayed(rVibratorFuck, 100);
            		if (bSoundOn) {
            			//media.seekTo(0); 
            			media.start();
            		}
            		wormImg.setVisibility(View.VISIBLE);
            		handler.postDelayed(rWormAnimation, iWormAnimationSpeed);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                	if (handler == null) return true;
                	handler.removeCallbacks(rScreenFuck);
                	handler.removeCallbacks(rUpdateTime);
                	handler.removeCallbacks(rVibratorFuck);
                	handler.removeCallbacks(rWormAnimation);
                	if (bVibrationOn)  ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).cancel();
                	if (bSoundOn) {
                		media.stop();
                		media.prepareAsync();
                	}
                	wormImg.setVisibility(View.INVISIBLE);
                	layout.setBackgroundColor(Color.WHITE);
                	handler = null;
                }
                return true;
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()){
    		case R.id.vibrationSettings:
    			if (bVibrationOn) {
    				bVibrationOn = false;
    				item.setTitle(R.string.vibrationOn);
    			} else {
    				bVibrationOn = true;
    				item.setTitle(R.string.vibrationOff);
    			}
    			break;
    		case R.id.soundSettings:
    			if (bSoundOn) {
    				bSoundOn = false;
    				item.setTitle(R.string.soundOn);
    			} else {
    				bSoundOn = true;
    				item.setTitle(R.string.soundOff);
    			}
    	}
    	return true;
    }
    
}


