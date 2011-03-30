package com.funtrigger.followdroidAdfree;


import com.funtrigger.followdroidAdfree.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class Btn1set extends Activity{

	Button previous,next;
	static AnimationDrawable aniimg;
	protected String tag="tag";
	static TextView tuition_pick_up;
	ImageView pic = null;
	TextView desc = null;
	ToggleButton toogle_btn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		setContentView(R.layout.page_layout);
		super.onCreate(savedInstanceState);
		this.setTitle(R.string.fall_notify);
		

			pic=(ImageView)findViewById(R.id.iv);
			desc=(TextView)findViewById(R.id.desc);

			pic.setBackgroundResource(R.anim.btn1_anim);
			desc.setText(R.string.btn1_set_des);
		
			aniimg=(AnimationDrawable) pic.getBackground();
			
		previous = (Button) findViewById(R.id.previous);
		next = (Button) findViewById(R.id.next);
		toogle_btn = (ToggleButton) findViewById(R.id.switch_btn);
		
		previous.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent();
				intent.setClass(Btn1set.this, Ahmydroid.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			
		});
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(aniimg !=null && aniimg.isRunning()==true){
					aniimg.stop();
				}
				finish();
				Intent intent = new Intent();
				intent.setClass(Btn1set.this, Btn1pickup.class);
				startActivity(intent);
			}
			
		});
		
		toogle_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(toogle_btn.isChecked() == true){
//					if(MySystemManager.checkServiceExist(Btn1set.this.getApplicationContext(),".FallDetector")==false){
						SwitchService.startService(Btn1set.this,FallDetector.class);
						Log.i("tag", "start");
//						}
						//用在讓重新開機時，能夠知道要不要啟動防摔小安
						MySharedPreferences.addPreference(Btn1set.this, "falldetector_status", true);

						MyDialog.newToast(Btn1set.this, getString(R.string.startfallprotect), R.drawable.icon);
				}else{
					Log.i("tag", "stop");
//					if(MySystemManager.checkServiceExist(Btn1set.this.getApplicationContext(),".FallDetector")==true){
						
						SwitchService.stopService(Btn1set.this,FallDetector.class);
						//用在讓重新開機時，能夠知道要不要啟動防摔小安
						MySharedPreferences.addPreference(Btn1set.this, "falldetector_status", false);
						MyDialog.newToast(Btn1set.this, getString(R.string.stopfallprotect), R.drawable.icon);
//					}
					
				}
			}
			
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		
		if(MySystemManager.checkServiceExist(Btn1set.this,".FallDetector")==true){
			toogle_btn.setChecked(true);
		}
		
		if(hasFocus==true){
			aniimg.start();
		}else{
			aniimg.stop();
		
		}
		super.onWindowFocusChanged(hasFocus);
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

}
