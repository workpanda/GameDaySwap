package com.hometown.sports;

public class teamList {
	
	int putloc;
    Team gl[];

    teamList(int len){
        gl = new Team[len];
        putloc=0;

    }

    int getTeams(){
        return putloc;
    }

    void push(Team game1){
        if (putloc >= gl.length){
            Team[] temp = new Team[putloc+10];
            for(int i=0; i<gl.length;i++){
                temp[i]=gl[i];
            }
            gl=temp;
        }

        gl[putloc]=game1;
        putloc++;
    }

    void pop(int gamID){
        int gamecheck;
        boolean triggered=false;
        for(int g=0; g < putloc; g++){
            gamecheck=gl[g].getTeamID();
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

    Team getTeam(int index){
        return gl[index];
    }

    int getIndex(int ID){
        int check;
        for(int j=0;j<putloc;j++){
            check = this.getTeam(j).getTeamID();
            if(check == ID) {
                return j;
            }
        }
        return -1;
    }
    
    Team getTeamByID(int gid){
    	int check;
        for(int j=0;j<putloc;j++){
            check = this.getTeam(j).getTeamID();
            if(check == gid) {
                return this.getTeam(j);
            }
        }
        return null;
    }
    
    boolean containsTeam(int ID){
    	boolean re = false;
    	int check;
    	for(int j=0;j<putloc;j++){
            check = this.getTeam(j).getTeamID();
            if(check == ID) {
                re = true;
                break;
            }
        }
    	
    	return re;
    }
    
    void clear(){
    	for(int i = 0; i<putloc; i++){
    		gl[i] = null;
    	}
    	putloc = 0;
    }

}
