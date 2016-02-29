package edu.uark.csce.Mobile4013_razorsquare;

import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Date;
import java.util.Date;

public class PostData {

	 String Post;
	 String createdDate;
	 String row;
	 String username;
	 double lat,lg;
	 
	 public PostData() 
	 {
			Date d;
			d = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM d, HH:mm:ss");
			createdDate = sdf.format(d).toString();
	}
	 
	public String getRow()
	{
		return row;
	}
	
	public String getPost() {
		return Post;
	}
	
	public String getCreatedDate(){
		return createdDate;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public double getLat()
	{
		return lat;
	}
	
	public double getLong()
	{
		return lg;
	}
	
	public void setRow(String id)
	{
		row=id;
	}
	
	public void setLat(double Lat)
	{
		lat=Lat;
	}
	
	public void setLong(double Long)
	{
		lg=Long;
	}
	
	public void setPostDatadb(String _user, String _Post,  String _date) {
		Post = _Post;
		createdDate = _date;
		username = _user;
	}
	
	public void setPostData(String _user, String _Post,  String _date) {
		Post = _Post;
		username = _user;
	}
	
	/*@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, HH:mm:ss");
		String dateString = sdf.format(createdDate);
		return dateString;
	}*/
}
