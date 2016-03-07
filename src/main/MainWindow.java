package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.JTextField;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import constant.Constant;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class MainWindow {
	private String EMPTY_STRING = "";
	private String NEW_LINE = "\n";
	private JFrame frame;
	private JPanel mainTab;
	private int WINDOW_X = 100;
	private int WINDOW_Y = 100;
	private int WINDOW_WIDTH = 1200;
	private int WINDOW_HEIGHT = 800;
	private JTextField textField;
	private JTable calendarTable;
	private JPanel calendarPanel;
	private static JTextArea actionsTextArea;
	
	static JLabel lblMonth;
	static JButton btnPrev, btnNext;
	static JTable tblCalendar;
	static JFrame frmMain;
	static Container pane;
	static DefaultTableModel mtblCalendar; //Table model
	static JScrollPane stblCalendar; //The scrollpane
	static JPanel pnlCalendar; //The panel
	static int realDay, realMonth, realYear, currentMonth, currentYear;
	private JTable rowHeaderTable;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//For Testing Purposes
		/*Index Positions of Data
 			public static final int NAME = 0;
			public static final int DESCRIPTION = 1;
			public static final int START_TIME = 2;
			public static final int END_TIME = 3;
			public static final int STATUS = 4;
		public static final int CATEGORY = 5;
		 */
		String[][] testParsedInformation = {
				{
				"testName",
				"test description blah blah but i'm not a rapper",
				"2016-01-01 00:00:00.0",
				"2011-01-19 00:00:00.0",
				Constant.CATEGORY_UNDETERMINED,
				"SCHOOL CATEGORY"
				}
		};
		
		InfoHandler info =  new InfoHandler(testParsedInformation);
		info.setNumberOfEvents(testParsedInformation.length);
		ArrayList<Event> allEvents = InfoHandler.processInfo();
		//Getting Events to display in Text Area
		String result = new String("");
		
		for(int i = 0; i < info.numberOfEvents; i++){
			result = result + allEvents.get(i).printEvent();
			
		}
		final String finalResult = result;
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					actionsTextArea.append(finalResult);

					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		initializeMainWindow();
		
		initializeTabButtons();
		
		initializeMainTab();
		
		initializeDetailScrollbar();
		
		initializeInputField();
		
		initializeOutputField();
		
		initializeCalendar();
	}
	
	private void initializeMainWindow() {
		frame = new JFrame();
		frame.setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	}
	
	private void initializeTabButtons() {
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Today");
		tglbtnNewToggleButton.setBounds(0, 68, 119, 23);
		frame.getContentPane().add(tglbtnNewToggleButton);
		
		JToggleButton tglbtnNewToggleButton_1 = new JToggleButton("Undetermined");
		tglbtnNewToggleButton_1.setBounds(0, 0, 119, 23);
		frame.getContentPane().add(tglbtnNewToggleButton_1);
		
		JToggleButton tglbtnNewToggleButton_2 = new JToggleButton("Completed");
		tglbtnNewToggleButton_2.setBounds(0, 34, 119, 23);
		frame.getContentPane().add(tglbtnNewToggleButton_2);
	}
	
	private void initializeMainTab() {
		mainTab = new JPanel();
		mainTab.setLayout(null);
		mainTab.setBounds(118, 0, 1056, 750);
		frame.getContentPane().add(mainTab);
	}
	
	private void initializeDetailScrollbar() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 265, 719);
		mainTab.add(scrollPane);
	}
	
	private void initializeInputField() {
		textField = new JTextField();
		
		Action inputAction = getInputAction();
		textField.addActionListener(inputAction);
		textField.setColumns(10);
		textField.setBounds(0, 730, 1046, 20);
		mainTab.add(textField);
	}
	
	private Action getInputAction() {
		Action action = new AbstractAction()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	String inputString = textField.getText() + NEW_LINE;
		    	textField.setText(EMPTY_STRING);
		    	actionsTextArea.append(inputString);
		    	
		        System.out.println("");
		    }
		};
		return action;
	}
	
	private void initializeOutputField() {
		actionsTextArea = new JTextArea("BRUH");
		actionsTextArea.setBounds(275, 531, 771, 188);
		actionsTextArea.setEditable(false);
		mainTab.add(actionsTextArea);
	}
	
	private void initializeCalendar() {
		initiliazeCalendarComponents();
		
		addCalendarComponents();
		
		setBoundsCalendarComponents();
		
		setCalendarHeaders();
		
		setCalendarRows();
		
        setSingleCellSelection();
	}
	
	private void initiliazeCalendarComponents() {
		lblMonth = new JLabel ("January");
		btnPrev = new JButton ("<<");
		btnNext = new JButton (">>");
		mtblCalendar = getDefaultTableModel();
		tblCalendar = new JTable(mtblCalendar); //Table using the above model
		stblCalendar = new JScrollPane(tblCalendar); //The scrollpane of the above table
		calendarPanel = new JPanel();
		rowHeaderTable = new RowNumberTable(tblCalendar);
	}
	
	private DefaultTableModel getDefaultTableModel() {
		DefaultTableModel defaultModel = new DefaultTableModel() {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
		    }
		};
		return defaultModel;
	}
	
	private void addCalendarComponents() {
		calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
		mainTab.add(calendarPanel);
		calendarPanel.setLayout(null);
		calendarPanel.add(btnPrev);
		calendarPanel.add(lblMonth);
		calendarPanel.add(btnNext);
		calendarPanel.add(stblCalendar);
	}
	
	private void setBoundsCalendarComponents() {
		calendarPanel.setBounds(275, 0, 771, 519);
		lblMonth.setBounds(348, 85, 49, 14);
		btnPrev.setBounds(289, 81, 49, 23);
		btnNext.setBounds(407, 81, 49, 23);
		tblCalendar.setSize(100, 100);
		stblCalendar.setBounds(10, 110, 751, 402);
	}
	
	private void setCalendarHeaders() {
		String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
		for (int i=0; i < 7; i++) {
        	mtblCalendar.addColumn(headers[i]);
        }	
	}
	
	private void setCalendarRows() {
		mtblCalendar.setRowCount(24);
		rowHeaderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowHeaderTable.setRowSelectionAllowed(true);
		rowHeaderTable.setRowHeight(38);
		rowHeaderTable.setColumnSelectionAllowed(true);
		stblCalendar.setRowHeaderView(rowHeaderTable);
	}
	
	private void setSingleCellSelection() {
        tblCalendar.setColumnSelectionAllowed(true);
        tblCalendar.setRowSelectionAllowed(true);
        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCalendar.setRowHeight(38);
	}
}
