package com.hometown.sports;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class GameListAdapter extends ArrayAdapter<HashMap<String, String>>{
	private Context context;
	private ArrayList<HashMap<String, String>> arrayList;
	final int GAME_STATUS_OPEN = MenuBarActivity.GAME_STATUS_OPEN;
	final int GAME_STATUS_CLOSED = MenuBarActivity.GAME_STATUS_CLOSED;
	final int GAME_STATUS_INPROGRESS = MenuBarActivity.GAME_STATUS_INPROGRESS;
	final int GAME_STATUS_FINISHED = MenuBarActivity.GAME_STATUS_FINISHED;
	final int NFL_ID = MenuBarActivity.NFL_ID;
	final int NHL_ID = MenuBarActivity.NHL_ID;
	final int MLB_ID = MenuBarActivity.MLB_ID;
	final int NBA_ID = MenuBarActivity.NBA_ID;
	final int NCAAF_ID = MenuBarActivity.NCAAF_ID;
	final int NCAAB_ID = MenuBarActivity.NCAAB_ID;
	public GameListAdapter(Context context, int resource,
			ArrayList<HashMap<String, String>> objects) {
		super(context, resource, objects);
		this.arrayList = objects;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = LayoutInflater.from(context);
		View rowView = inflater.inflate(R.layout.gametable, parent, false);
		HashMap<String, String>hashmap_Current;
		TextView awayTeam = (TextView) rowView.findViewById(R.id.awayName);
		TextView homeTeam = (TextView) rowView.findViewById(R.id.homeName);
		TextView awayScore = (TextView) rowView.findViewById(R.id.awayScore);
		TextView homeScore = (TextView) rowView.findViewById(R.id.homeScore);
		TextView dateView = (TextView) rowView.findViewById(R.id.awayTime);
		TextView timeView = (TextView) rowView.findViewById(R.id.homeTime);
		ImageView homeLogo = (ImageView) rowView.findViewById(R.id.homeLogo);
		ImageView awayLogo = (ImageView) rowView.findViewById(R.id.awayLogo);
		
		hashmap_Current=new HashMap<String, String>();
		hashmap_Current=arrayList.get(position);
		
		homeTeam.setText(hashmap_Current.get("homeName").toString());
		awayTeam.setText(hashmap_Current.get("awayName").toString()+" at");
		
		int homescore = Integer.parseInt(hashmap_Current.get("homeScore").toString());
		int awayscore = Integer.parseInt(hashmap_Current.get("awayScore").toString());	
		
		if(homescore == 2000){
			awayScore.setText("");
			homeScore.setText("");
		}
		else{
			awayScore.setText(String.valueOf(awayscore));
			homeScore.setText(String.valueOf(homescore));
		}
		
		long timeStampIn = Long.parseLong(hashmap_Current.get("timeStamp").toString());
		int statusIn = Integer.parseInt(hashmap_Current.get("status").toString());
		int sportID = Integer.parseInt(hashmap_Current.get("sport").toString());
		long mins = 0;
		long secs = 0;
		int days = 0;
		int mons = 0;
		switch (statusIn){
		case GAME_STATUS_OPEN:
			//convert unix timestamp
			timeStampIn = timeStampIn * (long)1000;
			TimeZone.setDefault(TimeZone.getTimeZone("EST"));
			Date df = new Date(timeStampIn);
			String s = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df); 
			String monStr = s.substring(0,2);
			String dayStr = s.substring(3,5);
			String hrStr = s.substring(12,14);
			String minStr = s.substring(15);
			if (monStr.substring(0,0).equals("0")) monStr = monStr.substring(1);
			if (dayStr.substring(0,0).equals("0")) dayStr = dayStr.substring(1);
			if (hrStr.substring(0,0).equals("0")) hrStr = hrStr.substring(1);
			int hr = Integer.parseInt(hrStr);
			if (hr>12){
				hr = hr-12;
				hrStr = String.valueOf(hr);
			}
			dateView.setText(monStr+"/"+dayStr);
			timeView.setText(hrStr+":"+minStr);
			
			break;
		case GAME_STATUS_CLOSED:
			dateView.setText("No More");
			timeView.setText("Activity");
			break;
		case GAME_STATUS_INPROGRESS:
			switch (sportID){
			case NFL_ID:
				mins = timeStampIn/60;
				secs = timeStampIn - (60* mins);
				if (mins > 45){
					dateView.setText("1st");
					timeView.setText(String.valueOf(mins-45) + ":" + String.valueOf(secs));
				}
				else if (mins>30){
					dateView.setText("2nd");
					timeView.setText(String.valueOf(mins-30) + ":" + String.valueOf(secs));
				}
				else if(mins>15){
					dateView.setText("3rd");
					timeView.setText(String.valueOf(mins-15) + ":" + String.valueOf(secs));
				}
				else{
					dateView.setText("4th");
					timeView.setText(String.valueOf(mins) + ":" + String.valueOf(secs));
				}
				break;
			case NHL_ID: 
				mins = timeStampIn/60;
				secs = timeStampIn - (60* mins);
				if (mins > 40){
					dateView.setText("1st");
					timeView.setText(String.valueOf(mins-40) + ":" + String.valueOf(secs));
				}
				else if (mins>20){
					dateView.setText("2nd");
					timeView.setText(String.valueOf(mins-20) + ":" + String.valueOf(secs));
				}
				else {
					dateView.setText("3rd");
					timeView.setText(String.valueOf(mins) + ":" + String.valueOf(secs));
				}
				break;
			case MLB_ID:
				String halfIn = "";
				if (secs == 1) halfIn = "Top";
				else halfIn = "Bottom";
				dateView.setText(halfIn);
				timeView.setText(String.valueOf(mins));
				break;
			case NBA_ID:
				mins = timeStampIn/60;
				secs = timeStampIn - (60* mins);
				if (mins > 36){
					dateView.setText("1st");
					timeView.setText(String.valueOf(mins-36) + ":" + String.valueOf(secs));
				}
				else if (mins>24){
					dateView.setText("2nd");
					timeView.setText(String.valueOf(mins-24) + ":" + String.valueOf(secs));
				}
				else if(mins>12){
					dateView.setText("3rd");
					timeView.setText(String.valueOf(mins-12) + ":" + String.valueOf(secs));
				}
				else{
					dateView.setText("4th");
					timeView.setText(String.valueOf(mins) + ":" + String.valueOf(secs));
				}
				break;
			case NCAAF_ID:
				mins = timeStampIn/60;
				secs = timeStampIn - (60* mins);
				if (mins > 45){
					dateView.setText("1st");
					timeView.setText(String.valueOf(mins-45) + ":" + String.valueOf(secs));
				}
				else if (mins>30){
					dateView.setText("2nd");
					timeView.setText(String.valueOf(mins-30) + ":" + String.valueOf(secs));
				}
				else if(mins>15){
					dateView.setText("3rd");
					timeView.setText(String.valueOf(mins-15) + ":" + String.valueOf(secs));
				}
				else{
					dateView.setText("4th");
					timeView.setText(String.valueOf(mins) + ":" + String.valueOf(secs));
				}
				break;
			case NCAAB_ID:
				mins = timeStampIn/60;
				secs = timeStampIn - (60* mins);
				if (mins > 20){
					dateView.setText("1st");
					timeView.setText(String.valueOf(mins-20) + ":" + String.valueOf(secs));
				}
				else{
					dateView.setText("2nd");
					timeView.setText(String.valueOf(mins) + ":" + String.valueOf(secs));
				}
				break;
			}
			break;
		case GAME_STATUS_FINISHED:
			dateView.setText("F");
			timeView.setText("");
			break;
			
		}
		
		String homelogourl = hashmap_Current.get("homeLogo").toString();
		String awaylogourl = hashmap_Current.get("awayLogo").toString();
		new DownloadImageTask(homeLogo).execute(homelogourl);
		new DownloadImageTask(awayLogo).execute(awaylogourl);
		
		
		return rowView;
		
		
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	        	String useURL = MenuBarActivity.ImageURL + urldisplay + ".png";
	            InputStream in = new java.net.URL(useURL).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}

}
