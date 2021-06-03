package com.hometown.sports;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class ChallengePage extends MenuBarActivity implements
		View.OnClickListener, SimpleGestureListener {
	ImageButton button1, button2, button3, button4, button5, button6;
	private SimpleGestureFilter detector;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		button1 = (ImageButton) findViewById(R.id.collegebasketball);
		button2 = (ImageButton) findViewById(R.id.collegefootball);
		button3 = (ImageButton) findViewById(R.id.probasketball);
		button4 = (ImageButton) findViewById(R.id.profootball);
		button5 = (ImageButton) findViewById(R.id.prohockey);
		button6 = (ImageButton) findViewById(R.id.probaseball);
		
		

		/**   */
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		detector = new SimpleGestureFilter(this, this);

	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.collegebasketball:
			Intent intent1 = new Intent(context, SingleSportPage.class);
			intent1.putExtra("sportType", NCAAB_ID);
			startActivity(intent1);
			break;
		case R.id.collegefootball:
			Intent intent2 = new Intent(context, SingleSportPage.class);
			intent2.putExtra("sportType", NCAAF_ID);
			startActivity(intent2);
			// do stuff;
			break;
		case R.id.probasketball:
			Intent intent3 = new Intent(context, SingleSportPage.class);
			intent3.putExtra("sportType", NBA_ID);
			startActivity(intent3);

			break;
		case R.id.profootball:
			Intent intent4 = new Intent(context, SingleSportPage.class);
			intent4.putExtra("sportType", NFL_ID);
			startActivity(intent4);
			break;

		case R.id.prohockey:
			Intent intent5 = new Intent(context, SingleSportPage.class);
			intent5.putExtra("sportType", NHL_ID);
			startActivity(intent5);
			break;
		case R.id.probaseball:
			Intent intent6 = new Intent(context, SingleSportPage.class);
			intent6.putExtra("sportType", MLB_ID);
			startActivity(intent6);
			break;

		}
	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Make a Challenge";
		String txt = "Select a sport to make a challenge in";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		final Context context = this;
		String str = "";
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			super.finish();
			Intent intent1 = new Intent(context, HomeTownSportsHome.class);

			startActivity(intent1);
			break;
		case SimpleGestureFilter.SWIPE_LEFT:

			break;
		case SimpleGestureFilter.SWIPE_DOWN:
			break;
		case SimpleGestureFilter.SWIPE_UP:
			break;
		}
	}

	@Override
	public void onDoubleTap() {

	}
}