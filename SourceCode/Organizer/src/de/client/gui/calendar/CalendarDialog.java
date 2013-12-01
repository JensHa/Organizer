package de.client.gui.calendar;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.freixas.jcalendar.*;

/**
 * Opens a Dialog Box to choose a date.
 * @author adrian
 */

public class CalendarDialog extends JFrame
{
	
	private static Date date = new Date();
	private static JLabel selected = new JLabel();
	private JButton okb = new JButton("Ok");

	public CalendarDialog(){}
	/**
	 * Opens a Dialog to choose a date and returns the selected Date.
	 * @author adrian
	 */
	public  Date startCalendarDialog(){
		// Set up the frame
		final JFrame fr = new JFrame("Choose a date");
		fr.setTitle("CalendarChooser");
		fr.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Container contentPane = fr.getContentPane();
		contentPane.setLayout(new GridLayout(2, 2, 5, 5));

		// Create a border for all calendars

		Border etchedBorder   = BorderFactory.createEtchedBorder();
		Border emptyBorder    = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border compoundBorder = BorderFactory.createCompoundBorder(etchedBorder, emptyBorder);

		// Create a date listener to be used for all calendars

		MyDateListener listener = new MyDateListener();

		// Display date and time using the default calendar and locale.
		// Display today's date at the bottom.

		JCalendar calendar1 =
				new JCalendar(
						JCalendar.DISPLAY_DATE | JCalendar.DISPLAY_TIME,
						true);
		calendar1.addDateListener(listener);
		calendar1.setBorder(compoundBorder);

		// Set fonts rather than using defaults

		calendar1.setTitleFont(new Font("Serif", Font.BOLD|Font.ITALIC, 24));
		calendar1.setDayOfWeekFont(new Font("SansSerif", Font.ITALIC, 12));
		calendar1.setDayFont(new Font("SansSerif", Font.BOLD, 16));
		calendar1.setTimeFont(new Font("DialogInput", Font.PLAIN, 10));
		calendar1.setTodayFont(new Font("Dialog", Font.PLAIN, 14));

		// Display date only
		//Ok button
		okb.setEnabled(false);
		okb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fr.dispose();
				
			}
		});
		// Add all the calendars to the content pane

		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.add(calendar1);
		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.add(okb, BorderLayout.BEFORE_FIRST_LINE);
		panel2.add(selected);
		panel1.add(panel2, BorderLayout.AFTER_LAST_LINE);
		contentPane.add(panel1);

		// Make the window visible

		fr.pack();
		fr.setVisible(true);
		
		return date;
	}
	private class MyDateListener implements DateListener
	{

		public void
		dateChanged(DateEvent e)
		{
			Calendar c = e.getSelectedDate();
			if (c != null) {
				date = e.getSelectedDate().getTime();
				selected.setText("Selected Date: " + date.toString());
				okb.setEnabled(true);
			}
		}

	}
	
	public Date getDate(){
		return this.date;
	}

}