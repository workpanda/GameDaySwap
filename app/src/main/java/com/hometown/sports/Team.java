package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

public class Team {

	int teamID;
	String location;
	String mascot;
	String letters;
	int sportID;
	int sportSubID;
	int rank;
	String logo;
	
	Team(int tid, String loc, String mas, String let, int spID, int spsID, int rnk, String lgo){
		teamID = tid;
		location = loc;
		letters = let;
		mascot = mas;
		sportID = spID;
		sportSubID=spsID;
		rank = rnk;
		logo = lgo;
	}
	
	Team(JSONObject j){
		try{
			teamID = Integer.parseInt(j.getString("TeamID"));
			location = j.getString("City");
			letters = j.getString("letters");
			mascot = j.getString("Name");
			sportID = Integer.parseInt(j.getString("SportID"));
			sportSubID=Integer.parseInt(j.getString("sportSubID"));
			rank = Integer.parseInt(j.getString("rank"));
			logo = j.getString("URL");
		} catch (Exception e){
			
		}
	}
	
	int getSport(){
		return sportID;
	}
	
	int getTeamID(){
		return teamID;
	}
	
	String getLocation(){
		return location;
	}
	
	String getMascot(){
		return mascot;
	}
	
	int getSportSubID(){
		return sportSubID;
	}
	
	int getRank(){
		return rank;
	}
	
	void setRank(int rnk){
		rank = rnk;
	}
	
	void setLogo(String lgo){
		logo = lgo;
	}
	
	String getLogo(){
		return logo;
	}
	
	String getLetters(){
		return letters;
	}
	
	void setSportSubID(int spsid){
		sportSubID = spsid;
	}
	
	void makeJSON(JSONObject jOb){
        try {
            jOb.put("teamID", teamID);
            jOb.put("location", location);
            jOb.put("mascot", mascot);
            jOb.put("logo", logo);
            jOb.put("rank", rank);
            jOb.put("sportID", sportID);
            jOb.put("sportSubID", sportSubID);
            jOb.put("letters", letters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
	
	String getDisplayName(){
		String dispA = "";
		if(sportID>4){
			if (rank>0)dispA = "(" + String.valueOf(rank) + ") " + location;
			else dispA = location;
		}
		else{
			dispA = mascot;
		}	
		return dispA;
	}
}
