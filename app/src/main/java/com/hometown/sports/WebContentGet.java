package com.hometown.sports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stripe.android.model.Token;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WebContentGet extends MenuBarActivity{
	
	protected static final int versionID = 2;
	String dataLine;
	String keyA;
	String keyB;
	int token;
	int status;
	String dataPass = "";
	JSONObject sendObject = new JSONObject();
	Activity activity;
	Context cont;
	
	WebContentGet(){
		cont = getBaseContext();
	}
	
	WebContentGet(Activity a){
		activity = a;
		cont = activity.getBaseContext();
	}
	
	
	Thread t = new Thread() {

		public void run() {
			//Log.d("in Thread T","run");
			JSONObject jOb = getSendJSON();
			try {
				jOb.put("version", getVersionID());
				jOb.put("tokenID", getTokenID());
				jOb.put("keyA", getKeyA());
				jOb.put("keyB", getKeyB());
				//Log.d("adding to jOb","parameter token= "+String.valueOf(getTokenID()));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000); // Timeout
																		// Limit
			HttpResponse response;
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			try {
				HttpPost post = new HttpPost(URL);
				nameValuePairs.add(new BasicNameValuePair("json", jOb
						.toString()));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = client.execute(post);
				String modl = jOb.getString("function");
				//Log.d("return from httppost","got response");
				if (response != null) {
					//Log.d("in response !null","response has value");
					BufferedReader in = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					StringBuffer sb = new StringBuffer("");
					String l = "";
					String nl = System.getProperty("line.separator");

					while ((l = in.readLine()) != null) {
						sb.append(l + nl);

					}
					in.close();
					String data = sb.toString();
					Log.d("webDataGet return string " + modl, data);
					if(data.indexOf("{")==-1){
						setStatus(0);
						Log.d("webObject return string", "no value");
					}
					else{
					String data2 = data.substring(data.indexOf("{"));
					//Log.d("webDataGet return string trimmed", data2);
					setData(data2);
					JSONObject checker = new JSONObject(data2);
					int stat = Integer.parseInt(checker.getString("status"));
					setStatus(stat);
					if(stat == KEY_STATUS_FAILED){
						Log.d("Process t", "Key Status Failed");

							Intent i = new Intent(currentActivity, UserLogIn2.class);
							startActivity(i);
						
						
					}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	};





	public int getExistingUserID(String emailIn) {
		// return 70 if no user; return 71 with userID if user
		//jObSend.put("function", "getExistingIDPhone"); for phone number search
		String emailUC = emailIn.toUpperCase();
		int uID = 0;
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("email", emailUC);
			jObSend.put("module", "user");
			jObSend.put("function", "getExistingIDEmail");
			
			setSendJSON(jObSend);
			t.start();
			t.join();
			
			if(getStatus() == LIST_STATUS_RESULTS){
				String data = getData();
				JSONObject jObReturn;
				jObReturn = new JSONObject(data);
				uID = Integer.parseInt(jObReturn.getString("userID"));
			}
			else uID = LIST_STATUS_NORESULTS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uID;
	}

	public int makeChal(JSONObject chalIn) {
		try {
			chalIn.put("module", "challenge");
			chalIn.put("function", "makeChal");
			setSendJSON(chalIn);
			t.start();
			t.join();
			
			int stat = getStatus();
			return stat;
		} catch (Exception e) {
			return CHAL_STATUS_FAILED;
		}
		// returning info 29 = good, 31-34=game not open for bets (why)
		// 46 = not enough money
	}

	public int acceptChal(int id) {
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("ChallengeID", id);
			jObSend.put("module", "challenge");
			jObSend.put("function", "acceptChal");
			setSendJSON(jObSend);
			t.start();
			t.join();
			int stat = getStatus();
			return stat;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CHAL_STATUS_FAILED;
		}
		// same status as makeBet + 28=error in processing REVIEW ERRORS NEXT TIME (accepting already-accepted public, re-accepting)
	}

	public int declineChal(int id) {
		//returns 29 - success; 28 - failure;  23 - cancelled bet
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("ChallengeID", id);
			jObSend.put("module", "challenge");
			jObSend.put("function", "declineChal");
			setSendJSON(jObSend);
			t.start();
			t.join();
			return getStatus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CHAL_STATUS_FAILED;
		}
	}
	
	public int cancelChal(int id) {
		//returns 29 - success; 28 - failure;  23 - cancelled bet
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("ChallengeID", id);
			jObSend.put("module", "challenge");
			jObSend.put("function", "cancelChal");
			setSendJSON(jObSend);
			t.start();
			t.join();
			return getStatus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CHAL_STATUS_FAILED;
		}
	}

	public JSONObject getUserAccountInfo(int uID) { //user id = 0 -> personal account info; userID >0 limited return for outside user
		//returns 70, 71, array getAccoutnInfo
		JSONObject re = new JSONObject();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("userID", uID);
			jObSend.put("module", "user");
			jObSend.put("function", "getUserAccountInfo");
			setSendJSON(jObSend);
			t.start();
			t.join();
			
			if (getStatus()==LIST_STATUS_RESULTS) {
				String data = getData();
				int startind = data.indexOf("[") + 1;
				int endind = data.indexOf("]");
				String data2 = data.substring(startind,endind);
				JSONObject jObReturn;
				jObReturn = new JSONObject(data2);
				Log.d("webob String output of userinfo JSON",jObReturn.toString());
				re = jObReturn;
			}
			else if(getStatus()==KEY_STATUS_FAILED){
				Intent in = new Intent(activity,UserLogIn2.class);
				startActivity(in);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return re;
		}
		return re;
	}

	public JSONArray BRAVO(int sportID) 
			throws InterruptedException {
		JSONArray re = new JSONArray();
		final int sptID = sportID;
		JSONObject jObSend = new JSONObject();

		try {
			jObSend.put("sportID", sptID);
			//jObSend.put("status", gtype);// if not provided everything returned statuses in 30s
			//jObSend.put("page", 1);//if want more games than 25, send page 2
			jObSend.put("module", "challenge");
			jObSend.put("function", "bravo");

			setSendJSON(jObSend);
			t.start();
			t.join();
			Log.d("webOb bravo","return from t");

			if (getStatus() == LIST_STATUS_RESULTS) {// 70 or 71, also return a pages item for total available pages
				Log.d("webOb bravo","in IF getStatus()");

				String data = getData();	
				//Log.d("webOb getGameList data",data);
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				//Log.d("webOb getGameList data 2",data2);
				JSONArray jsonArray = new JSONArray(data2);
				re=jsonArray;
			} else {
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return re;
		}

		return re;
		// 71 = good, 70 = error, 12
	}
	
	public JSONArray DELTA(int sportID, int paStat, long maxTime) 
			throws InterruptedException {
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();

		try {
			jObSend.put("sportID", sportID);
			jObSend.put("paStatus", paStat);
			jObSend.put("maxTime", maxTime);
			//jObSend.put("status", gtype);// if not provided everything returned statuses in 30s
			//jObSend.put("page", 1);//if want more games than 25, send page 2
			jObSend.put("module", "challenge");
			jObSend.put("function", "delta");

			setSendJSON(jObSend);
			t.start();
			t.join();
			Log.d("webOb DELTA","return from t");

			if (getStatus() == LIST_STATUS_RESULTS) {// 70 or 71, also return a pages item for total available pages
				Log.d("webOb DELTA","in IF getStatus()");

				String data = getData();	
				//Log.d("webOb getGameList data",data);
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				//Log.d("webOb getGameList data 2",data2);
				JSONArray jsonArray = new JSONArray(data2);
				re=jsonArray;
			} else {
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return re;
		}

		return re;
		// 71 = good, 70 = error, 12
	}
	
	public JSONArray CHARLIE(int rosterID) 
			throws InterruptedException {
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();

		try {
			jObSend.put("rosterID", rosterID);
			//jObSend.put("status", gtype);// if not provided everything returned statuses in 30s
			//jObSend.put("page", 1);//if want more games than 25, send page 2
			jObSend.put("module", "challenge");
			jObSend.put("function", "charlie");

			setSendJSON(jObSend);
			t.start();
			t.join();
			Log.d("webOb getGameList","return from t");

			if (getStatus() == LIST_STATUS_RESULTS) {// 70 or 71, also return a pages item for total available pages
				Log.d("webOb getGameList","in IF getStatus()");

				String data = getData();	
				//Log.d("webOb getGameList data",data);
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				//Log.d("webOb getGameList data 2",data2);
				JSONArray jsonArray = new JSONArray(data2);
				re=jsonArray;
			} else {
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return re;
		}

		return re;
		// 71 = good, 70 = error, 12
	}


	public JSONArray ECHO(int chalID) 
			throws InterruptedException {
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();

		try {
			jObSend.put("challengeID", chalID);
			//jObSend.put("status", gtype);// if not provided everything returned statuses in 30s
			//jObSend.put("page", 1);//if want more games than 25, send page 2
			jObSend.put("module", "challenge");
			jObSend.put("function", "echo");

			setSendJSON(jObSend);
			t.start();
			t.join();
			Log.d("webOb getGameList","return from t");

			if (getStatus() == LIST_STATUS_RESULTS) {// 70 or 71, also return a pages item for total available pages
				Log.d("webOb getGameList","in IF getStatus()");

				String data = getData();	
				//Log.d("webOb getGameList data",data);
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				//Log.d("webOb getGameList data 2",data2);
				JSONArray jsonArray = new JSONArray(data2);
				re=jsonArray;
			} else {
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return re;
		}

		return re;
		// 71 = good, 70 = error, 12
	}

	
	public JSONArray getUserChalList(int chalType) {
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("betType", LIST_STATUS_PERSONAL);// 75=my bets, 76 = public open
			jObSend.put("status", chalType);// 20=open public, 21=accepted
			jObSend.put("gameID", 0);		// public, 22=closed public, 25=open
											// friend,26=accepted friend,
											// 27=closed 0=null
			
			// pages coming back
			jObSend.put("module", "challenge");
			jObSend.put("function", "getBetList");
			setSendJSON(jObSend);
			t.start();
			t.join();
			if (getStatus() == LIST_STATUS_RESULTS) {

				String data = getData();			
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				JSONArray jsonArray = new JSONArray(data2);
				re=jsonArray;
			} else {
				re = null;
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			re = null;
		}

		return re;
	}

	public JSONArray getPublicChalList(int homeprime,int awayprime) {
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("status", CHAL_STATUS_OPENPUBLIC); //see above for inputs
			jObSend.put("module", "challenge");
			jObSend.put("function", "getChallengeListRosters");
			jObSend.put("type", LIST_STATUS_PUBLIC);
			jObSend.put("rosterid1", homeprime);
			jObSend.put("rosterid2", awayprime);
			setSendJSON(jObSend);
			t.start();
			t.join();
			if (getStatus() == LIST_STATUS_RESULTS) {

				String data = getData();			
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				JSONArray jsonArray = new JSONArray(data2);
				re=jsonArray;
			} else {
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return re;
	}
	
	public JSONArray ALPHA (int betType) {
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();
		try {
			
			/*
			jObSend.put("betType", LIST_STATUS_PERSONAL);// 75=my bets, 76 = public open
			jObSend.put("status", betType);// 20=open public, 21=accepted
			jObSend.put("gameID", 0);		// public, 22=closed public, 25=open
											// friend,26=accepted friend,
											// 27=closed 0=null
			*/
			// pages coming back
			jObSend.put("module", "challenge");
			jObSend.put("function", "alpha");
			setSendJSON(jObSend);
			t.start();
			t.join();
			if (getStatus() == LIST_STATUS_RESULTS) {

				String data = getData();			
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				JSONArray jsonArray = new JSONArray(data2);
				//writeToFile(jsonArray.toString());
				re=jsonArray;
			} else {
				re = null;
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			re = null;
		}

		return re;
	}
	
	public JSONArray getTeamList(int sportID){
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("sport", sportID);//not needed
			jObSend.put("module", "challenge");
			jObSend.put("function", "getTeamList");
			setSendJSON(jObSend);
			t.start();
			t.join();
			if (getStatus() == LIST_STATUS_RESULTS) {

				String data = getData();				
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				JSONArray jsonArray = new JSONArray(data2);
				re=jsonArray;
			} else {
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return re;
	}

	public JSONArray getFriends() { //NEED TO BUILD AFTER FB INTERACTS
		//returns status 70-71; two columns id, hide, two columns for name
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("module", "friends");
			jObSend.put("function", "getFriends");//need to add modify friends; sends what it needs to be "friendStatus"
			setSendJSON(jObSend);
			t.start();
			t.join();
			if (getStatus() == LIST_STATUS_RESULTS) {
				String data = getData();				
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				JSONArray jsonArray = new JSONArray(data2);
				re=jsonArray;
			} else {
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return re;
	}

	public int makeUser(JSONObject jOB) { //email, pw only
		// send scott all info from FB for original log in and regular
		// authentication
		try {
			jOB.put("module", "user");
			jOB.put("function", "makeUser");
			setSendJSON(jOB);
			t.start();
			t.join();
			return getStatus();

			// VERIFY THAT NO OTHER POSSIBLE STATUS RETURNS
			// 120 - sucessful create; 121 - already existing email; 122 - invalid email format; 123 - insufficient password 
			
		} catch (Exception e) {
			return USER_STATUS_INVALIDEMAIL;
		}
	}

	public int tryLogOn(String uEmail, String pw) {
		//call to get new keys on start up
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("email", uEmail);
			jObSend.put("password", pw);
			jObSend.put("module", "user");
			jObSend.put("function", "tryLogOn");
			setSendJSON(jObSend);
			t.start();
			t.join();
			if(getStatus() == USER_STATUS_SUCCESSFULLOGIN){
				String data = getData();
				JSONObject jObReturn = new JSONObject(data);
				Log.d("status from Json", String.valueOf(status));
				//check status = sucessful login
				String kyA = jObReturn.getString("keyA");
				String kyB = jObReturn.getString("keyB");
				int tkn = Integer.parseInt(jObReturn.getString("tokenID"));
				String salt = jObReturn.getString("salt");
				long exp = Long.parseLong(jObReturn.getString("expiration"));
				int uID = Integer.parseInt(jObReturn.getString("userID"));
				thisUser = uID;
				setToken(tkn);
				setKeyA(kyA);
				setKeyB(kyB);
				setSalt(salt);
				setExpires(exp);
				Log.d("WebContentGet tryLogIn","login values stored");
				session.storeLoginSession(uEmail,pw,uID, tkn, exp, kyA, kyB, salt);
				
			}
			
			return getStatus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getStatus();
		}
		
	}


	public int makeFriends(int friendID) { //send userID of new Friend; 28/29 fail/pass
		try {
			JSONObject f = new JSONObject();
			f.put("friendID", friendID);
			f.put("module", "friends");
			f.put("function", "addFriend");
			setSendJSON(f);
			t.start();
			t.join();
			return getStatus();
		} catch (Exception e) {
			return getStatus();
		}

		
	}
	
	public int getAddFriends(JSONObject f) { //send userID of new Friend; 28/29 fail/pass
		try {
			JSONObject jObject = new JSONObject();//phone key indexing, match index
			jObject.put("contactslist", f);
			jObject.put("module", "friends");
			jObject.put("function", "getAddFriends");
			setSendJSON(jObject);
			t.start();
			t.join();
			return getStatus();
		} catch (Exception e) {
			return getStatus();
		}

		
	}
	
	public int updateFriends(int friend, int setto) { //send userID of new Friend; 28/29 fail/pass
		try {
			JSONObject f = new JSONObject();
			f.put("friendID", friend);
			f.put("toStatus", setto);//140,141
			f.put("module", "friends");
			f.put("function", "updateFriend");
			setSendJSON(f);
			t.start();
			t.join();
			return getStatus();
		} catch (Exception e) {
			return getStatus();
		}

		
	}

	public JSONObject getGame(int GID) { //70/71 return
		JSONObject re = new JSONObject();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("gameID", GID);
			jObSend.put("module", "challenge");
			jObSend.put("function", "getGame");
			jObSend.put("version", versionID);
			setSendJSON(jObSend);
			t.start();
			t.join();
			if(getStatus()==LIST_STATUS_RESULTS){
				String data = getData();
				JSONObject jObReturn;
				jObReturn = new JSONObject(data);
				re = jObReturn;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	
	public JSONObject getChal(int BID) { //70/71 return
		JSONObject re = new JSONObject();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("betID", BID);
			jObSend.put("module", "challenge");
			jObSend.put("function", "getGame");
			jObSend.put("version", versionID);
			setSendJSON(jObSend);
			t.start();
			t.join();
			if(getStatus()==LIST_STATUS_RESULTS){
				String data = getData();
				JSONObject jObReturn;
				jObReturn = new JSONObject(data);
				re = jObReturn;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}

	public int updateUserPersonalInfo(JSONObject jOB) {
		int re = -1;
		try {
			jOB.put("module", "user");
			jOB.put("function", "updatePersonalInfo");
			jOB.put("version", versionID);
			setSendJSON(jOB);
			t.start();
			t.join();
			String data = getData();
			JSONObject jObReturn;
			jObReturn = new JSONObject(data);
			re = Integer.parseInt(jObReturn.getString("status"));
		} catch (Exception e) {

		}

		return re;
	}

	public int updateUserCreditCard(JSONObject jOB) {
		int re = -1;
		try {
			jOB.put("module", "user");
			jOB.put("function", "updateUserCreditCard");
			jOB.put("version", versionID);
			setSendJSON(jOB);
			t.start();
			t.join();
			String data = getData();
			JSONObject jObReturn = new JSONObject(data);
			re = Integer.parseInt(jObReturn.getString("status"));
		} catch (Exception e) {

		}

		return re;
	}

	public int changeUserPassword(JSONObject jOB) {
		int re = -1;
		try {
			jOB.put("module", "user");
			jOB.put("function", "changeUserPassword");
			jOB.put("version", versionID);
			setSendJSON(jOB);
			t.start();
			t.join();
			String data = getData();
			JSONObject jObReturn;
			jObReturn = new JSONObject(data);
			re = Integer.parseInt(jObReturn.getString("status"));
		} catch (Exception e) {

		}

		return re;
	}

	public JSONObject getTeamInfo(int teamID) {
		JSONObject re = new JSONObject();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("teamID", teamID);
			jObSend.put("module", "challenge");
			jObSend.put("function", "getTeamInfo");
			setSendJSON(jObSend);
			t.start();
			t.join();
			//70 71
			if(getStatus()==LIST_STATUS_RESULTS){
				String data = getData();
				int startind = data.indexOf("[") + 1;
				int endind = data.indexOf("]");
				String data2 = data.substring(startind,endind);
				JSONObject jObReturn;
				jObReturn = new JSONObject(data2);
				re = jObReturn;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	
	public JSONObject getRosterInfo(int teamID) {
		JSONObject re = new JSONObject();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("rosterID", teamID);
			jObSend.put("module", "challenge");
			jObSend.put("function", "getRosterInfo");
			setSendJSON(jObSend);
			t.start();
			t.join();
			//70 71
			if(getStatus()==LIST_STATUS_RESULTS){
				String data = getData();
				JSONObject jObReturn;
				int startind = data.indexOf("[") + 1;
				int endind = data.indexOf("]");
				String data2 = data.substring(startind,endind);
				jObReturn = new JSONObject(data2);
				re = jObReturn;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	
	public JSONArray getFullRoster(int teamID) {
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("rosterID", teamID);
			jObSend.put("module", "challenge");
			jObSend.put("function", "getFullRoster");
			setSendJSON(jObSend);
			t.start();
			t.join();
			//70 71
			if(getStatus()==LIST_STATUS_RESULTS){
				String data = getData();
				JSONArray jObReturn;
				int startind = data.indexOf("[");
				int endind = data.indexOf("]")+1;
				String data2 = data.substring(startind,endind);
				jObReturn = new JSONArray(data2);
				re = jObReturn;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	public JSONArray getFullRoster(int[] teamID) {
		JSONArray re = new JSONArray();
		JSONObject jObSend = new JSONObject();
		try {
			for(int i = 0; i<teamID.length;i++){
				String base = "rosterID";
				String unit = String.valueOf(i);
				String addString = base+unit;
				jObSend.put(addString,teamID[i]);
			}
			jObSend.put("module", "challenge");
			jObSend.put("function", "charlie");
			setSendJSON(jObSend);
			t.start();
			t.join();
			//70 71
			if(getStatus()==LIST_STATUS_RESULTS){
				String data = getData();
				JSONArray jObReturn;
				jObReturn = new JSONArray(data);
				re = jObReturn;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	
	public JSONObject getPlayerAssignment(int assID) {
		JSONObject re = new JSONObject();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("AssignID", assID);
			jObSend.put("module", "challenge");
			jObSend.put("function", "getPlayerAssignmentInfo");
			setSendJSON(jObSend);
			t.start();
			t.join();
			//70 71
			if(getStatus()==LIST_STATUS_RESULTS){
				String data = getData();
				JSONObject jObReturn;
				int startind = data.indexOf("[") + 1;
				int endind = data.indexOf("]");
				String data2 = data.substring(startind,endind);
				jObReturn = new JSONObject(data2);
				re = jObReturn;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	public JSONObject getPlayerInfo(int playerID) {
		JSONObject re = new JSONObject();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("playerID", playerID);
			jObSend.put("module", "challenge");
			jObSend.put("function", "getPlayerInfo");
			setSendJSON(jObSend);
			t.start();
			t.join();
			//70 71
			if(getStatus()==LIST_STATUS_RESULTS){
				String data = getData();
				JSONObject jObReturn;
				int startind = data.indexOf("[") + 1;
				int endind = data.indexOf("]");
				String data2 = data.substring(startind,endind);
				jObReturn = new JSONObject(data2);
				re = jObReturn;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	
	public JSONObject getUserName(int userID) {
		JSONObject re = new JSONObject();
		JSONObject jObSend = new JSONObject();
		try {
			jObSend.put("userID", userID);
			jObSend.put("module", "user");
			jObSend.put("function", "getUserName");
			jObSend.put("version", versionID);
			setSendJSON(jObSend);
			t.start();
			t.join();
			String data = getData();
			JSONObject jObReturn;
			jObReturn = new JSONObject(data);
			String first = jObReturn.getString("first");
			String last = jObReturn.getString("last");
			re.put("firstName", first);
			re.put("lastName", last);
			re.put("userID",String.valueOf(userID));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
	
	public int dummyCharge(int amount) {
		int re = -1;
		JSONObject jOB = new JSONObject();
		try {
			jOB.put("module", "fakemoney");
			jOB.put("function", "dummyCharge");
			jOB.put("amount", amount);
			//status accept reject hide
			setSendJSON(jOB);
			t.start();
			t.join();
			return getStatus(); //always 71
		} catch (Exception e) {
			return getStatus();
		}
	}
	
	public JSONArray getGameList(int sportID, int gametype, int pageNum) 
			throws InterruptedException {
		JSONArray re = new JSONArray();
		final int gtype = gametype;
		final int sptID = sportID;
		JSONObject jObSend = new JSONObject();
		Log.d("webOb getGameList","starting try loop");

		try {
			Log.d("webOb getGameList","in try loop");
			jObSend.put("sportID", sptID);
			jObSend.put("status", gtype);// if not provided everything returned statuses in 30s
			jObSend.put("page", pageNum);//if want more games than 25, send page 2
			jObSend.put("module", "challenge");
			jObSend.put("function", "getGameList");

			setSendJSON(jObSend);
			t.start();
			t.join();
			Log.d("webOb getGameList","return from t");

			if (getStatus() == LIST_STATUS_RESULTS) {// 70 or 71, also return a pages item for total available pages
				Log.d("webOb getGameList","in IF getStatus()");

				String data = getData();	
				//Log.d("webOb getGameList data",data);
				String data2 = "["+data.substring(data.indexOf("[")+1, data.indexOf("]"))+"]";
				//Log.d("webOb getGameList data 2",data2);
				JSONArray jsonArray = new JSONArray(data2);
				Log.d("webOb getGameList","json array created");
				re=jsonArray;
			} else {
				Log.d("IN PROCESS T", " ELSE LOOP");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return re;
		}

		return re;
		// 71 = good, 70 = error, 12
	}
	
	public int sendStripeToken(Token token){
		JSONObject sendJ = new JSONObject();
		try {
			sendJ.put("token", token.toString());
			sendJ.put("module", "stripe");
			sendJ.put("function", "postKeys");
			
			setSendJSON(sendJ);
			t.start();
			t.join();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int stat = getStatus();
		return stat;
	}
	
	public int makeCharge(double f){
		JSONObject sendJ = new JSONObject();
		try {
			sendJ.put("amount", f);
			sendJ.put("module", "stripe");
			sendJ.put("function", "charge");
			
			setSendJSON(sendJ);
			t.start();
			t.join();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int stat = getStatus();
		return stat;
	}


	/*
	 * 
	 * 
	 * public long tryFBLogOn(String uEmail){ long re = 0; int len =
	 * allUsers.getUsers() - 1; int index = 0; for (int i = 0; i < len; i++) {
	 * if (allUsers.getUser(i).getEmail().equals(uEmail)) { re =
	 * allUsers.getUser(i).getUserID(); break; } }
	 * 
	 * return re; }
	 */

	protected String getData() {
		return dataLine;
	}

	protected void setData(String s) {
		dataLine = s;
	}

	private int getVersionID(){
		return versionID;
	}

	private void setStatus(int tkn) {
		status = tkn;
	}

	private int getStatus() {
		return status;
	}

	private void setSendJSON(JSONObject send) {
		sendObject = send;
	}

	private JSONObject getSendJSON() {
		return sendObject;
	}
	
	/*protected void sendJson(final String email, final String pwd)
			throws InterruptedException {
		Thread t = new Thread() {

			public void run() {
				Log.d("webcontent", "in sendJson run");
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;
				JSONObject json = new JSONObject();
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				try {
					HttpPost post = new HttpPost(URL);

					json.put("module", "user");
					json.put("version", 1);
					json.put("email", email);
					json.put("password", pwd);
					json.put("function", "tryLogOn");

					nameValuePairs.add(new BasicNameValuePair("json", json
							.toString()));
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					response = client.execute(post);

					// Checking response 
					if (response != null) {
						// InputStream in = response.getEntity().getContent();
						// //Get the data in the entity
						BufferedReader in = new BufferedReader(
								new InputStreamReader(response.getEntity()
										.getContent()));
						StringBuffer sb = new StringBuffer("");
						String l = "";
						String nl = System.getProperty("line.separator");

						while ((l = in.readLine()) != null) {
							sb.append(l + nl);

						}
						in.close();
						String data = sb.toString();
						Log.d("response not null", "aa");

						Log.d("respose not null", data);
						String data2 = data.substring(data.indexOf("{"));
						Log.d("trimmed data", data2);
						

					}

				} catch (Exception e) {
					e.printStackTrace();
					// createDialog("Error", "Cannot Estabilish Connection");
				}

				Looper.loop(); // Loop in the message queue
			}

		};

		t.start();
	}*/
	
	private void writeToFile(String data) {
	    try {
	    	String path1 = System.getProperty ("user.home") + "/Desktop";
	    	String path2 = "/Users/bendickerson/Documents/HTS";
	    	File file = new File(path2 + "/alpha1output.rtf");
	    	FileOutputStream stream = new FileOutputStream(file);
	    	try {
	    	    stream.write(data.getBytes());
	    	} finally {
	    	    stream.close();
	    	}
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}

}
