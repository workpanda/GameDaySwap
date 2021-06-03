package com.hometown.sports;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmailFrag extends Fragment {
	
	EditText emailField, passwordField;
	TextView feedback;
	Button logInButton, forgotPasswordButton, newUserButton;
	String emailIn;
	String pwIn;
	EmailListener activityCallback;
	
	public interface EmailListener{
		public void onEmailLogInClick(String email, String pw);
		public void onForgotPWClicked(String email);
		public void onNewUserButtonClicked();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			activityCallback = (EmailListener) activity;
		} catch (ClassCastException e){
			Log.d("email frag onAttach","class cast");
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.emailfrag, container, false);
		emailField = (EditText)view.findViewById(R.id.fragEmailField);
		passwordField = (EditText) view.findViewById(R.id.fragPasswordField);
		logInButton = (Button) view.findViewById(R.id.fragLogInButton);
		forgotPasswordButton = (Button) view.findViewById(R.id.fragLogInForgotPassword);
		newUserButton = (Button) view.findViewById(R.id.fragLogInNewUser);
		
		logInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickedLogIn(v);
			}
		});
		
		forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickedForgotPW(v);
			}

		
		});
		
		newUserButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickedNewUser(v);
			}

			
		});
		return view;
	}
	
	private void clickedLogIn(View v) {
		// TODO Auto-generated method stub
		emailIn = emailField.getText().toString();
		pwIn = passwordField.getText().toString();
		activityCallback.onEmailLogInClick(emailIn, pwIn);
	}
	
	private void clickedForgotPW(View v) {
		String emailIn;
		emailIn = emailField.getText().toString();
		activityCallback.onForgotPWClicked(emailIn);
	}

	private void clickedNewUser(View v) {
		activityCallback.onNewUserButtonClicked();
		
	}
}
