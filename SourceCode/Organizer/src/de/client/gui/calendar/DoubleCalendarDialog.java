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

public class DoubleCalendarDialog extends JDialog
{
	
	private static Date[] date = new Date[2];
	private static JTextArea selected = new JTextArea();
	private JButton okb = new JButton("Ok");
    private JCalendar calendar1;
    private JCalendar calendar2;
    private Boolean[] pressed = {false,false};
	
	/**
	 * Opens a Dialog to choose two dates
	 * @param parent the parent frame, which gets inactive if the dialog isnt finished
	 * @author adrian
	 */
	public  DoubleCalendarDialog(JFrame parent, String title, Boolean is_modal){
		super(parent,title,is_modal);
		
		selected.setEditable(false);
		// Set up the frame
		setTitle("CalendarChooser");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(2, 2, 5, 5));

		// Create a border for all calendars

		Border etchedBorder   = BorderFactory.createEtchedBorder();
		Border emptyBorder    = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border compoundBorder = BorderFactory.createCompoundBorder(etchedBorder, emptyBorder);

		// Create a date listener to be used for all calendars

		MyDateListener listener = new MyDateListener();
		MyDateListener listener2 = new MyDateListener();

		// Display date and time using the default calendar and locale.
		// Display today's date at the bottom.

		calendar1 =	new JCalendar(
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
		
		//Calendar 2


		 calendar2 =new JCalendar(
						JCalendar.DISPLAY_DATE | JCalendar.DISPLAY_TIME,
						true);
		calendar2.addDateListener(listener2);
		calendar2.setBorder(compoundBorder);

		// Set fonts rather than using defaults

		calendar2.setTitleFont(new Font("Serif", Font.BOLD|Font.ITALIC, 24));
		calendar2.setDayOfWeekFont(new Font("SansSerif", Font.ITALIC, 12));
		calendar2.setDayFont(new Font("SansSerif", Font.BOLD, 16));
		calendar2.setTimeFont(new Font("DialogInput", Font.PLAIN, 10));
		calendar2.setTodayFont(new Font("Dialog", Font.PLAIN, 14));

		// Display date only
		//Ok button
		okb.setEnabled(false);
		okb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				
			}
		});
		// Add all the calendars to the content pane

		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.add(calendar1, BorderLayout.BEFORE_FIRST_LINE);
		panel1.add(calendar2);
		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.add(okb, BorderLayout.BEFORE_FIRST_LINE);
		panel2.add(selected);
		panel1.add(panel2, BorderLayout.AFTER_LAST_LINE);
		contentPane.add(panel1);

		// Make the window visible

		pack();
		setVisible(true);
		
	//	return date;
	}
	private class MyDateListener implements DateListener
	{

		public void
		dateChanged(DateEvent e)
		{
			Calendar c = e.getSelectedDate();
			if (c != null) {
				if (e.getSource()==calendar1){
					date[0] = e.getSelectedDate().getTime();
					pressed[0] = true;
				}
				else if (e.getSource()==calendar2){
					date[1] = e.getSelectedDate().getTime();
					pressed[1] = true;
				}
				selected.setText("From:"+"\t" + date[0]+ "\nto:\t" + date[1]);
				if(pressed[0] && pressed[1])
				okb.setEnabled(true);
			}
		}

	}
	/**
	* gives the selected date.
	 * @return selected Date object.
	 */
	public Date[] getDate(){

		return date;
	}

}