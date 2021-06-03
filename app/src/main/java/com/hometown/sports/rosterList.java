package com.hometown.sports;

public class rosterList {
	
	int putloc;
    Roster gl[];

    rosterList(int len){
        gl = new Roster[len];
        putloc=0;

    }

    int getRosters(){
        return putloc;
    }

    void push(Roster game1){
        if (putloc >= gl.length){
            Roster[] temp = new Roster[putloc+10];
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

    Roster getRoster(int index){
        return gl[index];
    }

    int getIndex(int ID){
        int check;
        for(int j=0;j<putloc;j++){
            check = this.getRoster(j).getID();
            if(check == ID) {
                return j;
            }
        }
        return -1;
    }
    
    Roster getRosterByID(int gid){
    	int check;
        for(int j=0;j<putloc;j++){
            check = this.getRoster(j).getID();
            if(check == gid) {
                return this.getRoster(j);
            }
        }
        return null;
    }
    
    boolean containsRoster(int ID){
    	boolean re = false;
    	int check;
    	for(int j=0;j<putloc;j++){
            check = this.getRoster(j).getID();
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
