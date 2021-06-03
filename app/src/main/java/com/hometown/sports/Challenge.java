package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class Challenge extends MenuBarActivity{
	
	int chalID;
	double wager;
	int winner;
	int Challenger;
	int Accepter;
	double line;
	int status;
	String result;
	int homeRosterID;
	int awayRosterID;
	int homePlayer1;
	int homePlayer2;
	int awayPlayer1;
	int awayPlayer2;
	int homeRoster2;
	int awayRoster2;
	
	Challenge(int chid, int userC, int userA, int hm, int hmdrp, int hmadd, int aw, int awdrp, int awadd, int wnr, double lne, double wgr, int stat){
		wager = wgr;
		Challenger = userC;
		Accepter = userA;
		winner = wnr;
		line = lne;
		chalID = chid;
		homeRosterID = hm;
		awayRosterID = aw;
		homePlayer2 = hmadd;
		homePlayer1 = hmdrp;
		awayPlayer2 = awadd;
		awayPlayer1 = awdrp;
		homeRoster2 = 0;
		awayRoster2 = 0;
		status = stat;
	}
	
	Challenge(int chid, int userC, int userA, int hm, int hmdrp, int hm2,int hm2dp, int aw, int awdrp, int aw2,int aw2dp,  int wnr, double lne, double wgr, int stat){
		wager = wgr;
		Challenger = userC;
		Accepter = userA;
		winner = wnr;
		line = lne;
		chalID = chid;
		homeRosterID = hm;
		awayRosterID = aw;
		homeRoster2 = hm2;
		homePlayer1 = hmdrp;
		homePlayer2 = hm2dp;
		awayRoster2 = aw2;
		awayPlayer1 = awdrp;
		awayPlayer2 = aw2dp;
		status = stat;
	}
	
	Challenge(JSONObject j){
		try{
			wager = Double.parseDouble(j.getString("wager"));
			Challenger = Integer.parseInt(j.getString("Challenger"));
			Accepter = Integer.parseInt(j.getString("Accepter"));
			winner = Integer.parseInt(j.getString("winner"));
			line = Double.parseDouble(j.getString("line"));
			chalID = Integer.parseInt(j.getString("challengeID"));
			homeRosterID = Integer.parseInt(j.getString("homeRosterID"));
			awayRosterID = Integer.parseInt(j.getString("awayRosterID"));
			awayPlayer1 = Integer.parseInt(j.getString("awayPlayerDrop"));
			homePlayer1 = Integer.parseInt(j.getString("homePlayerDrop"));
			awayPlayer2 = Integer.parseInt(j.getString("awayPlayerAdd"));
			homePlayer2 = Integer.parseInt(j.getString("homePlayerAdd"));
			status = Integer.parseInt(j.getString("status"));;
		} catch (Exception e){
			try{
			homeRoster2 = Integer.parseInt(j.getString("homeRosterID2"));
			awayRoster2 = Integer.parseInt(j.getString("awayRosterID2"));
			homePlayer2 = Integer.parseInt(j.getString("homeRoster2Add"));
			awayPlayer2 = Integer.parseInt(j.getString("awayRoster2Add"));
			}catch(Exception e1){
				Log.d("Build Chal from JSON","error");
			}
		} 
	}

	int getChalID() {
		return chalID;
	}

	void setStatus(int stat) {
		status = stat;
	}

	int getStatus() {
		return status;
	}

	int getChallenger() {
		return Challenger;
	}

	int getAccepter() {
		return Accepter;
	}

	void setAccepter(int uID) {
		Accepter = uID;
	}

	double getWager() {
		return wager;
	}

	int getWinner() {
		return winner;
	}
	
	void setWinner(int w){
		winner = w;
	}

	double getLine() {
		return line;
	}
	
	int getHomeRoster(){
		return homeRosterID;
	}
	
	int getHomeRoster2(){
		return homeRoster2;
	}
	
	int getAwayRoster(){
		return awayRosterID;
	}
	
	int getAwayRoster2(){
		return awayRoster2;
	}
	
	int getPlayerID(int i){
		switch(i){
		case 1:
			return homePlayer1;
		case 2:
			return homePlayer2;
		case 3:
			return awayPlayer1;
		case 4: 
			return awayPlayer2;
		default:
			return 0;
		}
	}
	
	void setPlayerID(int i, int id){
		switch(i){
		case 1:
			homePlayer1=id;
			break;
		case 2:
			homePlayer2=id;
			break;
		case 3:
			awayPlayer1=id;
			break;
		case 4: 
			awayPlayer2=id;
			break;
		default:
			break;
		}
	}

	/*
	void enforce(int[] winLines, userList ul) {

		int checkline;
		switch (winner) {
		case 0:
			checkline = winLines[0];
			if (line > checkline) {
				winUse = Challenger;
				loseUse = Accepter;
			} else {
				winUse = Accepter;
				loseUse = Challenger;
			}
			break;
		case 1:
			checkline = winLines[1];
			if (line > checkline) {
				winUse = Challenger;
				loseUse = Accepter;
			} else {
				winUse = Accepter;
				loseUse = Challenger;
			}
			break;
		case 2:
			checkline = winLines[0];
			if (line > checkline) {
				winUse = Challenger;
				loseUse = Accepter;
			} else {
				winUse = Accepter;
				loseUse = Challenger;
			}
			break;
		case 3:
			checkline = winLines[0];
			if (line < checkline) {
				winUse = Challenger;
				loseUse = Accepter;
			} else {
				winUse = Accepter;
				loseUse = Challenger;
			}
			break;
		}

		ul.getUserByID(loseUse).adjustFrozen(-wager);
		ul.getUserByID(winUse).adjustFrozen(-wager);
		ul.getUserByID(winUse).adjustFree(2 * wager);

		result = ul.getUserByID(winUse).getName() + " wins $" + wager;

		if (winUse == Accepter)
			status = BET_ACP_WIN_ID;
		else
			status = BET_CHAL_WIN_ID;

		// Remove from active bets
		// Push to historical bets
	}
	*/
	
	boolean challengerWin(){

		int homeScore = currentAssignList.getRosterScore(homeRosterID, homePlayer1) - currentAssignList.getPlayerScore(homeRoster2, homePlayer2);
		int awayScore = currentAssignList.getRosterScore(awayRosterID, awayPlayer1) - currentAssignList.getPlayerScore(awayRoster2, awayPlayer2);
		
		switch(winner){
		case WINNER_STATUS_HOME:
			if(homeScore+line>awayScore) return true;
			else return false;
		case WINNER_STATUS_AWAY:
			if(homeScore+line<awayScore) return true;
			else return false;
		case WINNER_STATUS_OVER:
			if(homeScore+awayScore>line) return true;
			else return false;
		case WINNER_STATUS_UNDER:
			if(homeScore+awayScore<line) return true;
			else return false;
		}
		return true;
	}

	void makeJSON(JSONObject jOb) {
		try {
			jOb.put("challengeID", chalID); //set to zero for make bet
			int wageInt = (int) wager;
			jOb.put("wager", wageInt);
			jOb.put("winner", winner);
			jOb.put("line", line);
			jOb.put("Accepter", Accepter);//change to friend key once we have server communication running
			jOb.put("friend", Accepter);
			jOb.put("homePrimaryRosterID", homeRosterID);
			jOb.put("homeAddedRosterID", homeRoster2);
			jOb.put("homeRemovedPlayer", homePlayer1);
			jOb.put("homeAddedPlayer", homePlayer2);
			jOb.put("awayPrimaryRosterID", awayRosterID);
			jOb.put("awayAddedRosterID", awayRoster2);
			jOb.put("awayRemovedPlayer", awayPlayer1);
			jOb.put("awayAddedPlayer", awayPlayer2);
			// for local only 
			jOb.put("Challenger", Challenger);
			jOb.put("status", status);//may need to change with regards to server
			
			//for server only
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}


