package com.hometown.sports;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;
import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

public class CreditCardPage extends MenuBarActivity implements
View.OnClickListener, SimpleGestureListener{
	
	private SimpleGestureFilter detector;
	EditText CCNField, CCVField;
	Button submitButton;
	Spinner monthSpin, yearSpin;
	ArrayList<String> monthList, yearList;
	int[] yearInts = new int[10];
	int startYr = 2015;
	boolean toAccount;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		detector = new SimpleGestureFilter(this, this);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		Intent in8 = getIntent();
		toAccount = in8.getBooleanExtra("fromAccount",false);

		setContentView(R.layout.user_credit_card);
		submitButton = (Button) findViewById(R.id.submitCardButton);
		submitButton.setOnClickListener(this);
		CCNField = (EditText) findViewById(R.id.cardNumberField);
		CCNField.setInputType(InputType.TYPE_CLASS_NUMBER);
		CCVField = (EditText) findViewById(R.id.cardCCVField);
		CCVField.setInputType(InputType.TYPE_CLASS_NUMBER);
		monthSpin = (Spinner) findViewById(R.id.cardmonthspinner);
		yearSpin = (Spinner) findViewById(R.id.cardyearspinner);
		
		monthList = new ArrayList<String>();
		monthList.add("Month");
		monthList.add("Jan");
		monthList.add("Feb");
		monthList.add("Mar");
		monthList.add("Apr");
		monthList.add("May");
		monthList.add("Jun");
		monthList.add("Jul");
		monthList.add("Aug");
		monthList.add("Sep");
		monthList.add("Oct");
		monthList.add("Nov");
		monthList.add("Dec");
		
		yearList = new ArrayList<String>();
		for(int i = 0; i<yearInts.length;i++){
			int year = startYr + i;
			yearInts[i] = year;
			yearList.add(String.valueOf(year));			
		}
		
		final ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
				this, R.layout.spinnerlayout, yearList);
		
		final ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(
				this, R.layout.spinnerlayout, monthList);
		
		monthSpin.setAdapter(monthAdapter);
		yearSpin.setAdapter(yearAdapter);

		
	}
	
	public void onResume(){
		super.onResume();
		currentActivity = this;
	}

	@Override
	public void onSwipe(int direction) {
		String str = "";
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			super.finish();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.submitCardButton:
			int monthpos = monthSpin.getSelectedItemPosition();
			int yearpos = yearSpin.getSelectedItemPosition();
			if(monthpos == 0 | yearpos == 0){
				showDialog(CreditCardPage.this,"Error","Must fill out all fields");
			}
			else{
				String numString = CCNField.getText().toString();
				String CCVString = CCVField.getText().toString();
				
				final Card card = new Card(numString,monthpos,yearInts[yearpos],CCVString);
				
				if(!card.validateCard()){
					showDialog(CreditCardPage.this,"Error","Credit Card information is not valid");
				}
				else{
					Stripe stripe;
					try {
						stripe = new Stripe("pk_test_xDb20aumYZHwAiHljg0we3zl");
					
					stripe.createToken(
					  card,
					  new TokenCallback() {
					      public void onSuccess(Token token) {
					          WebContentGet webOb = new WebContentGet();
					          int stat = webOb.sendStripeToken(token);
					          
					          session.setCard(card.getLast4());
					          Toast.makeText(getBaseContext(),
							            "Card Information Stored",
							            Toast.LENGTH_LONG
							          ).show();
					          
					          if(toAccount){
					        	  Intent i = new Intent(getBaseContext(), AccountInfo.class);
									startActivity(i);
					          }
					          else{
					        	  Intent i = new Intent(getBaseContext(), HomeTownSportsHome.class);
									startActivity(i);
					          }
					         
					      }
					      public void onError(Exception error) {
					          // Show localized error message
					          Toast.makeText(getBaseContext(),
					            error.toString(),
					            Toast.LENGTH_LONG
					          ).show();
					      }
					  }
					);
					
					} catch (AuthenticationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			break;
		}
		
	}
	
	public void showDialog(Activity activity, String title, CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// close box
			}
		});
		builder.show();
	}

	
	

}
