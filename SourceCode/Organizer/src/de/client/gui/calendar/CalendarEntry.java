package de.client.gui.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import com.google.gdata.data.dublincore.Date;

/**
 * This class represents an object that can be displayed in our calendar.
 * For example birthdays of certain persons etc.
 * @author adrian
 *
 */
public class CalendarEntry {
	
	
	private Date date;
	private String title;
	
	public CalendarEntry() {
	}
	
	public CalendarEntry(String date, String title) {
		setDate(date);
		this.title = title;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * sets the date from a String 
	 * format should be like this: 2013-12-24T18:00:00.000+07:00 
	 * @param googleDateFormat a correctly formatted string representing a date
	 */
	public void setDate(String googleDateFormat){
		//2013-12-24T18:00:00.000+07:00
		if (!((googleDateFormat!= null) && (!googleDateFormat.equals("")))){
			return;
		}
		
		java.util.Date datep = new Date();
		// Set format, dont use miliseconds (who the hell uses that in his calendar)
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd-HH:mm:ss.SSS" );
		
		String subs = googleDateFormat.substring(0,23);
		try {
			datep = df.parse(subs.replace('T', '-'));
			date = datep;
		} catch (ParseException e) {
			System.out.println("problem when parsing a date");
			e.printStackTrace();
		}
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the title.
	 */
	@Override
	public String toString(){
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
		String returnstring = "";
		try{
			returnstring =  "" + title+" on "+ df.format(date);
			return returnstring;
		}
		catch(NullPointerException e){
			//occurs often, for example if the entry only has a day, but not a specific time
			//but works still so just ignore it
			System.out.println("Nullpointer exception when trying to format the date of entry \"" + title + "\"");
			return returnstring;
			
		}
	}
	
}
