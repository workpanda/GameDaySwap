package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Friendship extends MenuBarActivity{
	    public int ID1;
	    public int ID2;
	    public int status1;
	    public int status2;


	    Friendship(User user1, User user2){
	        ID1 = user1.getUserID();
	        ID2 = user2.getUserID();
	        status1 = FRIEND_STATUS_SHOW;
	        status2 = FRIEND_STATUS_SHOW;
	    }

	    Friendship(int user1, int user2){
	        ID1 = user1;
	        ID2 = user2;
	        status1 = FRIEND_STATUS_SHOW;
	        status2 = FRIEND_STATUS_SHOW;
	    }
	    
	    Friendship(int user1, int user2, int s1, int s2){
	        ID1 = user1;
	        ID2 = user2;
	        status1 = s1;
	        status2 = s2;
	    }
	    
	    Friendship(JSONObject j){
	    	try{
	    		ID1 = Integer.parseInt(j.getString("friendSender"));
		        ID2 = Integer.parseInt(j.getString("friendReceiver"));;
		        status1 = Integer.parseInt(j.getString("hideSender"));;
		        status2 = Integer.parseInt(j.getString("hideReceiver"));;
	    	} catch (Exception e){
	    		Log.d("Friendship constructor","JSONERROR");
	    	}
	    }

	    public int[] getUserIDs(){
	        int[] re = new int[2];
	            re[0] = ID1;
	            re[1] = ID2;
	        return re;
	    }
	    
	    public int getUserID(int i){
	        switch(i){
	        case 1: 
	        	return ID1;
	        case 2:
	        	return ID2;
	        }
	        return 0;
	    }
	    
	    public int getStatus(int i){
	        switch(i){
	        case 1: 
	        	return status1;
	        case 2:
	        	return status2;
	        }
	        return 0;
	    }



	    void makeJSON(JSONObject jOb){
	        try {
	            jOb.put("userID1", ID1);
	            jOb.put("userID2", ID2);
	            jOb.put("status1",  status1);
	            jOb.put("status2",  status2);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }


}
