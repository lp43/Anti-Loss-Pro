package com.funtrigger.followdroidAdfree;

import com.funtrigger.followdroidAdfree.R;
import com.funtrigger.followdroidAdfree.MyListAdapter.Holder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Smsset extends Activity {
	Button previous,next,setting;
	ImageButton btn_keyword_add;
	TextView describe;
	ImageView pic;
	ToggleButton switch_btn;
	AnimationDrawable aniimg;
	MySqlHelper mysql = null;
	MyListAdapter mylistadapter;
	EditText edittext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_layout);
		previous = (Button) findViewById(R.id.previous);
		next = (Button) findViewById(R.id.next);
		
		setting = (Button) findViewById(R.id.setting);
		describe = (TextView) findViewById(R.id.desc);
		describe.setText(R.string.find_phone_des);
		switch_btn = (ToggleButton) findViewById(R.id.switch_btn);
		
		pic = (ImageView) findViewById(R.id.iv);
		
		pic.setBackgroundResource(R.anim.keyword_anim);
		aniimg=(AnimationDrawable) pic.getBackground();
		
		switch_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(switch_btn.isChecked() == true){
					MySharedPreferences.addPreference(Smsset.this, "FindPhoneByKeyword", true);
					
					Log.i("tag", "start");
				}else{
					MySharedPreferences.addPreference(Smsset.this, "FindPhoneByKeyword", false);
					
					Log.i("tag", "stop");
				}
			}
			
		});

		previous.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent();
				intent.setClass(Smsset.this, Ahmydroid.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			
		});
		
		next.setVisibility(View.INVISIBLE);
		
		setting.setVisibility(View.VISIBLE);
		setting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mysql = new MySqlHelper(Smsset.this);
				
				LayoutInflater lf = LayoutInflater.from(Smsset.this);
				View keyword_add = lf.inflate(R.layout.keyword_editor, null);
				ListView listview = (ListView) keyword_add.findViewById(R.id.list);
				edittext = (EditText) keyword_add.findViewById(R.id.edittext);
				btn_keyword_add = (ImageButton) keyword_add.findViewById(R.id.keyword_add);
				btn_keyword_add.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						if(!edittext.getText().toString().equals("")){
							mysql.insert(edittext.getText().toString());
							edittext.setText("");
							Toast.makeText(Smsset.this, R.string.add_success, Toast.LENGTH_SHORT).show();
							mylistadapter.notifyDataSetChanged();
						}else{
							new AlertDialog.Builder(Smsset.this)
							.setTitle(R.string.attention)
							.setMessage(R.string.cant_be_null)
							.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which) {	
								}
								
							})
							.show();
						}
					}
					
				});
				
				
				mylistadapter = new MyListAdapter(Smsset.this, mysql);
					
				listview.setAdapter(mylistadapter);
				listview.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						
						Holder holder = (Holder) view.getTag();
						mysql.delete(holder._id);
						Toast.makeText(Smsset.this, R.string.remove_success, Toast.LENGTH_SHORT).show();
						mylistadapter.notifyDataSetChanged();
					}
					
				});
				Builder dialog = new AlertDialog.Builder(Smsset.this);
				dialog.setTitle(R.string.keyword_editor);
				dialog.setView(keyword_add);
				dialog.setPositiveButton(R.string.back, new DialogInterface.OnClickListener(){


					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if(mysql!=null)mysql.close();
						
					}
					
				});
				dialog.show();
			}
			
		});
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(MySharedPreferences.getPreference(Smsset.this, "FindPhoneByKeyword", false)==true){
			switch_btn.setChecked(true);
		}else{
			switch_btn.setChecked(false);
		}
		
		if(hasFocus==true){
			aniimg.start();
		}else{
			aniimg.stop();
		
		}
		super.onWindowFocusChanged(hasFocus);
	}
	
	@Override
	protected void onStop() {
		if(mysql !=null){
			mysql.close();
			
		}
		
		super.onStop();
	}
	
	
}
