package main;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;

import controller.Controller;
import json.JSONException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import command.*;

import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import java.awt.Font;


public class MainWindow {
	private String EMPTY_STRING = "";
	private String NEW_LINE = "\n";
	private JFrame frame;
	private JPanel mainTab;
	private int DISPLAYED_DAYS_NUM = 7;
	private int WINDOW_X = 100;
	private int WINDOW_Y = 100;
	private int WINDOW_WIDTH = 1200;
	private int WINDOW_HEIGHT = 800;
	private JTextField textField;
	private JTable calendarTable;
	private JPanel calendarPanel;
	private JPanel infoPanel;
	private static JTextArea actionsTextArea;
	
	private static Color navbarColor;
	private static Color backgroundColor;
	private static Color buttonColor;
	private static Color darkGreen;
	public static Color fontColor;
	public static Color lightGray;
	public static Color borderColor;
	
	private Controller mainController;
	private State currentState;
	private Calendar calendarInstance;
	private Calendar startCalendar;
	private Calendar endCalendar;
	
	static JLabel lblMonth;
	static JTable tblCalendar;
	static JFrame frmMain;
	static Container pane;
	static DefaultTableModel mtblCalendar; //Table model
	static JScrollPane stblCalendar; //The scrollpane
	static JScrollPane areaScrollPane;	//text area scrollpane
	static JPanel pnlCalendar; //The panel
	static int realDay, realMonth, realYear, currentMonth, currentYear;
	private JTable rowHeaderTable;
	private JLabel lblInfoEventDescription;
	private JLabel lblInfoEventStartTime;
	private JLabel lblInfoEventEndTime;
	private JLabel lblInfoEventCategory;
	private JLabel lblInfoEventName;
	private JLabel lblInfoEventLocation;
	private JToggleButton toggleButton;


	/**
	 * Launch the application.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		
		
		/*_________ Testing ADD ______________*/
		//Creating object manually
		//Controller controller = new Controller();
		//State completeState = new State();
		/*
		Event testNewEvent = new Event();
		Event brunch = new Event();
		String result = new String();
		final String finalResult;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "31-08-1982 10:20:56";
		Date aTime = null;
		try {
			aTime = sdf.parse(dateInString);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		testNewEvent.setName("TEST EVENT NAME");
		testNewEvent.setDescription("This is a test event created in main");
		testNewEvent.setLocation("Supahotfire's house");
		testNewEvent.setStatus(Event.Status.COMPLETE);
		testNewEvent.setStartTime(aTime);
		testNewEvent.setEndTime(aTime);
		
		brunch.setName("brunch");
		brunch.setDescription("with supa fire");
		brunch.setLocation("Supahotfire's house");
		brunch.setStatus(Event.Status.INCOMPLETE);
		brunch.setStartTime(aTime);
		brunch.setEndTime(aTime);
		
		
		Command adding = new Add(testNewEvent);
		completeState = adding.execute(completeState);
		/*
		Command adding2 = new Add(testNewEvent);
		Command adding3 = new Add(brunch);
		//Add event, event, brunch to test delete event
		
		completeState = adding2.execute(completeState);
		completeState = adding3.execute(completeState);
		
		Command deleting = new Delete(brunch);
		completeState = deleting.execute(completeState);
		
		
		for(Event e: completeState.displayedEvents){
			result = result + e.printEvent();
			System.out.println(result);
			
		}
	
		finalResult = result;*/
		
		/*_________ Testing ADD ______________*/
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					
					//for Testing Purposes
					//actionsTextArea.append(finalResult);

					window.frame.setVisible(true);
					window.renderCalendar();
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
		initializeController();
		
		intializeState();
				
		initializeColors();
		
		initializeMainWindow();
		
		initializeTabButtons();
		
		initializeInfoSection();
		
		initializeMainTab();
				
		initializeInputField();
		
		initializeOutputField();
		
