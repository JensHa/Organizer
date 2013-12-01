package de.client.gui.calendar;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import de.client.contacts.Contact;

public class GoogleCalendarEntryFeed {
	
	private ArrayList<CalendarEntry> entries = new ArrayList<CalendarEntry>();
	
	/**
	 * Parses a Json Google Calendar Entry Feed to an GoogleCalendarEntryFeed Object
	 * provides access to all entries of that feed/calendar
	 * @param jsonfeed a valid json feed
	 * @author adrian
	 */
	public GoogleCalendarEntryFeed(JSONObject jsonfeed){

		//Get the head (feed)
		JSONObject result = jsonfeed.getJSONObject("feed");

		//System.out.println("AFTER GET FEED: "+result.toString(4));
		//Get all contact-entries (people).... is there more than one entry?=> array ... if not then Object
		if(result.get("entry").getClass().toString().equals("class org.codehaus.jettison.json.JSONArray"))
		{
	    JSONArray allentries = result.getJSONArray("entry");
		
	    //only one entry=> JSONObject
		}else
		{	
			//always this case, as there is only one entry, but that contains an array.
			 JSONArray entryarray = (JSONArray) result.get("entry"); 
			 
			 for (int i = 0; i < entryarray.length(); i++) {
				JSONObject entry = (JSONObject) entryarray.get(i);
				
				//Get the data that we are using:
				String title =  (String) entry.getJSONObject("title").get("content");
				String time =  (String) entry.getJSONObject("gd:when").get("startTime");
				
				//Build and add the new entry
				CalendarEntry newentry = new CalendarEntry(time, title);
			//	System.out.println("NEW: " + newentry);
				entries.add(newentry);
			}
			 
		}
	}
	
	public ArrayList<CalendarEntry> getEntries(){
		return this.entries;
	}
	
	public void addEntry(CalendarEntry entry){
		if (entry != null)
			this.entries.add(entry);
	}
	
}
