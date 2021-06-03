package com.hometown.sports;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User {
	private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String password;
    private int zipCode;
    private int userID;
    private double ActFree;
    private double ActFrozen;
    private double ActTotal;
    private long CCnum;
    private int month;
    private int year;
    private long phone;
    private int friendStatus;

    User(String fName, String lName, String eml, String pw, String addss1, String addss2, String cty, String ste, int zip, long ccn, int mnth, int yr, int uID){
        firstName = fName;
        lastName = lName;
        fullName = fName+ " " + lName;
        emailAddress = eml;
        address1 = addss1;
        address2 = addss2;
        city = cty;
        state = ste;
        zipCode = zip;
        CCnum=ccn;
        ActFree=0;
        ActFrozen=0;
        ActTotal =0;
        userID = uID;
        password = pw;
        month = mnth;
        year = yr;

    }
    
    User(String fName, String lName, int uID){
    	firstName = fName;
    	lastName = lName;
    	fullName = fName+ " " + lName;
    	userID = uID;
    }
    
    User(String fName, String lName, int uID, long phoneInt) {
    	firstName = fName;
    	lastName = lName;
    	fullName = fName+ " " + lName;
    	userID = uID;
    	phone = phoneInt;
    	friendStatus = 0;
	}
    
    User(String email, String pw, String fN, String ln){
    	firstName = fN;
        lastName = ln;
        fullName = "";
        emailAddress = email;
        address1 = "";
        address2 = "";
        city = "";
        state = "";
        zipCode = 0;
        CCnum=0;
        ActFree=0;
        ActFrozen=0;
        ActTotal =0;
        userID = 0;
        password = pw;
        month = 0;
        year = 0;
    }
    
    User(JSONObject j){
    	try{
    		firstName = j.getString("firstName");
    		lastName = j.getString("lastName");
    		userID = Integer.parseInt(j.getString("userID"));
    		fullName = firstName + " " + lastName;
    		phone = Long.parseLong(j.getString("phone"));
    	} catch (Exception e){
    		
    	}	
    }
    
    User(int id, boolean isR, JSONObject j){
    	userID = id;
    	if(isR){
    		try{
        		firstName = j.getString("RFN");
        		lastName = j.getString("RLN");
        		fullName = firstName + " " + lastName;
        	} catch (Exception e){
        		Log.d("User constructor","JSONERROR isR");
        	}
    	}
    	else{
    		try{
        		firstName = j.getString("SFN");
        		lastName = j.getString("SLN");
        		fullName = firstName + " " + lastName;
        	} catch (Exception e){
        		Log.d("User constructor","JSONERROR !isR");
        	}
    	}
    	
    		
    }

    void chargeCC(int amt){
        ActFree = ActFree+amt;
        //Charge Card Here
        //String CCStr = (String)CCnum;
        //String amtStr = (String)amt;
        System.out.println("Credit Card " + CCnum +" charged $" +amt);
    }

    void adjustFree(double amt){
        ActFree=ActFree+amt;
        if (ActFree<0) {
            int rdup = (int) ActFree*-1;
            rdup=(rdup+4)/5*5;
            //USER INPUT OK?
            chargeCC(rdup);
        }
        ActTotal = ActFree+ActFrozen;
    }

    void adjustFrozen(double amt){
        ActFrozen = ActFrozen+amt;
        ActTotal = ActFree+ActFrozen;
    }

    int getUserID(){
        return userID;
    }
    
    String getEmail(){ return emailAddress;}
    
    int checkPassword(String pwIn){
    	if (password.equals(pwIn)){return 1;}
    	else { return 0;}
    }

    double getFree(){return ActFree;}
    double getFrozen(){return ActFrozen;}
    String getName(){return fullName;}
    String getFName(){return firstName;}
    String getLName(){return lastName;}
    double getTotal(){return ActTotal;}

    boolean isSame(long IDin){
        if(IDin == userID){return true;}
        else{return false;}
    }
    
    String makeCCNDisp(){
    	String re = "";
    	String CCNStr = String.valueOf(CCnum);
    	String CCNDisp = CCNStr.substring(CCNStr.length() - 4);
    	
    	re = CCNDisp;
    	
    	return re;
    }

    void makeFullJSON(JSONObject jOb){
        try {
            jOb.put("userID", userID);
            jOb.put("firstName",firstName);
            jOb.put("lastName", lastName);
            jOb.put("fullName", fullName);
            jOb.put("emailAddress",emailAddress);
            jOb.put("password",password);
            jOb.put("address1", address1);
            jOb.put("address2", address2);
            jOb.put("city", city);
            jOb.put("state",state);
            jOb.put("zipCode", zipCode);
            jOb.put("CCnum", CCnum);
            jOb.put("month", month);
            jOb.put("year", year);
            jOb.put("ActFree", ActFree);
            jOb.put("ActFrozen", ActFrozen);
            jOb.put("ActTotal", ActTotal);
            jOb.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void makeShareJSON(JSONObject jOb){
        try {
            jOb.put("userID", userID);
            jOb.put("firstName",firstName);
            jOb.put("lastName", lastName);
            jOb.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    void makeCreateJSON(JSONObject jOb){
        try {
            jOb.put("email", emailAddress);
            jOb.put("firstName",firstName);
            jOb.put("lastName", lastName);
            jOb.put("passwowrd", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    void changeUserInfo(String fName, String lName, String eml, String addss1, String addss2, String cty, String ste, int zip){
    	firstName = fName;
        lastName = lName;
        fullName = fName+ " " + lName;
        emailAddress = eml;
        address1 = addss1;
        address2 = addss2;
        city = cty;
        state = ste;
        zipCode = zip;
    }
    
    void changeCreditCard(long ccn, int mon, int yr){
    	CCnum=ccn;
    	month = mon;
        year = yr;
    }
    
    void changePassword(String pwNew){
    	password = pwNew;
    }
    
    
    void setPhone(long ph){
    	phone = ph;
    }
    
    long getPhone(){
    	return phone;
    }
    
    void setFriend(int i){
    	friendStatus = i;
    }
    
    int getFriend(){
    	return friendStatus;
    }
    
    void setID (int ID){
    	userID = ID;
    }

}
