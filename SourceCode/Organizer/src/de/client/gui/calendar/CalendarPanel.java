package de.client.gui.calendar;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * This Panel shows CalendarEntry Items
 * @author adrian
 *
 */
public class CalendarPanel extends JPanel {

	/**
	 * Creates a new Calendar Panel.
	 */
	public CalendarPanel(List<CalendarEntry> entrylist) {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 450, 300);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		//setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		
		//List model
		DefaultListModel<CalendarEntry> model = new DefaultListModel<CalendarEntry>();
		for(CalendarEntry entry : entrylist){
			model.addElement(entry);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{105, 71, 108, 6, 108, 42, 0};
		gbl_panel.rowHeights = new int[]{133, 23, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
			
			JTextPane txtpnAllDates = new JTextPane();
			txtpnAllDates.setText("All Dates");
			txtpnAllDates.setEditable(false);
			GridBagConstraints gbc_txtpnAllDates = new GridBagConstraints();
			gbc_txtpnAllDates.gridwidth = 2;
			gbc_txtpnAllDates.fill = GridBagConstraints.VERTICAL;
			gbc_txtpnAllDates.insets = new Insets(0, 0, 5, 5);
			gbc_txtpnAllDates.gridx = 0;
			gbc_txtpnAllDates.gridy = 0;
			panel.add(txtpnAllDates, gbc_txtpnAllDates);
			
			JList list = new JList(model);
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
			
			JRadioButton rdbtnSingleDate = new JRadioButton("single date");
			GridBagConstraints gbc_rdbtnSingleDate = new GridBagConstraints();
			gbc_rdbtnSingleDate.gridwidth = 2;
			gbc_rdbtnSingleDate.anchor = GridBagConstraints.WEST;
			gbc_rdbtnSingleDate.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnSingleDate.gridx = 0;
			gbc_rdbtnSingleDate.gridy = 1;
			panel.add(rdbtnSingleDate, gbc_rdbtnSingleDate);
		
		JRadioButton rdbtnPeriod = new JRadioButton("Period");
		GridBagConstraints gbc_rdbtnPeriod = new GridBagConstraints();
		gbc_rdbtnPeriod.gridwidth = 2;
		gbc_rdbtnPeriod.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPeriod.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPeriod.gridx = 0;
		gbc_rdbtnPeriod.gridy = 2;
		panel.add(rdbtnPeriod, gbc_rdbtnPeriod);
		
		JRadioButton rdbtnAll = new JRadioButton("All");
		GridBagConstraints gbc_rdbtnAll = new GridBagConstraints();
		gbc_rdbtnAll.gridwidth = 2;
		gbc_rdbtnAll.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnAll.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAll.gridx = 0;
		gbc_rdbtnAll.gridy = 3;
		panel.add(rdbtnAll, gbc_rdbtnAll);
	}
}
