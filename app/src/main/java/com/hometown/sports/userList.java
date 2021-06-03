package com.hometown.sports;

public class userList {
	int putloc;
    User ul[];

    userList(int len){
        ul = new User[len];
        putloc=0;

    }

    int getUsers(){
        return putloc;
    }

    void push(User user1){
        if (putloc >= ul.length){
            User[] temp = new User[putloc+10];
            for(int i=0; i<ul.length;i++){
                temp[i]=ul[i];
            }
            ul=temp;
        }

        ul[putloc]=user1;
        putloc++;

    }
    
    void pop(int userID){
        int betcheck;
        boolean triggered=false;
        for(int g=0; g < putloc; g++){
            betcheck=ul[g].getUserID();
            if(betcheck==userID){
                triggered = true;
            }
            if(triggered & g<ul.length){
                ul[g]=ul[g+1];
            }
        }

        if (triggered){
            ul[ul.length-1]=null;
            putloc--;
        }
        else{
            System.out.println("Bet not found");
        }
    }

    User getUser(int index){
        User re = ul[index];
        return re;
    }

    User getUserByID(int ID){
        User re = null;
        for(int i=0;i<putloc;i++){
            if(ul[i].getUserID() == ID){
                re= ul[i];
                break;
            }
        }
        return re;
    }
    
    public void sortFName(){
    	int len = putloc-1;
    	for(int i = 0; i<len; i++){
    		for(int j=0; j<len;j++){
    			User u1 = ul[j];
    			User u2 = ul[j+1];
    			if(u1.getFName().compareTo(u2.getFName()) > 0){
    				ul[j+1] = u1;
    				ul[j] = u2;
    			}
    		}
    	}
    	
    }
    
    boolean contains(int ID){
    	boolean re = false;
    	int check;
    	for(int j=0;j<putloc;j++){
            check = this.getUser(j).getUserID();
            if(check == ID) {
                re = true;
                break;
            }
        }
    	return re;
    }
    
    void clear(){
    	for(int i = 0; i<putloc; i++){
    		ul[i] = null;
    	}
    	putloc = 0;
    }

}
