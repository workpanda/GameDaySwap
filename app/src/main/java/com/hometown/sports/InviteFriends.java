package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hometown.sports.SimpleGestureFilter.SimpleGestureListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class InviteFriends extends MenuBarActivity implements
		SimpleGestureListener {
	private ProgressDialog pDialog;
	ListView friendsListView;
	Switch friendSelector;
	private SimpleGestureFilter detector;
	boolean isInvite = true;
	userList myFriends = new userList(1);
	userList myContacts = new userList(1);
	userList phoneContacts = new userList(1);
	userList allContacts = new userList(1);
	User selectedUser;
	ArrayList<HashMap<String, String>> friendsArray;
	ArrayList<HashMap<String, String>> contactsArray;
	ListAdapter friendAdapter;
	ListAdapter contactAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitefriendslayout);
		detector = new SimpleGestureFilter(this, this);
		friendsListView = (ListView) findViewById(R.id.friendslist);
		friendSelector = (Switch) findViewById(R.id.friendswitch);
		friendsArray = new ArrayList<HashMap<String, String>>();
		contactsArray = new ArrayList<HashMap<String, String>>();

		friendsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// DO ON CLICK
				if (isInvite) {
					selectedUser = myContacts.getUser(position);

					if (selectedUser.getFriend() == FRIEND_STATUS_MEMBER) {
						JSONObject f = new JSONObject();
						try {
							f.put("userID1", thisUser);
							f.put("userID2", selectedUser.getUserID());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						MakeFriend mf = new MakeFriend(f,1);
						mf.execute();
						selectedUser.setFriend(FRIEND_STATUS_SHOW);
						myFriends.push(selectedUser);
						myFriends.sortFName();
						myContacts.pop(selectedUser.getUserID());
						setFriendDisplay();
						friendsListView.setAdapter(contactAdapter);
					} else {
						selectedUser.setFriend(FRIEND_STATUS_SHOW);

						String phoneNumber = String.valueOf(selectedUser
								.getPhone());
						String sendText = userFName
								+ " would like to invite you to join HomeTown, a new way to challenge your friends in fantasy sports.  Follow the link______";

						sendSMS(phoneNumber, sendText);
						setFriendDisplay();
						friendsListView.setAdapter(contactAdapter);
						// send friend status show to server

					}
					// move to send SMS out to phone number with linked invite

					// need to talk with scott on how to set up a temporary
					// phone only user
					// move user from myContacts to myFriends

				} else {
					selectedUser = myFriends.getUser(position);

					if (selectedUser.getFriend() == FRIEND_STATUS_SHOW) {
						showDialog(InviteFriends.this, "Hide Friend?",
								"Do you really want to hide this friend from your HometTown activities?");
					} else {
						selectedUser.setFriend(FRIEND_STATUS_SHOW);
						JSONObject f = new JSONObject();
						try {
							f.put("userID1", thisUser);
							f.put("userID2", selectedUser.getUserID());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// send friend status show to server
						MakeFriend mf2 = new MakeFriend(f,2);
						mf2.execute();
						setFriendDisplay();
						friendsListView.setAdapter(friendAdapter);
						

					}
					// send switchFriendHide(oppID) to server, update user
					// friend status, change icon

				}

			}
		});

		friendSelector
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {
							isInvite = true;
							friendsListView.setAdapter(contactAdapter);
							Log.d("onCreate", "setAdapter2");
							// switch is ON, set invite list
						} else {
							isInvite = false;
							friendsListView.setAdapter(friendAdapter);
							Log.d("onCreate", "setAdapter3");
							// switch is OFF, set current list
						}

					}

				});

		new GetFriends().execute();

		friendSelector.setChecked(true);
		friendsListView.setAdapter(contactAdapter);
		Log.d("onCreate", "setAdapter1");

		if (friendSelector.isChecked()) {
			isInvite = true;
			friendsListView.setAdapter(contactAdapter);
			// SWITCH ON SET invite list
		} else {
			isInvite = false;
			friendsListView.setAdapter(friendAdapter);
			// switch OFF set current list
		}
		
		

	}
	
	public void onResume(){
		super.onResume();
		String ttl = "Invite Friends:";
		String txt = "Select a friend to invite to HomeTown Sports, or manage your HTS friends";
		setBottomTitle(ttl);
		setBottomText(txt);
		setBottomInt(0);
		currentActivity = this;
	}
	
	private class MakeFriend extends AsyncTask<Void, Void, Void> {
		
		JSONObject f;
		int type;
		MakeFriend(JSONObject j, int t){
			f = j;
			type=t;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(InviteFriends.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			WebContentGet webOb = new WebContentGet();
			try {
				f.put("type", type);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//webOb.makeFriends(f);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

		}
	}

	private class GetFriends extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(InviteFriends.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(Looper.myLooper()==null)	Looper.prepare();
			ReadPhoneContacts(InviteFriends.this);
			Log.d("in aSync background", "after readPhoneContacts");
			// may need to put a thread counter in here to avoid jumping ahead
			JSONArray phoneJar = new JSONArray();
			Log.d("length of phoneContacts1",
					String.valueOf(phoneContacts.getUsers()));
			for (int i = 0; i < phoneContacts.getUsers() - 1; i++) {
				JSONObject addJSON = new JSONObject();
				Log.d("phoneContacts loop for JSONmake",
						phoneContacts.getUser(i).getFName());
				phoneContacts.getUser(i).makeShareJSON(addJSON);
				phoneJar.put(addJSON);
			}
			try {
				String check1 = phoneJar.getJSONObject(0)
						.getString("firstName");
				String check2 = phoneJar.getJSONObject(1)
						.getString("firstName");
				String check3 = phoneJar.getJSONObject(2)
						.getString("firstName");
				String check4 = phoneJar.getJSONObject(3)
						.getString("firstName");
				Log.d("check1", check1);
				Log.d("check2", check2);
				Log.d("check3", check3);
				Log.d("check4", check4);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JSONArray retFriends = new JSONArray();
			
			
			//retFriends = Buddybets.ourServer.getFriendsServer(
			//		Buddybets.c.thisUser, phoneJar);
			
			
			
			
			
			
			
			
			// NEED A SERVER VERSION OF THIS
			//NEED AN IF EMPTY
			
			
			
			
			
			
			
			
			
			
			//Log.d("Length retFriends", String.valueOf(retFriends.length()));
			for (int i = 0; i < retFriends.length(); i++) {
				//Log.d("in retFriends", String.valueOf(i));
				int uId = -1;
				String fname = "";
				String lname = "";
				int friendStat = 0;
				long phone = 0;
				try {
					JSONObject jo = retFriends.getJSONObject(i);
					fname = jo.getString("firstName");
					lname = jo.getString("lastName");
					uId = Integer.parseInt(jo.getString("userID"));
					phone = Long.parseLong(jo.getString("phone"));
					friendStat = Integer.parseInt(jo.getString("friendStatus"));
					Log.d("frist Name", fname);
					Log.d("friendStatus", String.valueOf(friendStat));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				User addUse = new User(fname, lname, uId, phone);
				addUse.setFriend(friendStat);
				allContacts.push(addUse);

			}

			setFriendDisplay();

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
			// set friends and contacts lists
			Log.d("postExecute", "create adapters");
			friendAdapter = new FriendListAdapter(InviteFriends.this,
					R.layout.friendslist, friendsArray);

			contactAdapter = new FriendListAdapter(InviteFriends.this,
					R.layout.friendslist, contactsArray);

			friendSelector.setChecked(true);
			friendsListView.setAdapter(contactAdapter);

		}
	}

	// probably move to menubar
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

	public void setFriendDisplay() {
		Log.d("setFriendDisplay", "starting");
		Log.d("setFriendDisplay", "startThread");
		friendsArray.clear();
		contactsArray.clear();
		User addUse;

		for (int i = 0; i < allContacts.getUsers() - 1; i++) {

			addUse = allContacts.getUser(i);
			int friendStat = addUse.getFriend();

			switch (friendStat) {
			case FRIEND_STATUS_SHOW:
				Log.d("in friendStat switch", "SHOW");
				myFriends.push(addUse);
				HashMap<String, String> friendstring = new HashMap<String, String>();
				friendstring.put("name", addUse.getName());
				friendstring.put("details",
						"Click to hide info from this friend");
				int logo = getResources().getIdentifier(
						"drawable/ic_filled_plus", "drawable/ic_filled_plus",
						getPackageName());
				friendstring.put("icon", String.valueOf(logo));
				friendsArray.add(friendstring);

				break;
			case FRIEND_STATUS_HIDE:
				Log.d("in friendStat switch", "HIDE");
				myFriends.push(addUse);
				HashMap<String, String> friendstring2 = new HashMap<String, String>();
				friendstring2.put("name", addUse.getName());
				friendstring2.put("details",
						"Click to show info from this friend");
				logo = getResources().getIdentifier("drawable/ic_empty_plus",
						"drawable/ic_empty_plus", getPackageName());
				friendstring2.put("icon", String.valueOf(logo));
				friendsArray.add(friendstring2);

				break;
			case FRIEND_STATUS_MEMBER:
				Log.d("in friendStat switch", "MEMBER");
				// add group
				myContacts.push(addUse);
				HashMap<String, String> contactString = new HashMap<String, String>();
				contactString.put("name", addUse.getName());
				contactString.put("details",
						"Click to add to your HomeTown friends");
				logo = getResources().getIdentifier("drawable/ic_empty_plus",
						"drawable/ic_empty_plus", getPackageName());
				contactString.put("icon", String.valueOf(logo));
				contactsArray.add(contactString);

				break;
			default:
				Log.d("in friendStat switch", "DEFAULT");
				// INVITE group
				myContacts.push(addUse);
				HashMap<String, String> contactString2 = new HashMap<String, String>();
				contactString2.put("name", addUse.getName());
				contactString2.put("details", "Click to Invite to HomeTown");
				logo = getResources().getIdentifier("drawable/ic_empty_plus",
						"drawable/ic_empty_plus", getPackageName());
				contactString2.put("icon", String.valueOf(logo));
				contactsArray.add(contactString2);
			}
		}
		Log.d("setFriendDisplay", "endCode");

		Log.d("setFriendDisplay", "leaving");

	}

	public void showDialog(Activity activity, String title, CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("Hide",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						selectedUser.setFriend(FRIEND_STATUS_HIDE);
						JSONObject f = new JSONObject();
						try {
							f.put("userID1", thisUser);
							f.put("userID2", selectedUser.getUserID());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// send friend status show to server
						MakeFriend mf2 = new MakeFriend(f,3);
						mf2.execute();
						setFriendDisplay();
						friendsListView.setAdapter(friendAdapter);
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// close box
					}
				});
		builder.show();
	}

	protected void sendSMS(String numberIn, String sendTextBody) {
		Log.i("Send SMS", "");

		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("smsto:"));
		smsIntent.setType("vnd.android-dir/mms-sms");

		smsIntent.putExtra("address", new String(numberIn));
		smsIntent.putExtra("sms_body", sendTextBody);
		smsIntent.putExtra("exit_on_sent", true);
		try {
			startActivity(smsIntent);
			finish();
			Log.i("Finished sending SMS...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(InviteFriends.this,
					"SMS faild, please try again later.", Toast.LENGTH_SHORT)
					.show();
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

// add to manifest
// <uses-permission
// android:name="android.permission.READ_CONTACTS"></uses-permission>

