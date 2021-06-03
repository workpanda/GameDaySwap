package com.hometown.sports;

import org.json.JSONObject;

import android.util.Log;


public class Roster extends MenuBarActivity{

	int rosterID;
	int selectedPlayerID;
	int teamID;
	int status;
	long time;
	
	Roster(int ID, int tm, long tim, int selPlay){
		teamID = tm;
		time = tim;
		rosterID = ID;
		selectedPlayerID = 0;
		status = selPlay;
	}
	
	Roster(JSONObject j){
		try{
		teamID = Integer.parseInt(j.getString("teamID"));
		time = Long.parseLong(j.getString("timestamp"));
		rosterID = Integer.parseInt(j.getString("rosterID"));
		status = Integer.parseInt(j.getString("status"));
		} catch (Exception e){
			
		}
	}
	
	int getID(){
		return rosterID;
	}
	
	long getTime(){
		return time;
	}
	
    int getTeamID(){
    	return teamID;
    }
    
    public int getSelectedPlayer(){
    	return selectedPlayerID;
    }
    
    public void setSelectedPlayer(int i){
    	selectedPlayerID = i;
    }
    
    public int getStatus(){
    	return status;
    }
    
    public void setStatus(int i){
    	status = i;
    }
    
    public rosterAssignList getSortedRoster(){
    	rosterAssignList locked = new rosterAssignList(1);
    	rosterAssignList unlocked = new rosterAssignList(1);
    	rosterAssignList total = currentAssignList.getFullRoster(this.rosterID);
    	for(int i = 0; i<total.getNumAssigns();i++){
    		RosterAssign checkAssign = total.getAssign(i);
    		if (checkAssign.getStatus()==ASSIGN_STATUS_UNLOCKED){
    			unlocked.push(checkAssign);
    		}
    		if (checkAssign.getStatus()==ASSIGN_STATUS_LOCKED){
    			locked.push(checkAssign);
    		}
    	}
    	locked.sortPlayerNum();
    	unlocked.sortPlayerNum();
    	for(int j = 0; j<unlocked.getNumAssigns();j++){
    		locked.push(unlocked.getAssign(j));
    	}
    	
    	return locked;
    }
}
