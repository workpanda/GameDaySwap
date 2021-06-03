package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.EmailFrag.EmailListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class UserLogIn2 extends MenuBarActivity implements EmailListener {
	String emailIn;
	String pwIn;
	//SessionManager session;
	Context context = this;
	//private FBFrag fbFrag;

	@Override
	public void onEmailLogInClick(String email, String pw) {
		Log.d("UserLogin2", "onEmailLogInClick");
		emailIn = email;
		pwIn = pw;
		emailIn = emailIn.trim();
		emailIn = emailIn.toUpperCase();
		Log.d("prepped email", emailIn);

		pwIn = pwIn.trim();
		WebContentGet webOb = new WebContentGet();
		int webLog = webOb.tryLogOn(emailIn, pwIn);
		if (webLog == USER_STATUS_SUCCESSFULLOGIN) {
			int tkn = getTokenID();
			long exp = getExpires();
			String kya = getKeyA();
			String kyb = getKeyB();
			String slt = getSalt();
			Log.d("UserLogIn2 - token", String.valueOf(tkn));
			//session.storeLoginSession(emailIn, pwIn, thisUser, tkn, exp, kya, kyb, slt);

				Intent intent1 = new Intent(context, HomeTownSportsHome.class);
				startActivity(intent1);
			
		} else {
			showDialog(this, "Log in failure", "Incorrect Email or Password");
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlogin2);
		Log.d("UserLogin2", "onCreate");
		//session = new SessionManager(getApplicationContext());
		/*
		 * if (savedInstanceState == null) { Log.d("UserLogin2",
		 * "savedInstanceState null"); // Add the fragment on initial activity
		 * setup fbFrag = new FBFrag(); getSupportFragmentManager()
		 * .beginTransaction() .add(android.R.id.content, fbFrag) .commit(); }
		 * else { Log.d("UserLogin2", "savedInstanceState else"); // Or set the
		 * fragment from restored state info fbFrag = (FBFrag)
		 * getSupportFragmentManager() .findFragmentById(android.R.id.content);
		 * }
		 */

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

	/*
	@Override
	public void onFBLogInClick(String email, String fName, String lName) {
		Log.d("UserLogin2", "onFBLogInClick");
		int logIn;

		logIn = (int) Buddybets.ourServer.tryFBLogOn(email);
		Log.d("Tried FBServerLogin", String.valueOf(logIn));
		// session.createFBLoginSession(access_token,access_expires, email);

		if (logIn == 0) {
			Intent in = new Intent(context, NewUserPage2.class);
			in.putExtra("FirstName", fName);
			in.putExtra("LastName", lName);
			in.putExtra("emailAddress", email);
			String pw = fName + "123456";
			in.putExtra("password", pw);

			startActivity(in);
		} else {
			Buddybets.c.setUser(logIn);
			JSONObject jOB = Buddybets.ourServer.getUserAccountInfo(logIn);
			double userFreeAct = 0;
			double userFrozAct = 0;
			String userCCND = "";
			String pw = fName + "123456";
			session.createLoginSession(email, pw);

			try {
				fName = jOB.getString("firstName");
				lName = jOB.getString("lastName");
				userFreeAct = Double.parseDouble(jOB.getString("accountFree"));
				userFrozAct = Double
						.parseDouble(jOB.getString("accountFrozen"));
				userCCND = jOB.getString("CCNDisp");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Buddybets.c.setUserName(fName, lName);
			Buddybets.c.setUserAcctInfo(userFreeAct, userFrozAct, userCCND,
					email);
			Intent intent1 = new Intent(context, Buddybets.class);
			startActivity(intent1);

		}

	}
	*/

	@Override
	public void onForgotPWClicked(String Email) {

		int goodEmail = 0;
		//Buddybets.ourServer.checkNewEmail(Email);

		if (goodEmail == 0) {
			// send email
			showDialog(this, "Password Reset",
					"Password has been sent to email");
		} else {
			showDialog(this, "Password Reset", "Email address not found");
		}
	}

	@Override
	public void onNewUserButtonClicked() {
		Intent intent2 = new Intent(this, NewUserPage1.class);
		startActivity(intent2);
	}

}
