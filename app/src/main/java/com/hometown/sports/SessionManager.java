package com.hometown.sports;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class SessionManager extends MenuBarActivity{
	// Shared Preferences
		SharedPreferences pref;

		// Editor for Shared Preferences

		Editor editor;
		// Context
		Context _context;

		// shared pref mod
		int PRIVATE_MODE = 0;
		

		// Shared pref file name

		private static final String PREF_NAME = "Hometownpref";

		// All Shared Preference Keys
		private static final String IS_LOGIN = "IsLoggedIn";
		// User Name (make vaiable public to access from the outside)
		public static final String KEY_PASSWORD = "password";
		public static final String KEY_EMAIL = "email";
		public static final String KEY_USERID = "userID";
		public static final String KEY_KEYA = "keyA";
		public static final String KEY_KEYB = "keyB";
		public static final String KEY_SALT = "salt";
		public static final String KEY_EXPIRES = "expires";
		public static final String KEY_TOKEN = "token";
		public static final String KEY_LAST4 = "lastfour";
		String checkpw;
		String checkemail;
		//Session fbSesh;
		String keyA;
		String keyB;
		String salt;
		int token;
		long expires;
		int userID;
		

		public SessionManager(){
			super();
		}

		// constructor
		public SessionManager(Context context) {
			Log.d("Session Manager","constructor");
			this._context = context;
			pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
			checkpw = pref.getString(KEY_PASSWORD, null);
			checkemail = pref.getString(KEY_EMAIL, null);
			String checkID = pref.getString(KEY_USERID, null);
			if (checkID!=null) userID = Integer.parseInt(checkID);
			else userID = 0;
			String checkExp = pref.getString(KEY_EXPIRES,null);
			if (checkExp!=null) expires = Long.parseLong(checkExp);
			else expires = 0;
			String checkTok = pref.getString(KEY_TOKEN,null);
			if (checkTok!=null) token = Integer.parseInt(checkTok);
			else token = 0;
			keyA = pref.getString(KEY_KEYA,null);
			keyB = pref.getString(KEY_KEYB,null);
			salt = pref.getString(KEY_SALT,null);
			
			editor = pref.edit();
			if (checkpw == null | checkemail == null | checkID == null | keyA == null | keyB == null | token == 0 | salt == null ) {

				Log.d("Session Manager","constructor, failed initialize null check");
				editor.putBoolean(IS_LOGIN, false);
				editor.commit();
			} else {
				Log.d("Session Manager","constructor, passed initialize null check");
				setUser(Integer.parseInt(checkID));
			}

		}

		// create login session

		public void createLoginSession(String email, String password) {
			Log.d("Session Manager","createLoginSession(email,pw)");
			editor.putBoolean(IS_LOGIN, true);
			editor.putString(KEY_PASSWORD, password);
			editor.putString(KEY_EMAIL, email);
			int userID = thisUser;
			editor.putString(KEY_USERID, String.valueOf(userID));
			editor.commit();
		}
		
		public void storeLoginSession(String email, String password, int userID) {
			editor.putBoolean(IS_LOGIN, true);
			editor.putString(KEY_PASSWORD, password);
			editor.putString(KEY_EMAIL, email);
			editor.putString(KEY_USERID, String.valueOf(userID));
			editor.putString(KEY_KEYA, getKeyA());
			editor.putString(KEY_KEYB, getKeyB());
			editor.putString(KEY_SALT, getSalt());
			editor.putString(KEY_EXPIRES, String.valueOf(getExpires()));
			editor.putString(KEY_TOKEN, String.valueOf(getTokenID()));
			
			editor.commit();
		}
		
		public void storeLoginSession(String email, String password, int userID, int tkn, long exp, String kyA, String kyB, String slt) {
			Log.d("SM storeLogin, full data", "entered");
			editor.putBoolean(IS_LOGIN, true);
			editor.putString(KEY_PASSWORD, password);
			editor.putString(KEY_EMAIL, email);
			editor.putString(KEY_USERID, String.valueOf(userID));
			editor.putString(KEY_KEYA, kyA);
			editor.putString(KEY_KEYB, kyB);
			editor.putString(KEY_SALT, slt);
			editor.putString(KEY_EXPIRES, String.valueOf(exp));
			editor.putString(KEY_TOKEN, String.valueOf(tkn));
			
			editor.commit();
		}
		
		public void storeLoginSession() {
			Log.d("Session Manager","storeLoginSession()");
			editor.putBoolean(IS_LOGIN, true);
			editor.putString(KEY_PASSWORD, checkpw);
			editor.putString(KEY_EMAIL, checkemail);
			editor.putString(KEY_USERID, String.valueOf(userID));
			editor.putString(KEY_KEYA, getKeyA());
			editor.putString(KEY_KEYB, getKeyB());
			editor.putString(KEY_SALT, getSalt());
			editor.putString(KEY_EXPIRES, String.valueOf(getExpires()));
			editor.putString(KEY_TOKEN, String.valueOf(getTokenID()));
			
			editor.commit();
		}
		
		public void checkLogin(long unix) {
			Log.d("Session Manager","checkLogin Unix");
			Log.d("checklogin unix timestamp",String.valueOf(unix));
			WebContentGet webOb = new WebContentGet();

			if (!this.isLoggedIn()) {
				Log.d("Session Manager","checkLogin Unix; !isLoggedIn()");
				Intent i = new Intent(_context, UserLogIn2.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				_context.startActivity(i);
			}
			else {
				Log.d("checking time stamp","  passed isLoggedIn()  ");
				//PUT EXPIRES CHECK HERE
				boolean store = true;
				unix = unix + 15*60;
				if (unix>expires){
					Log.d("checking time stamp","expired");
					int tryLogIn = webOb.tryLogOn(checkemail,checkpw);
					if(tryLogIn != USER_STATUS_SUCCESSFULLOGIN) store = false;
				}
				else{
					Log.d("Session Manager","checkLogin pass check expire");
					setKeyA(keyA);
					setKeyB(keyB);
					setToken(token);
					setSalt(salt);
					setExpires(expires);
					thisUser = userID;
					userEmail = checkemail;
					userPW = checkpw;
					
				}
				Log.d("checking time stamp","passed");
				if (store){
					
					Log.d("relog in","successful");

					storeLoginSession();
				}
				else{
					Log.d("relog in","failed");
					Intent i = new Intent(_context, UserLogIn2.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					_context.startActivity(i);
				}
			}
		}

		public HashMap<String, String> getUserDetails() {
			HashMap<String, String> user = new HashMap<String, String>();
			user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
			user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
			user.put(KEY_USERID, pref.getString(KEY_USERID, null));
			return user;
		}

		public void logoutUser() {
			Log.d("session manager","inlogout user");
			editor.clear();
			editor.commit();
			/*
			fbSesh = Session.getActiveSession();
			
			if(fbSesh != null){
				Log.d("inlogout user","in fbSesh not null");
				fbSesh.closeAndClearTokenInformation();
			}
			*/
			

			Intent i = new Intent(_context, UserLogIn2.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}

		public boolean isLoggedIn() {
			boolean isLog = pref.getBoolean(IS_LOGIN, false);
			Log.d("Session Manger","isLoggIn");
			/*if (!isLog){
				fbSesh = Session.getActiveSession();
				Log.d("IsLoggedIn","!isLog");
				if(fbSesh != null){
					Log.d("IsLoggedIn","fbSesh null");
					fbSesh.closeAndClearTokenInformation();
				}
			}*/
			
			Log.d("Session Manager isLoggedIn()",String.valueOf(isLog));
			return isLog;
		}
		
		public String getKyA(){
			String r = pref.getString(KEY_KEYA, "");
			setKeyA(r);
			return r;
		}
		
		public String getKyB(){
			String r = pref.getString(KEY_KEYB, "");
			setKeyB(r);
			return r;
		}
		
		public String getSlt(){
			return pref.getString(KEY_SALT, "");
		}
		
		public int getTkn(){
			int i = Integer.parseInt(pref.getString(KEY_TOKEN,"0"));
			setToken(i);
			return i;
		}
		
		public int getExp(){
			return Integer.parseInt(pref.getString(KEY_EXPIRES,"0"));
		}

		public void setCard(String last4) {
			editor.putString(KEY_LAST4, last4);
			editor.commit();
			
		}
		
		public String getCard() {
			String four = pref.getString(KEY_LAST4, "");
			return four;
			
		}
		
		/*
		 * FB log in check in session:
		 * 
		 * need to store access_token and access_expires
		 * 
		 * 
		 * String access_token = pref.getString("accessToken", null);
		 * long expires = pref.getLong("access_expires",0);
		 * 
		 * if(acess_token != null){
		 * fb.setAcessToken(access_token);
		 * }
		 * if(expires!=0){
		 * fb.setAccessExpires(expires);
		 * }
		 * 
		 * if(access_token
		 */

		// // last bracket

}
