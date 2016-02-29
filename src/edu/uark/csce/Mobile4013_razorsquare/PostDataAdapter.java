package edu.uark.csce.Mobile4013_razorsquare;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostDataAdapter extends ArrayAdapter<PostData> {

	int resource;
	
	public PostDataAdapter(Context context, int resource, List<PostData> itemList) 
	{
		super(context, resource, itemList);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout todoView;
		PostData item = getItem(position);
		
		String postString = item.getPost();
		String createdDate = item.getCreatedDate();
		
		if (convertView == null) {
			todoView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater lifr = (LayoutInflater)getContext().getSystemService(inflater);
			lifr.inflate(resource, todoView, true);
		}
		else {
			todoView = (LinearLayout)convertView;
		}
		
		TextView postView = (TextView)todoView.findViewById(R.id.listItem1);
		TextView dateView = (TextView)todoView.findViewById(R.id.listItem2);
		
		postView.setText(postString);
		dateView.setText(createdDate);
		
		return todoView;
	}
	
}
