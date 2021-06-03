package com.hometown.sports;

import org.json.JSONObject;

import android.util.Log;

public class Player {

	static final int PLAYER_LOCKED_IN = 1;
	static final int PLAYER_UNLOCK_IN_HOME = 2;
	static final int PLAYER_UNLOCK_OUT_HOME = 5;
	static final int PLAYER_UNLOCK_IN_AWAY = 3;
	static final int PLAYER_UNLOCK_OUT_AWAY = 6;
	
	String name;
	String position;
	int ID;
	int team;
	int number;
	int status;
	int points;
	
	Player(){
		name = "";
		position = "";
		ID=0;
		team = 0;
		number = -1;
		status = -1;
		points = 0;
		
	}
	
	Player(int id, String nme, String pos, int tm, int num, int stat){
		ID=id;
		name = nme;
		position = pos;
		team = tm;
		number = num;
		status = stat;
		points = 0;
	}
	
	Player(JSONObject jOb){
		try{
			ID = Integer.parseInt(jOb.getString("playerID"));
			name = jOb.getString("name");
			position = jOb.getString("position");
			number = Integer.parseInt(jOb.getString("jersey"));
			status = Integer.parseInt(jOb.getString("statusP"));
			team = Integer.parseInt(jOb.getString("teamID"));
			//points = Integer.parseInt(jOb.getString("points"));
		}catch(Exception e){
			Log.d("Player create","JSON ERROR");
		}
	}
	
	public int getID(){
		return ID;
	}
	
	public int getStatus(){
		return status;
	}
	
	public void setStatus(int i){
		status = i;
	}
	
	public int getNumber(){
		return number;
	}
	
	public String getName(){
		return name;
	}
	
	public int getTeam(){
		return team;
	}
	
	public String getPosition(){
		return position;
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setPoints(int p){
		points = p;
	}

}
