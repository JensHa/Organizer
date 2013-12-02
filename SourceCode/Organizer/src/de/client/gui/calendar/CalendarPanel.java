package de.client.gui.calendar;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import org.freixas.jcalendar.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This Panel shows CalendarEntry Items
 * @author adrian
 *
 */
public class CalendarPanel extends JPanel {

	private JFrame parent;
	private DefaultListModel<CalendarEntry> model = new DefaultListModel<CalendarEntry>();
	private List<CalendarEntry> entrylist;

	/**
	 * Creates a new Calendar Panel.
	 */
	public CalendarPanel(JFrame parent,final List<CalendarEntry> entrylist) {
		this.entrylist = entrylist;
		this.parent = parent;
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 450, 300);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		//setContentPane(contentPane);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);

		//Add entries to list model
		for(CalendarEntry entry : entrylist){
			model.addElement(entry);
		}

		JScrollPane scrollPane = new JScrollPane();
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{171, 22, 108, 6, 108, 42, 0};
		gbl_panel.rowHeights = new int[]{133, 23, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		final JTextPane txtpnAllDates = new JTextPane();
		txtpnAllDates.setText("Show all entries");
		txtpnAllDates.setEditable(false);
		GridBagConstraints gbc_txtpnAllDates = new GridBagConstraints();
		gbc_txtpnAllDates.anchor = GridBagConstraints.BELOW_BASELINE;
		gbc_txtpnAllDates.gridwidth = 2;
		gbc_txtpnAllDates.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnAllDates.gridx = 0;
		gbc_txtpnAllDates.gridy = 0;
		panel.add(txtpnAllDates, gbc_txtpnAllDates);

		JList<CalendarEntry> list = new JList<CalendarEntry>(model);
		//	scrollPane.add(list);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridwidth = 3;
		gbc_list.gridheight = 4;
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.gridx = 2;
		gbc_list.gridy = 0;
		panel.add(list, gbc_list);
		//	add(scrollPane);


		//	for(CalendarEntry entry : entrylist){
		//		list.		}
		//panel.add(list);

		final	JRadioButton rdbtnSingleDate = new JRadioButton("single date");
		GridBagConstraints gbc_rdbtnSingleDate = new GridBagConstraints();
		gbc_rdbtnSingleDate.gridwidth = 2;
		gbc_rdbtnSingleDate.anchor = GridBagConstraints.WEST;
		gbc_rdbtnSingleDate.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnSingleDate.gridx = 0;
		gbc_rdbtnSingleDate.gridy = 1;
		panel.add(rdbtnSingleDate, gbc_rdbtnSingleDate);

		final JRadioButton rdbtnPeriod = new JRadioButton("Period");
		GridBagConstraints gbc_rdbtnPeriod = new GridBagConstraints();
		gbc_rdbtnPeriod.gridwidth = 2;
		gbc_rdbtnPeriod.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPeriod.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPeriod.gridx = 0;
		gbc_rdbtnPeriod.gridy = 2;
		panel.add(rdbtnPeriod, gbc_rdbtnPeriod);

		final JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setSelected(true);
		GridBagConstraints gbc_rdbtnAll = new GridBagConstraints();
		gbc_rdbtnAll.gridwidth = 2;
		gbc_rdbtnAll.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnAll.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAll.gridx = 0;
		gbc_rdbtnAll.gridy = 3;
		panel.add(rdbtnAll, gbc_rdbtnAll);

		ActionListener singleListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){

				rdbtnAll.setSelected(false);
				rdbtnPeriod.setSelected(false);

				Date date = selectSingleDateDialog();

				txtpnAllDates.setText("Show entries for: " + date);
				
				refreshEntriesSingleDate(date);
			}

		};
		rdbtnSingleDate.addActionListener(singleListener);

		ActionListener allListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){

				rdbtnSingleDate.setSelected(false);
				rdbtnPeriod.setSelected(false);

				txtpnAllDates.setText("Show all entries");
				//Add all entries to the listmodel
				model.removeAllElements();
				for(CalendarEntry entry : entrylist){
					
					model.addElement(entry);
				}
			}			
		};
		rdbtnAll.addActionListener(allListener);

		ActionListener periodListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){

				rdbtnAll.setSelected(false);
				rdbtnSingleDate.setSelected(false);

				Date[] dates = selectPeriodDialog();
			//	System.out.println("Show entries from: " + dates[0] + " to: "+dates[1]);
				txtpnAllDates.setText("Show entries from: " + dates[0] + " to: "+dates[1]);
				
				refreshEntriesForPeriod(dates);

			}			
		};		
		rdbtnPeriod.addActionListener(periodListener);
	}
	/**
	 * Refresh the List Model so, that it only shows entries 
	 * on the same date as the Date selected.
	 * @param date the date on which entries should be shown. Timestamp is ignored!
	 */
	private void refreshEntriesSingleDate(Date date){
		if (date != null){
			model.removeAllElements();
			for(CalendarEntry entry : entrylist){
				//Only same date, not same time!
				Date newd = entry.getDate();
				if    ((newd.getYear() == date.getYear())
					&& (newd.getMonth()== date.getMonth())
					&& (newd.getDate()  == date.getDate())){
					
					model.addElement(entry);
				}
			}
		}
	}
	
	/**
	 * refreshes the List Model so, that it only shows entries in the
	 * specific period
	 * @param dates an array with two dates at [0] and [1]
	 */
	private void refreshEntriesForPeriod(Date[] dates){
		if ((dates[0] != null) && (dates[1] != null)){
			model.removeAllElements();
			for(CalendarEntry entry : entrylist){
				//is the date in the period
				if ((entry.getDate().after(dates[0]) && (entry.getDate().before(dates[1])))){
					model.addElement(entry);
				}
			}
		}
	}
	
	/**
	 * opens the dialog and returns the dates
	 * @return an array of Date with two date objects in [0] and [1]
	 */
	private Date[] selectPeriodDialog(){
		DoubleCalendarDialog calDia = new DoubleCalendarDialog(parent, "Select start and end date", true);
		return calDia.getDate();
	}

	/**
	 * Opens a Dialog to choose a date and returns the selected date
	 * @return the date that was selected by the user
	 */
	private Date selectSingleDateDialog() {
		CalendarDialog cal = new CalendarDialog(this.parent,"choose a date", true);

		Date date = cal.getDate();

		return date;
	}
}
