package de.client.gui.calendar;

import java.util.Calendar;

/**
 * This class represents an object that can be displayed in our calendar.
 * For example birthdays of certain persons etc.
 * @author adrian
 *
 */
public class CalendarEntry {
	
	private Calendar date;
	private String description;
	
	public CalendarEntry(Calendar date, String description) {
		if(date==null)
			date = Calendar.getInstance();
	//	super();
		this.date = date;
		this.description = description;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the description.
	 */
	@Override
	public String toString(){
		return " - " + description;
	}
	
}
