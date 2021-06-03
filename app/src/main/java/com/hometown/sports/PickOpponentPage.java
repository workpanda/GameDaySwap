package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PickOpponentPage extends MenuBarActivity implements
		View.OnClickListener, SimpleGestureListener {
	Button button1;
	private ProgressDialog pDialog;
	private SimpleGestureFilter detector;
	JSONArray friendsin = null;
	userList friends = new userList(1);
	userList phoneContacts = new userList(1);
	ArrayList<HashMap<String, String>> friendslist;
	int friendSelected;
	ListView lv;

	String name;
	String home;
	String away;
	double line;
	int wager;
	int winner, homer1, homer2, awayr1, awayr2, homep1, homep2, awayp1, awayp2;
	int gameID;
	int selectedFriendIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pickopponent);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		button1 = (Button) findViewById(R.id.SendChal);
		button1.setOnClickListener(this);
		friendslist = new ArrayList<HashMap<String, String>>();
		detector = new SimpleGestureFilter(this, this);

		TextView gameheading = (TextView) findViewById(R.id.ChosenGame);
		TextView lblwinner = (TextView) findViewById(R.id.ChosenWinner);
		final TextView lblfriend = (TextView) findViewById(R.id.chosenFriend);
		Intent in = getIntent();
		homer1 = in.getIntExtra(TAG_HOME1, 0);
		homer2 = in.getIntExtra(TAG_HOME2, 0);
		awayr1 = in.getIntExtra(TAG_AWAY1, 0);
		awayr2 = in.getIntExtra(TAG_AWAY2, 0);
		homep1 = in.getIntExtra(TAG_HOMEP1, 0);
		homep2 = in.getIntExtra(TAG_HOMEP2, 0);
		awayp1 = in.getIntExtra(TAG_AWAYP1, 0);
		awayp2 = in.getIntExtra(TAG_AWAYP2, 0);
		winner = in.getIntExtra(TAG_WINNER, 0);
		line = in.getDoubleExtra(TAG_LINE, 0.5);
		wager = in.getIntExtra(TAG_VALUE, 1);

		int homeRID = selectedChal.getHomeRoster();
		int awayRID = selectedChal.getAwayRoster();
		int homeTID = currentRosterList.getRosterByID(homeRID).getTeamID();
		int awayTID = currentRosterList.getRosterByID(awayRID).getTeamID();
		Team homeT = currentTeamList.getTeamByID(homeTID);
		Team awayT = currentTeamList.getTeamByID(awayTID);
		home = homeT.getDisplayName();
		away = awayT.getDisplayName();
		name = away + " at " + home;

		String winnerStr = "";
		String lineStr = "";
		String wagerStr = "";
		String secondLine = "";

		switch (winner) {
		case (WINNER_STATUS_HOME):
			winnerStr = home;
			if (line > 0) {
				lineStr = "+" + String.valueOf(line);
			} else {
				lineStr = "" + String.valueOf(line);
			}
			break;
		case (WINNER_STATUS_AWAY):
			winnerStr = away;
			if (line > 0) {
				lineStr = "+" + String.valueOf(line);
			} else {
				lineStr = String.valueOf(line);
			}
			break;
		case (WINNER_STATUS_OVER):
			winnerStr = "Over";
			lineStr = String.valueOf(line);
			break;
		case (WINNER_STATUS_UNDER):
			winnerStr = "Under";
			lineStr = String.valueOf(line);
			break;
		}

		wagerStr = "$" + String.valueOf(wager);

		secondLine = winnerStr + " " + lineStr + " for " + wagerStr;
		gameheading.setText(name);
		lblwinner.setText(secondLine);
		lblfriend.setText("Choose a friend to challenge");

		lv = (ListView) findViewById(R.id.opponentList);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				lblfriend.setText(friends.getUser(position).getName());

				selectedFriendIndex = position;

			}
		});

		new GetFriends().execute();

	}
	
	public void onResume(){
		super.onResume();
		currentActivity = this;
	}

	@Override
	public void onClick(View v) {
		int UserB = friends.getUser(selectedFriendIndex).getUserID();

		selectedChal.setAccepter(UserB);

		final Context context = this;

		Intent intent1 = new Intent(context, MakeChalConfirmation.class);
		intent1.putExtra(TAG_ACCEPTER, UserB);
		intent1.putExtra(TAG_HOME1, homer1);
		intent1.putExtra(TAG_HOME2, homer2);
		intent1.putExtra(TAG_AWAY1, awayr1);
		intent1.putExtra(TAG_AWAY2, awayr2);
		intent1.putExtra(TAG_HOMEP1, homep1);
		intent1.putExtra(TAG_HOMEP2, homep2);
		intent1.putExtra(TAG_AWAYP1, awayp1);
		intent1.putExtra(TAG_AWAYP2, awayp2);
		intent1.putExtra(TAG_WINNER, winner);
		intent1.putExtra(TAG_LINE, line);
		intent1.putExtra(TAG_VALUE, wager);
		intent1.putExtra(TAG_STATUS, CHAL_STATUS_OPENFRIEND);
		startActivity(intent1);

	}

	private class GetFriends extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(PickOpponentPage.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// ReadPhoneContacts(PickOpponentPage.this);
			if (Looper.myLooper() == null)
				Looper.prepare();

			JSONArray retFriends = new JSONArray();
			WebContentGet webOb = new WebContentGet();
			retFriends = webOb.getFriends();

			// NEED AN IF EMPTY SCENARIO
			Log.d("retFriends length", String.valueOf(retFriends.length()));
			for (int i = 0; i < retFriends.length(); i++) {
				// long phone = 0;
				try {
					JSONObject jo = retFriends.getJSONObject(i);
					Friendship addFriendship = new Friendship(jo);
					currentFriendList.push(addFriendship);
					if (addFriendship.getUserID(1) == thisUser) {
						if (addFriendship.getStatus(2) == FRIEND_STATUS_SHOW) {
							User adduse = new User(addFriendship.getUserID(2),
									true, jo);
							friends.push(adduse);
							currentUserList.push(adduse);
						}
					} else {
						if (addFriendship.getStatus(1) == FRIEND_STATUS_SHOW) {
							User adduse = new User(addFriendship.getUserID(1),
									false, jo);
							friends.push(adduse);
							currentUserList.push(adduse);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if (thisUser != 2 && !friends.contains(2)) {
				User addUse = new User("David", "Cagle", 2);
				friends.push(addUse);
			}
			if (thisUser != 3 && !friends.contains(3)) {
				User addUse = new User("Ben", "Dickerson", 3);
				friends.push(addUse);
			}
			
			friends.sortFName();
			for (int i = 0; i<friends.getUsers(); i++){
				HashMap<String, String> friendstring = new HashMap<String, String>();
				friendstring.put("name", friends.getUser(i).getName());
				friendslist.add(friendstring);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(PickOpponentPage.this,
					friendslist, R.layout.gamelist, new String[] { "name" },
					new int[] { R.id.name });

			lv.setAdapter(adapter);
		}

	}

	public void ReadPhoneContacts(Context cntx) // This Context parameter is
	// nothing but your Activity
	// class's Context
	{
		User addUser1;
		Cursor cursor = cntx.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		Integer contactsCount = cursor.getCount(); // get how many contacts you
		// have in your contacts
		// list
		if (contactsCount > 0) {
			while (cursor.moveToNext()) {
				String id = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				String contactName = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				Log.d("contactName", contactName);
				if (Integer
						.parseInt(cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					// the below cursor will give you details for multiple
					// contacts
					Cursor pCursor = cntx.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					// continue till this cursor reaches to all phone numbers
					// which are associated with a contact in the contact list
					while (pCursor.moveToNext()) {
						int phoneType = pCursor
								.getInt(pCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
						// String isStarred =
						// pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
						String phoneNo = pCursor
								.getString(pCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						// you will get all phone numbers according to it's type
						// as below switch case.
						// Logs.e will print the phone number along with the
						// name in DDMS. you can use these details where ever
						// you want.

						phoneNo = phoneNo.replaceAll("[^0-9]+", "");
						Log.d("phone Num", phoneNo);

						// probably need to screen out +,1,etc.
						long phoneInt = Long.parseLong(phoneNo);

						Log.d("making addUser1", contactName);

						addUser1 = new User(contactName, "", -1, phoneInt);
						phoneContacts.push(addUser1);
						Log.d("pushed addUser1 to phoneContacts",
								addUser1.getFName());
						Log.d("pushed addUser 1 to contacts - phone:",
								String.valueOf(addUser1.getPhone()));
					}
					pCursor.close();
				}
			}
			cursor.close();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
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
	}

}