package com.hometown.sports;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SwapListAdapter extends ArrayAdapter<HashMap<String, String>>{

	private Context context;
	private ArrayList<HashMap<String, String>> arrayList;
	
	public SwapListAdapter(Context context, int resource,
			ArrayList<HashMap<String, String>> objects) {
		super(context, resource, objects);
		this.arrayList = objects;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = LayoutInflater.from(context);
		View rowView = inflater.inflate(R.layout.swap_list_item, parent, false);
		HashMap<String, String>hashmap_Current;
		TextView setType = (TextView) rowView.findViewById(R.id.swapListIn);
		TextView addTeam = (TextView) rowView.findViewById(R.id.swapListAddTeam);
		//TextView dropTeam = (TextView) rowView.findViewById(R.id.swapListDropTeam);
		TextView addPos = (TextView) rowView.findViewById(R.id.swapListAddPos);
		//TextView dropPos = (TextView) rowView.findViewById(R.id.swapListDropPos);
		TextView addName = (TextView) rowView.findViewById(R.id.swapListAddName);
		//TextView dropName = (TextView) rowView.findViewById(R.id.swapListDropName);
		
		hashmap_Current=new HashMap<String, String>();
		hashmap_Current=arrayList.get(position);
		
		int type = Integer.parseInt(hashmap_Current.get("Type").toString());
		
		if (type == 1){
			setType.setText("In: ");
			setType.setTextColor(Color.GREEN);
		}
		else{
			setType.setText("Out: ");
			setType.setTextColor(Color.RED);
		}
		
		addTeam.setText(hashmap_Current.get("subTeam").toString());
		addPos.setText(hashmap_Current.get("subPos").toString());
		addName.setText(hashmap_Current.get("subName").toString());
		//dropTeam.setText(hashmap_Current.get("mainTeam").toString());
		//dropPos.setText(hashmap_Current.get("mainPos").toString());
		//dropName.setText(hashmap_Current.get("mainName").toString());
		
		return rowView;
		
		
	}
}
