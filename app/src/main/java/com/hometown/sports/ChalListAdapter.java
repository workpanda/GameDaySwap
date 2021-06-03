package com.hometown.sports;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChalListAdapter extends ArrayAdapter<HashMap<String, String>>{
	private Context context;
	private ArrayList<HashMap<String, String>> arrayList;
	
	
	public ChalListAdapter(Context context, int resource,
			ArrayList<HashMap<String, String>> objects) {
		super(context, resource, objects);
		this.arrayList = objects;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = LayoutInflater.from(context);
		View rowView = inflater.inflate(R.layout.list_item, parent, false);
		HashMap<String, String>hashmap_Current;
		TextView gameDisplay = (TextView) rowView.findViewById(R.id.gameTitle);
		TextView lineDisplay = (TextView) rowView.findViewById(R.id.chalTitle);
		TextView wagerDisplay = (TextView) rowView.findViewById(R.id.wagerTitle);
		TextView statusDisplay = (TextView) rowView.findViewById(R.id.chalStatus);
		ImageView homeLogo = (ImageView) rowView.findViewById(R.id.chalItemHomeLogo);
		ImageView awayLogo = (ImageView) rowView.findViewById(R.id.chalItemAwayLogo);
		
		hashmap_Current=new HashMap<String, String>();
		hashmap_Current=arrayList.get(position);
		gameDisplay.setText(hashmap_Current.get("gameTitle").toString());
		lineDisplay.setText(hashmap_Current.get("betTitle").toString());
		wagerDisplay.setText(hashmap_Current.get("wagerTitle").toString());
		
		String homelogourl = hashmap_Current.get("homeTeam");
		String awaylogourl = hashmap_Current.get("awayTeam");
		
		new DownloadImageTask(homeLogo).execute(homelogourl);
		new DownloadImageTask(awayLogo).execute(awaylogourl);
		
		int betStatus = Integer.parseInt(hashmap_Current.get("betStatus").toString());
		
		switch(betStatus){
		case MenuBarActivity.CHAL_STATUS_ACCEPTEDPUBLIC:
			statusDisplay.setText("Accepted");
			statusDisplay.setTextColor(Color.BLUE);
			break;
		case MenuBarActivity.CHAL_STATUS_OPENPUBLIC:
			statusDisplay.setText("Pending");
			statusDisplay.setTextColor(Color.MAGENTA);
			break;
		case MenuBarActivity.CHAL_STATUS_OPENFRIEND:
			statusDisplay.setText("Pending");
			statusDisplay.setTextColor(Color.MAGENTA);
			break;
		case MenuBarActivity.CHAL_STATUS_ACCEPTEDFRIEND:
			statusDisplay.setText("Accepted");
			statusDisplay.setTextColor(Color.BLUE);
			break;
		case MenuBarActivity.CHAL_STATUS_CLOSEDFRIEND:
			statusDisplay.setText("Completed");
			statusDisplay.setTextColor(Color.GRAY);
			break;
		case MenuBarActivity.CHAL_STATUS_CLOSEDPUBLIC:
			statusDisplay.setText("Completed");
			statusDisplay.setTextColor(Color.GRAY);
			break;
		
		}
		
		return rowView;
		
		
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        String useurl = MenuBarActivity.ImageURL + urldisplay +".png";
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(useurl).openStream();
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
