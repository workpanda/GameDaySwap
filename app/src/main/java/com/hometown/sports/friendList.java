package com.hometown.sports;

public class friendList {
	
	int putloc;
    Friendship gl[];

    friendList(int len){
        gl = new Friendship[len];
        putloc=0;

    }

    int getFriends(){
        return putloc;
    }

    void push(Friendship game1){
        if (putloc >= gl.length){
            Friendship[] temp = new Friendship[putloc+10];
            for(int i=0; i<gl.length;i++){
                temp[i]=gl[i];
            }
            gl=temp;
        }

        gl[putloc]=game1;
        putloc++;
        
    }

    /*
    void pop(long gamID){
        long gamecheck;
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
    
*/
    Friendship getFriendship(int index){
        return gl[index];
    }
    
    void clear(){
    	for(int i = 0; i<putloc; i++){
    		gl[i] = null;
    	}
    	putloc = 0;
    }

	public boolean contains(int i) {
		boolean re = false;
		for (int j = 0; j<putloc; j++){
			int [] ids = new int[2];
			ids = gl[j].getUserIDs();
			if (ids[0] == i | ids[1] == i){
				re = true; 
				break;
			}
		}
		return re;
	}
    
   
/*
    int getIndex(long ID){
        long check;
        for(int j=0;j<putloc;j++){
            check = this.getGame(j).getID();
            if(check == ID) {
                return j;
            }
        }
        return -1;
    }
*/

}
