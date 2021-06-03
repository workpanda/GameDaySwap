package com.hometown.sports;


public class chalList {
    int putloc;
    Challenge bl[];

    chalList(int len){
        bl = new Challenge[len];
        putloc=0;

    }

    int getChals(){
        return putloc;
    }

    void push(Challenge c1){
        if (putloc >= bl.length){
            Challenge[] temp = new Challenge[putloc+10];
            for(int i=0; i<bl.length;i++){
                temp[i]=bl[i];
            }
            bl=temp;
        }

        bl[putloc]=c1;
        putloc++;
    }

    void pop(int chalID){
        long betcheck;
        boolean triggered=false;
        for(int g=0; g < putloc; g++){
            betcheck=bl[g].getChalID();
            if(betcheck==chalID){
                triggered = true;
            }
            if(triggered & g<bl.length){
                bl[g]=bl[g+1];
            }
        }

        if (triggered){
            bl[bl.length-1]=null;
            putloc--;
        }
        else{
            System.out.println("Bet not found");
        }
    }

    Challenge getChal(int index){
        return bl[index];
    }
    
    void clear(){
    	for(int i = 0; i<putloc; i++){
    		bl[i] = null;
    	}
    	putloc = 0;
    }

}
