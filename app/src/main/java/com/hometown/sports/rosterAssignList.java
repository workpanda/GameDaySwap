package com.hometown.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class rosterAssignList extends MenuBarActivity {

	int putloc;
    RosterAssign gl[];

    rosterAssignList(int len){
        gl = new RosterAssign[len];
        putloc=0;

    }

    int getAssignments(){
        return putloc;
    }

    void push(RosterAssign game1){
        if (putloc >= gl.length){
            RosterAssign[] temp = new RosterAssign[putloc+10];
            for(int i=0; i<gl.length;i++){
                temp[i]=gl[i];
            }
            gl=temp;
        }

        gl[putloc]=game1;
        putloc++;
    }
    
    void push(JSONArray j){
    	for(int i = 0; i<j.length();i++){
    		JSONObject jO = new JSONObject();
    		try {
				jO = j.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		RosterAssign rA = new RosterAssign(jO);
    		this.push(rA);
    		
    	}
    }

    void pop(int gamID){
        int gamecheck;
        boolean triggered=false;
        for(int g=0; g < putloc; g++){
            gamecheck=gl[g].getID();
            if(gamecheck==gamID){
                triggered = true;
            }
            if(triggered & g<gl.length){
                gl[g]=gl[g+1];
            }
        }

        if (triggered){
            gl[gl.length-1]=null;
            putloc--;
        }
        else{
            System.out.println("Game not found");
        }
    }

    RosterAssign getAssign(int index){
        return gl[index];
    }

    int getIndex(int ID){
        int check;
        for(int j=0;j<putloc;j++){
            check = this.getAssign(j).getID();
            if(check == ID) {
                return j;
            }
        }
        return -1;
    }
    
    RosterAssign getAssignByID(int gid){
    	int check;
        for(int j=0;j<putloc;j++){
            check = this.getAssign(j).getID();
            if(check == gid) {
                return this.getAssign(j);
            }
        }
        return null;
    }
    
    RosterAssign getAssignByPlayer(int gid){
    	int check;
        for(int j=0;j<putloc;j++){
            check = this.getAssign(j).getPlayer();
            if(check == gid) {
                return this.getAssign(j);
            }
        }
        return null;
    }
    
    boolean containsRosterAssign(int ID){
    	boolean re = false;
    	int check;
    	for(int j=0;j<putloc;j++){
            check = this.getAssign(j).getID();
            if(check == ID) {
                re = true;
                break;
            }
        }
    	
    	return re;
    }
    
    int getRosterScore(int id, int pid){
    	int score = 0;
    	for(int i = 0;i<putloc;i++){
    		int ros = this.getAssign(i).getRoster();
    		if(ros==id){
    			int stat = this.getAssign(i).getStatus();
    			if(stat==ASSIGN_STATUS_UNLOCKED){
    				if(this.getAssign(i).getPlayer()!=pid){
    					stat = ASSIGN_STATUS_LOCKED;
    				}
    			}
    			if(stat==ASSIGN_STATUS_LOCKED){
    				int sc = this.getAssign(i).getPoints();
        			score = score+sc;
    			}
    			
    		}
    	}
    	return score;
    }
    
    int getPlayerScore(int ros, int pid){
    	int score = 0;
    	for(int i = 0;i<putloc;i++){
    		int ros1 = this.getAssign(i).getRoster();
    		if(ros==ros1){
    			int stat = this.getAssign(i).getPlayer();
    			if(stat==pid){
    				if(this.getAssign(i).getPlayer()==pid){
    					score = this.getAssign(i).getPoints();
    					break;
    				}
    			}
    			
    		}
    	}
    	return score;
    }
    
    rosterAssignList getFullRoster(int id){
    	rosterAssignList re = new rosterAssignList(1);
    	for(int i = 0; i<putloc; i++){
    		if(this.getAssign(i).getRoster()==id){
    			re.push(this.getAssign(i));
    		}
    	}
    	return re;
    }
    
    int getNumAssigns(){
    	return putloc;
    }
    
    void sortPlayerNum(){
    	for(int i = 0;i<putloc;i++){
    		for(int j = 1; j<putloc;j++){
    			if(currentPlayerList.getPlayerByID(gl[j].getPlayer()).getNumber()<currentPlayerList.getPlayerByID(gl[j].getPlayer()).getNumber()){
    				RosterAssign temp = gl[j];
    				gl[j] = gl[j-1];
    				gl[j-1]=temp;
    			}
    		}
    	}
    }
    
    void clear(){
    	for(int i = 0; i<putloc; i++){
    		gl[i] = null;
    	}
    	putloc = 0;
    }
    
}
