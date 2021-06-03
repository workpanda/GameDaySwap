package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class NewPassword extends MenuBarActivity implements View.OnClickListener {
	Button buttonUpdatePW, buttonCancel;
	EditText passwordFieldNew, passwordFieldNew2, passwordFieldOld;
	TextView feedback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newpassword);
		ActionBar actionBar = getActionBar();
        actionBar.show();

		buttonUpdatePW = (Button) findViewById(R.id.buttonUpdatePassword);
		buttonCancel = (Button) findViewById(R.id.buttonCancelNewPW);
		passwordFieldOld = (EditText) findViewById(R.id.editTextOldPassword);
		passwordFieldNew = (EditText) findViewById(R.id.editTextNewPassword1);
		passwordFieldNew2 = (EditText) findViewById(R.id.editTextNewPassword2);
		feedback = (TextView) findViewById(R.id.textNewPasswordStatus);
		feedback.setText("");

		buttonUpdatePW.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);

		passwordFieldOld.requestFocus();

		passwordFieldOld.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent e) {
				if ((e.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					passwordFieldNew.requestFocus();
					return true;
				}
				return false;
			}

		});

		passwordFieldNew.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent e) {
				if ((e.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					passwordFieldNew2.requestFocus();
					return true;
				}
				return false;
			}

		});

		passwordFieldNew2.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent e) {
				if ((e.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					return true;
				}
				return false;
			}

		});

	}

	@Override
	public void onClick(View v) {
		final Context context = this;
		switch (v.getId()) {
		case R.id.buttonUpdatePassword:

			String pwOld;
			String pwNew1;
			String pwNew2;

			pwOld = passwordFieldOld.getText().toString();
			pwOld = pwOld.trim();
			pwNew1 = passwordFieldNew.getText().toString();
			pwNew1 = pwNew1.trim();
			pwNew2 = passwordFieldNew2.getText().toString();
			pwNew2 = pwNew2.trim();
			
			int proceed = 1;

			if (pwOld.equals("")) {
				proceed = 0;
			}
			if (pwNew1.equals("")) {
				proceed = 0;
			}
			if (pwNew2.equals("")) {
				proceed = 0;
			}

			if (proceed == 0) {
				showDialog(this, "Password Update Error:", "Please fill out all fields");
				break;
			}
			
			WebContentGet webOb = new WebContentGet();
			int logIn = webOb.tryLogOn(userEmail, userPW);

			if (logIn < 3) {
				showDialog(this, "Password Update Error:", "Old Password does not match current");
				break;
			}

			if (!pwNew1.equals(pwNew2)) {
				showDialog(this, "Password Update Error:", "New Passwords do not match");
				break;
			}
			
			JSONObject jOb = new JSONObject();
			
			try {
				jOb.put("userID", thisUser);
				jOb.put("newPW", pwNew1);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			WebContentGet webOb2 = new WebContentGet();
			webOb2.changeUserPassword(jOb);

			Intent in2 = new Intent(context, AccountInfo.class);
			startActivity(in2);
			break;
		case R.id.buttonCancelNewPW:
			super.finish();
			break;

		}
	}
	
	public void showDialog(Activity activity, String title, CharSequence message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
    	builder.setMessage(message);
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int which) {
    			//close box
    		}
    		});
    	builder.show();
    }
}
