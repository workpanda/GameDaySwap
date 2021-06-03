package com.hometown.sports;

public class gameList {
	
	int totalgames=200001;
    int putloc;
    Game gl[];

    gameList(int len){
        gl = new Game[len];
        putloc=0;

    }

    int getGames(){
        return putloc;
    }

    void push(Game game1){
        if (putloc >= gl.length){
            Game[] temp = new Game[putloc+10];
            for(int i=0; i<gl.length;i++){
                temp[i]=gl[i];
            }
            gl=temp;
        }

        gl[putloc]=game1;
        putloc++;
        totalgames++;
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

    Game getGame(int index){
        return gl[index];
    }

    int getIndex(int ID){
        int check;
        for(int j=0;j<putloc;j++){
            check = this.getGame(j).getID();
            if(check == ID) {
                return j;
            }
        }
        return -1;
    }
    
    Game getGameByID(int gid){
    	int check;
        for(int j=0;j<putloc;j++){
            check = this.getGame(j).getID();
            if(check == gid) {
                return this.getGame(j);
            }
        }
        return null;
    }
    
    boolean containsGame(int ID){
    	boolean re = false;
    	int check;
    	for(int j=0;j<putloc;j++){
            check = this.getGame(j).getID();
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
