package com.hometown.sports;

import org.json.JSONObject;

import android.util.Log;

public class RosterAssign extends MenuBarActivity{
	
	int assignID;
	int playerID;
	int rosterID;
	int status;
	int points;
	
	
	RosterAssign(int paid, int pid, int rid, int stat, int pt){
		assignID = paid;
		playerID = pid;
		rosterID = rid;
		status = stat;
		points = pt;
	}
	
	RosterAssign(JSONObject j){
		try{
			assignID = Integer.parseInt(j.getString("paID"));
			playerID = Integer.parseInt(j.getString("playerID"));
			rosterID = Integer.parseInt(j.getString("rosterID"));
			status = Integer.parseInt(j.getString("statusPA"));
			points = Integer.parseInt(j.getString("points"));
		} catch (Exception e){
			Log.d("RosterAssign constructor","JSONERROR");
		}
	}
	
	int getPoints(){
		return points;
	}
	
	int getStatus(){
		return status;
	}
	
	void setStatus(int i){
		status = i;
	}
	
	int getRoster(){
		return rosterID;
	}
	
	int getPlayer(){
		return playerID;
	}
	
	int getID(){
		return assignID;
	}
	
	boolean isLocked(){
		if(status == ASSIGN_STATUS_LOCKED) return true;
		else return false;
	}

}
