package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

public class Game extends MenuBarActivity{

	int gameID;
    int teamA;
    int teamH;
    int sportID;
    public int lineWins[] = new int[4];
    long time = 0;
    int date = 0;
    int homeScore;
    int awayScore;
    int status;

    Game(int home, int away, int gameID2, int spid){
        gameID=gameID2;
        sportID = spid;
        //NEED TO LOOK UP IDNum when creating Game
        teamA = away;
        teamH = home;
        homeScore = 2000;
        awayScore = 2000;

        //WRITE TO TEXT FILE
        //Push to Upcoming games
    }
    
    Game(int home, int away, int gameID2, int spid, long tme, int stat){
        gameID=gameID2;
        sportID = spid;
        //NEED TO LOOK UP IDNum when creating Game
        teamA = away;
        teamH = home;
        time = tme;
        status = stat;
        homeScore = 2000;
        awayScore = 2000;

        //WRITE TO TEXT FILE
        //Push to Upcoming games
    }

    int getID(){
        return gameID;
    }

    int getHomeID(){
        return teamH;
    }

    int getAwayID(){
        return teamA;
    }
    
    String getDisplay(){
    	String dispA = currentTeamList.getTeamByID(currentRosterList.getRosterByID(teamA).getTeamID()).getDisplayName();
    	String dispH = currentTeamList.getTeamByID(currentRosterList.getRosterByID(teamH).getTeamID()).getDisplayName();
		

		return dispA + " at " + dispH;
    }
    
    long getTime(){
    	return time;
    }
    
    int getStatus(){
    	return status;
    }
    
    
    int getSport(){return sportID;}
    
    void updateScore(int homeScr, int awayScr, int period, int tme){
    	homeScore = homeScr;
    	awayScore = awayScr;
    	date = period;
    	time = tme;
    }


    void makeJSON(JSONObject jOb){
        try {
            jOb.put("gameID", gameID);
            jOb.put("awayTeam", teamA);
            jOb.put("homeTeam", teamH);
            jOb.put("sportID", sportID);
            jOb.put("time", time);
            jOb.put("date", date);
            jOb.put("status", status);
            jOb.put("homeScore", homeScore);
            jOb.put("awayScore", awayScore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