		initializeCalendar();
		
		
	}

	private void initializeController() {
		mainController = new Controller();
	}
	
	private void intializeState() {
		currentState = new State();
		currentState = mainController.getCompleteState();
	}
	
	private void initializeColors() {
		navbarColor = new Color(55, 71, 79);
		backgroundColor = new Color(243, 243, 244);
		buttonColor = new Color(28, 192, 159);
		darkGreen = new Color(23, 152, 126);
		lightGray = new Color(244, 246, 250);
		borderColor = new Color(231, 234, 236);
		fontColor = new Color(103, 106, 108);
	}
	
	private void initializeMainWindow() {
		navbarColor = new Color(55, 71, 79);
		frame = new JFrame();
		frame.getContentPane().setBackground(navbarColor);
		frame.setBackground(navbarColor);
		frame.setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	}
	
	private void initializeTabButtons() {
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Today");
		tglbtnNewToggleButton.setForeground(Color.WHITE);
		tglbtnNewToggleButton.setBorder(null);
		tglbtnNewToggleButton.setBackground(buttonColor);
		tglbtnNewToggleButton.setBounds(0, 68, 119, 34);
		frame.getContentPane().add(tglbtnNewToggleButton);
		
		JToggleButton tglbtnNewToggleButton_1 = new JToggleButton("Undetermined");
		tglbtnNewToggleButton_1.setForeground(Color.WHITE);
		tglbtnNewToggleButton_1.setBorder(null);
		tglbtnNewToggleButton_1.setBackground(buttonColor);
		tglbtnNewToggleButton_1.setBounds(0, 0, 119, 34);
		frame.getContentPane().add(tglbtnNewToggleButton_1);
		
		JToggleButton tglbtnNewToggleButton_2 = new JToggleButton("Completed");
		tglbtnNewToggleButton_2.setForeground(Color.WHITE);
		tglbtnNewToggleButton_2.setBorder(null);
		tglbtnNewToggleButton_2.setBackground(buttonColor);
		tglbtnNewToggleButton_2.setBounds(0, 34, 119, 34);
		frame.getContentPane().add(tglbtnNewToggleButton_2);
	}
	
	private void initializeInfoSection() {
		infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, borderColor));
		infoPanel.setBounds(119, 0, 286, 761);
		infoPanel.setBackground(lightGray);
		frame.getContentPane().add(infoPanel);
		
		lblInfoEventName = new JLabel("PLACEHOLDER");
		lblInfoEventName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInfoEventName.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventName.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
		lblInfoEventName.setBounds(0, 0, 286, 45);
		lblInfoEventName.setForeground(fontColor);
		infoPanel.add(lblInfoEventName);
		
		lblInfoEventDescription = new JLabel("PLACEHOLDER");
		lblInfoEventDescription.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventDescription.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventDescription.setBounds(0, 54, 286, 40);
		lblInfoEventDescription.setForeground(fontColor);
		infoPanel.add(lblInfoEventDescription);
		
		lblInfoEventLocation = new JLabel("PLACEHOLDER");
		lblInfoEventLocation.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventLocation.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventLocation.setBounds(0, 100, 286, 14);
		lblInfoEventLocation.setForeground(fontColor);
		infoPanel.add(lblInfoEventLocation);
		
		lblInfoEventStartTime = new JLabel("PLACEHOLDER");
		lblInfoEventStartTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventStartTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventStartTime.setBounds(0, 120, 286, 14);
		lblInfoEventStartTime.setForeground(fontColor);
		infoPanel.add(lblInfoEventStartTime);
		
		lblInfoEventEndTime = new JLabel("PLACEHOLDER");
		lblInfoEventEndTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventEndTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventEndTime.setBounds(0, 140, 286, 14);
		lblInfoEventEndTime.setForeground(fontColor);
		infoPanel.add(lblInfoEventEndTime);
		
		lblInfoEventCategory = new JLabel("PLACEHOLDER");
		lblInfoEventCategory.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventCategory.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventCategory.setBounds(0, 160, 286, 14);
		lblInfoEventCategory.setForeground(fontColor);
		infoPanel.add(lblInfoEventCategory);
		
		infoPanel.setLayout(null);		
	}
	
	private void displayEventDetails(Event currentEvent) {
		lblInfoEventName.setText(currentEvent.getName());
		
		lblInfoEventDescription.setText(currentEvent.getDescription());
		
		lblInfoEventLocation.setText(currentEvent.getLocation());
		
		lblInfoEventStartTime.setText(currentEvent.getStartTime().toString());
		
		lblInfoEventEndTime.setText(currentEvent.getEndTime().toString());
		
		lblInfoEventCategory.setText(currentEvent.getCategory());
	}
	
	private void initializeMainTab() {
		mainTab = new JPanel();
		mainTab.setBackground(Color.WHITE);
		mainTab.setLayout(null);
		mainTab.setBounds(402, 0, 782, 761);
		frame.getContentPane().add(mainTab);
	}
	
	private void initializeInputField() {
		toggleButton = new JToggleButton("GO");
		toggleButton.setForeground(Color.WHITE);
		toggleButton.setBorder(null);
		toggleButton.setBackground(new Color(28, 192, 159));
		toggleButton.setBounds(707, 730, 64, 20);
		mainTab.add(toggleButton);
		
		textField = new JTextField();
		textField.setBorder(new LineBorder(borderColor));
		
		Action inputAction = getInputAction();
		textField.addActionListener(inputAction);
		textField.setColumns(10);
		textField.setBounds(10, 500, 698, 20);
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
		    	
		    	//calling controller
		    	try {
					currentState = mainController.executeCommand(inputString);
				} catch (IOException | JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	renderCalendar();
		    }
		};
		return action;
	}
	
	private void renderCalendar() {
		tblCalendar.removeAll();
		
		for (Event event : currentState.displayedEvents){
	    	//actionsTextArea.append(event.printEvent());
			
	    	if (event.isDeadline()) {
	    		createDeadlineEvent(event);
	    	} else if (event.isEvent()) {
	    		createSpecificEvent(event);
	    	}
	    	displayEventDetails(event);
    	}
	}
	
	private void initializeOutputField() {
		actionsTextArea = new JTextArea("BRUH");
		actionsTextArea.setBounds(10, 478, 761, 252);
		//actionsTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, borderColor));
		actionsTextArea.setEditable(false);
		
		areaScrollPane = new JScrollPane(actionsTextArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, borderColor));
		areaScrollPane.setBounds(10, 478, 761, 252);
	    
		mainTab.add(areaScrollPane);
	}
	
	private void initializeCalendar() {
		initializeCalendarInstance();
		
		initiliazeCalendarComponents();
		
		addCalendarComponents();
		
		setBoundsCalendarComponents();
		
		setCalendarHeaders();
		
		setCalendarRows();
		
        setSingleCellSelection();
	}
	
	private void initializeCalendarInstance() {
		calendarInstance = Calendar.getInstance();
	}
	
	private void initiliazeCalendarComponents() {
		mtblCalendar = getDefaultTableModel();
		tblCalendar = new JTable(mtblCalendar); //Table using the above model
		tblCalendar.setGridColor(borderColor);
		stblCalendar = new JScrollPane(tblCalendar); //The scrollpane of the above table
		stblCalendar.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, borderColor));
		stblCalendar.setBackground(Color.WHITE);
		stblCalendar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		calendarPanel = new JPanel();
		calendarPanel.setBackground(Color.WHITE);
		calendarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
		rowHeaderTable = new RowNumberTable(tblCalendar);
		rowHeaderTable.setGridColor(borderColor);
	}
	
	private void refreshMonth() {
		Date currentDate = calendarInstance.getTime();
		SimpleDateFormat format = getHeaderFormat();
		lblMonth.setText(format.format(currentDate));
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
		mainTab.add(calendarPanel);
		calendarPanel.setLayout(null);
		lblMonth = new JLabel();
		lblMonth.setBounds(0, 0, 782, 45);
		calendarPanel.add(lblMonth);
		lblMonth.setForeground(fontColor);
		lblMonth.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
		calendarPanel.add(stblCalendar);

		refreshMonth();
	}
	
	private void createDeadlineEvent(Event deadline) {
		updateStartCalendar();
		updateEndCalendar();
		
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		Date deadlineDate = deadline.getEndTime();
		
		Calendar deadlineCalendar = (Calendar) calendarInstance.clone();
		deadlineCalendar.setTime(deadlineDate);
		
		if (deadlineDate.after(startDate) && deadlineDate.before(endDate)) {
			addDeadlineEvent(deadline, deadlineCalendar, startCalendar);
		}
	}
	
	private void updateStartCalendar() {
		startCalendar = (Calendar) calendarInstance.clone();
	}
	
	private void updateEndCalendar() {
		endCalendar = (Calendar) calendarInstance.clone();
		endCalendar.set(Calendar.DAY_OF_MONTH, calendarInstance.get(Calendar.DAY_OF_MONTH) + DISPLAYED_DAYS_NUM);
		endCalendar.set(Calendar.HOUR_OF_DAY, 0);
		endCalendar.set(Calendar.MINUTE, 0);
	}
	
	private void addDeadlineEvent(Event deadline, Calendar deadlineCalendar, Calendar startCalendar) {
		double eventHeight = getEventHeight();
		double eventWidth = getEventWidth();
		int dayDifference = deadlineCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR);
		int hour = deadlineCalendar.get(Calendar.HOUR_OF_DAY);
		int xOffset = (int) eventWidth * dayDifference;
		int yOffset = (int) eventHeight * hour;
		
		JTextField currentEvent = new JTextField(deadline.getName());
		currentEvent.setBounds(xOffset, yOffset, (int) eventWidth, (int) eventHeight);
		currentEvent.setBackground(darkGreen);
		currentEvent.setBorder(null);
		currentEvent.setEditable(false);
		currentEvent.setHorizontalAlignment(JTextField.CENTER);
		currentEvent.setForeground(Color.WHITE);
		tblCalendar.add(currentEvent);
	}
	
	private double getEventHeight() {
		double height = tblCalendar.getHeight() / (double) 24;
		return height;
	}
	
	private double getEventWidth() {
		double width = tblCalendar.getWidth() / (double) DISPLAYED_DAYS_NUM;
		return width;
	}
	
	private void createSpecificEvent(Event specificEvent) {
		updateStartCalendar();
		updateEndCalendar();
		
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		Date startDateEvent = specificEvent.getStartTime();
		Date endDateEvent = specificEvent.getEndTime();

		Calendar startEventCalendar = (Calendar) calendarInstance.clone();
		startEventCalendar.setTime(startDateEvent);
		Calendar endEventCalendar = (Calendar) calendarInstance.clone();
		endEventCalendar.setTime(endDateEvent);
		
		addSpecificEvent(specificEvent, startEventCalendar, endEventCalendar);
	}
	
	private void addSpecificEvent(Event specificEvent, Calendar startEventCalendar, Calendar endEventCalendar) {
		double eventHeight = getEventHeight();
		double eventWidth = getEventWidth();
		int dayDifference = endEventCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR);
		int hour = startEventCalendar.get(Calendar.HOUR_OF_DAY);
		int xOffset = (int) eventWidth * dayDifference;
		int yOffset = (int) eventHeight * hour;
		double yMultiplier = (endEventCalendar.get(Calendar.HOUR_OF_DAY) - startEventCalendar.get(Calendar.HOUR_OF_DAY) +
				(endEventCalendar.get(Calendar.MINUTE) - startEventCalendar.get(Calendar.MINUTE)) / 60.0);
		eventHeight *= yMultiplier;
		
		JTextField currentEvent = createEventBlock(specificEvent.getName(), xOffset, yOffset, (int) eventWidth, (int) eventHeight);
		tblCalendar.add(currentEvent);
	}
	
	private JTextField createEventBlock(String name, int xOffset, int yOffset, int eventWidth, int eventHeight) {
		JTextField currentEvent = new JTextField(name);
		currentEvent.setBounds(xOffset, yOffset, (int) eventWidth, (int) eventHeight);
		currentEvent.setBackground(darkGreen);
		currentEvent.setBorder(null);
		currentEvent.setEditable(false);
		currentEvent.setHorizontalAlignment(JTextField.CENTER);
		currentEvent.setForeground(Color.WHITE);
		return currentEvent;
	}
	
	private void setBoundsCalendarComponents() {
		calendarPanel.setBounds(0, 0, 782, 467);
		tblCalendar.setSize(100, 100);
		stblCalendar.setBounds(10, 54, 762, 402);
	}
	
	private void setCalendarHeaders() {
		SimpleDateFormat format = getHeaderTableFormat();
		String formatted = format.format(calendarInstance.getTime());
		Calendar calendarClone = (Calendar) calendarInstance.clone();
		
		for (int i=0; i < DISPLAYED_DAYS_NUM; i++) {
			formatted = format.format(calendarClone.getTime());
			calendarClone.set(Calendar.DAY_OF_MONTH, calendarClone.get(Calendar.DAY_OF_MONTH) + 1);
        	mtblCalendar.addColumn(formatted);
        }
		JTableHeader header = tblCalendar.getTableHeader();
		header.setBackground(Color.WHITE);
		header.setForeground(fontColor);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderColor));
	}
	
	private SimpleDateFormat getHeaderTableFormat() {
		SimpleDateFormat format = new SimpleDateFormat("E (d)");
		return format;
	}
	
	private SimpleDateFormat getHeaderFormat() {
		SimpleDateFormat format = new SimpleDateFormat("MMMM / yyyy");
		return format;
	}
	
	private void setCalendarRows() {
		mtblCalendar.setRowCount(24);
		rowHeaderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowHeaderTable.setRowSelectionAllowed(true);
		rowHeaderTable.setRowHeight(12);
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
