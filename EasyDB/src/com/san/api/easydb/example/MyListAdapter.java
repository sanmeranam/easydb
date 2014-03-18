package com.san.api.easydb.example;

import java.util.List;

import com.san.api.easydb.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<MyEntityClass> {
	private Context				context;
	private List<MyEntityClass>	values;

	public MyListAdapter(Context context, List<MyEntityClass> values) {
		super(context, R.layout.list_item, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = null;

		if (convertView == null)
			rowView = inflater.inflate(R.layout.list_item, parent, false);
		else
			rowView = convertView;
		
		MyEntityClass dataObject=values.get(position);
		
		((TextView)rowView.findViewById(R.id.textTitle)).setText(dataObject.getTitle());
		((TextView)rowView.findViewById(R.id.textSubTitle)).setText(dataObject.getSubTitle());
		((TextView)rowView.findViewById(R.id.textTag)).setText(dataObject.getTag());
		
		return rowView;
	}
}
