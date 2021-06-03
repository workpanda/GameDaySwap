package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends ArrayAdapter<HashMap<String, String>>{
	private Context context;
	private ArrayList<HashMap<String, String>> arrayList;	
	
	public FriendListAdapter(Context context, int resource,
			ArrayList<HashMap<String, String>> objects) {
		super(context, resource, objects);
		this.arrayList = objects;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Log.d("in friendadapt switch", "in friendadapt");
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View rowView = inflater.inflate(R.layout.friendslist, parent, false);
		HashMap<String, String>hashmap_Current;
		TextView friendName = (TextView) rowView.findViewById(R.id.friendname);
		TextView subtext = (TextView) rowView.findViewById(R.id.friendsubtext);
		ImageView friendIcon = (ImageView) rowView.findViewById(R.id.friendicon);
		
		
		hashmap_Current=new HashMap<String, String>();
		hashmap_Current=arrayList.get(position);
		friendName.setText(hashmap_Current.get("name").toString());
		//Log.d("making addUser1", friendName);
		subtext.setText(hashmap_Current.get("details").toString());
		//Log.d("making addUser1", subtext);
		
		subtext.setTextColor(Color.GRAY);
		friendIcon.setImageResource(Integer.parseInt(hashmap_Current.get("icon").toString()));

		return rowView;
		
		
	}

}