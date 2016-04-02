package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;

import controller.Controller;
import json.JSONException;
import storage.Storage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit.UnderlineAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;

import java.awt.Component;
import java.awt.ComponentOrientation;


public class MainWindow {
	private String EMPTY_STRING = "";
	private String NEW_LINE = "\n";
	private JFrame frame;
	private JPanel mainTab;
	private int DISPLAYED_DAYS_NUM = 7;
	private int WINDOW_X = 100;
	private int WINDOW_Y = 100;
	private int WINDOW_WIDTH = 1600;
	private int WINDOW_HEIGHT = 800;
	private JTextField textField;
	private JTable calendarTable;
	private JPanel calendarPanel;
	private JPanel infoPanel;
	private JPanel infoSectionWrapper;
	private static JTextArea actionsTextArea;
	
	private static Color navbarColor;
	private static Color backgroundColor;
	private static Color buttonColor;
	private static Color darkGreen;
	private static Color lightRed;
	public static Color fontColor;
	public static Color lightGray;
	public static Color borderColor;
	public static Color transperantColor;
	private static ArrayList<Color> randomColors;
	private static HashMap<String, Color> coloredEvents;
	
	private Controller mainController;
	private State currentState;
	private Calendar calendarInstance;
	private Calendar startCalendar;
	private Calendar endCalendar;
	private JToggleButton incompletedTab;
	
	private JToggleButton undeterminedTab;
	
	private JToggleButton completedTab;

	
	static JLabel lblMonth;
	static JTable tblCalendar;
	static JFrame frmMain;
	static Container pane;
	static DefaultTableModel mtblCalendar; //Table model
	static JScrollPane stblCalendar; //The scrollpane
	static JScrollPane areaScrollPane;	//text area scrollpane
	static JScrollPane infoScrollPane;
	static JPanel pnlCalendar; //The panel
	static int realDay, realMonth, realYear, currentMonth, currentYear;
	private RowNumberTable rowHeaderTable;
	private JToggleButton toggleButton;
	private JLabel lblGuidance;
	private JLabel lblInfoEventName;
	private JTextArea lblAdd;
	
	private String defaultStorage = "./storage/storage.txt";
	private Color Color1;
	private Color Color2;
	private Color Color3;
	private Color Color4;
	private Color Color5;


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
		initializeFile();
		
		initializeController();
		
		intializeState();
				
		initializeColors();
		
		initializeMainWindow();
		
		initializeTabButtons();
		
		//initializeInfoSection();
		
		initializeMainTab();
				
		initializeInputField();
		
		initializeOutputField();
		
		initializeCalendar();
		
		initializeInfoSection();
		
		
	}

	private void initializeFile() {
		Storage storage = new Storage();
		storage.createFile(Storage.storageFile);
	}

	private void initializeController() {
		mainController = new Controller();
	}
	
	private void intializeState() {
		//currentState = new State();
		currentState = mainController.getCompleteState();
	}
	
	private void initializeColors() {
		navbarColor = new Color(55, 71, 79);
		backgroundColor = new Color(243, 243, 244);
		buttonColor = new Color(28, 192, 159);
		lightGray = new Color(244, 246, 250);
		borderColor = new Color(231, 234, 236);
		fontColor = new Color(103, 106, 108);
		lightRed = new Color (231,111,81,160);
		darkGreen = new Color(42,157,143, 160);
		transperantColor = new Color(42,157,143, 0);
		randomColors = new ArrayList<Color>();
		
		//Color1 = new Color(255,150,180,150);
		Color2 = new Color(182, 166, 202, 150);
		Color3 = new Color(134,222,183,150);
		Color4 = new Color(255, 241, 208, 150);
		Color5 = new Color(192, 246, 255, 150);
		
		//randomColors.add(Color1);
		randomColors.add(Color2);
		randomColors.add(Color3);
		randomColors.add(Color4);
		randomColors.add(Color5);
		
		/*
		for (int i = 0; i < 300; i++) {
			Color currentColor = new Color((int) (Math.random() * 255),
										   (int) (Math.random() * 255),
										   (int) (Math.random() * 255),
										   160);
			randomColors.add(currentColor);			
		}*/
		coloredEvents = new HashMap<String, Color>();
		
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
		incompletedTab = new JToggleButton("Incompleted");
		incompletedTab.setForeground(Color.WHITE);
		incompletedTab.setBorder(null);
		incompletedTab.setBackground(buttonColor);
		incompletedTab.setBounds(0, 68, 119, 34);
		frame.getContentPane().add(incompletedTab);
		
		undeterminedTab = new JToggleButton("Undetermined");
		undeterminedTab.setForeground(Color.WHITE);
		undeterminedTab.setBorder(null);
		undeterminedTab.setBackground(buttonColor);
		undeterminedTab.setBounds(0, 0, 119, 34);
		frame.getContentPane().add(undeterminedTab);
		
		completedTab = new JToggleButton("Completed");
		completedTab.setForeground(Color.WHITE);
		completedTab.setBorder(null);
		completedTab.setBackground(buttonColor);
		completedTab.setBounds(0, 34, 119, 34);
		frame.getContentPane().add(completedTab);
	}
	
	private void initializeInfoSection() {
	}
	
	private void displayEventDetails(Event currentEvent) {	
		JPanel infoSectionWrapper = new JPanel();
		infoSectionWrapper.setLayout(null);
		infoSectionWrapper.setSize(new Dimension(infoPanel.getWidth(), 200));
		infoSectionWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.RED));
		
		infoPanel.add(infoSectionWrapper);
		
		addEventDetails(infoSectionWrapper, currentEvent);	
	}
	
	private void displayMultipleEventsDetails(ArrayList<Event> events) {
		JPanel infoSectionWrapper = new JPanel();
		infoSectionWrapper.setLayout(null);
		infoSectionWrapper.setSize(new Dimension(infoPanel.getWidth(), 200));
		infoSectionWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.RED));
		
		infoPanel.add(infoSectionWrapper);
		int counter = 0;
		for (Event currentEvent : events) {
			addEventDetails(infoSectionWrapper, currentEvent, counter);
			counter++;
			if (counter >= 6) {
				break;
			}
		}
	}
	
	private void addEventDetails(JPanel wrapper, Event currentEvent) {
		this.addEventDetails(wrapper, currentEvent, 0);
	}
	
	private void addEventDetails(JPanel wrapper, Event currentEvent, int elementNumber) {		
		JPanel currentPanel = new JPanel();
		currentPanel.setBounds(0, 113 * elementNumber, infoPanel.getWidth(), 113);
		GridBagLayout layout = new GridBagLayout();
		currentPanel.setLayout(layout);
		
		GridBagConstraints c = new GridBagConstraints();
	    c.ipady = 5;      //make this component tall
	    c.gridwidth = 5;
	    c.gridx = 0;
	    c.gridy = 0;
		
		JLabel lblInfoEventName = createInfoLabelTitle(currentEvent, elementNumber);
		layout.setConstraints(lblInfoEventName, c);
		currentPanel.add(lblInfoEventName);
		
		JLabel lblInfoEventDescription = createInfoLabelDescription(currentEvent);
		c.gridy = 1;
		layout.setConstraints(lblInfoEventDescription, c);
		currentPanel.add(lblInfoEventDescription);
		
		JLabel lblInfoEventLocation = createInfoLabelLocation(currentEvent);
		c.gridy = 2;
		layout.setConstraints(lblInfoEventLocation, c);
		currentPanel.add(lblInfoEventLocation);
		
		JLabel lblInfoEventStartTime = createInfoLabelStartTime(currentEvent);
		c.gridy = 3;
		layout.setConstraints(lblInfoEventStartTime, c);
		currentPanel.add(lblInfoEventStartTime);
		
		JLabel lblInfoEventEndTime = createInfoLabelEndTime(currentEvent);
		c.gridy = 4;
		layout.setConstraints(lblInfoEventEndTime, c);
		currentPanel.add(lblInfoEventEndTime);
		
		JLabel lblInfoEventCategory = createInfoLabelCategory(currentEvent);
		c.gridy = 5;
		layout.setConstraints(lblInfoEventCategory, c);
		currentPanel.add(lblInfoEventCategory);
		
		wrapper.add(currentPanel);
	}

	private JLabel createInfoLabelCategory(Event currentEvent) {
		JLabel lblInfoEventCategory = new JLabel(currentEvent.getCategory().toString());
		lblInfoEventCategory.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventCategory.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventCategory.setForeground(fontColor);
		return lblInfoEventCategory;
	}

	private JLabel createInfoLabelEndTime(Event currentEvent) {
		JLabel lblInfoEventEndTime = new JLabel(currentEvent.getEndTime().toString());
		lblInfoEventEndTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventEndTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventEndTime.setForeground(fontColor);
		return lblInfoEventEndTime;
	}

	private JLabel createInfoLabelStartTime(Event currentEvent) {
		JLabel lblInfoEventStartTime = new JLabel(currentEvent.getStartTime().toString());
		lblInfoEventStartTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventStartTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventStartTime.setForeground(fontColor);
		return lblInfoEventStartTime;
	}

	private JLabel createInfoLabelLocation(Event currentEvent) {
		JLabel lblInfoEventLocation = new JLabel(currentEvent.getLocation());
		lblInfoEventLocation.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventLocation.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventLocation.setForeground(fontColor);
		return lblInfoEventLocation;
	}

	private JLabel createInfoLabelDescription(Event currentEvent) {
		JLabel lblInfoEventDescription = new JLabel(currentEvent.getDescription());
		lblInfoEventDescription.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblInfoEventDescription.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventDescription.setForeground(fontColor);
		return lblInfoEventDescription;
	}

	private JLabel createInfoLabelTitle(Event currentEvent, int id) {
		String title = String.format("[%d] %s", id + 1, currentEvent.getName());
		JLabel lblInfoEventName = new JLabel(title);
		lblInfoEventName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblInfoEventName.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEventName.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
		lblInfoEventName.setForeground(fontColor);
		return lblInfoEventName;
	}
	
	private void initializeMainTab() {
		mainTab = new JPanel();
		mainTab.setBackground(Color.WHITE);
		mainTab.setLayout(null);
		mainTab.setBounds(117, 0, 1467, 761);
		frame.getContentPane().add(mainTab);
	}
	
	private void initializeInputField() {
		infoPanel = new JPanel();
		infoPanel.setBounds(0, 0, 286, 425);
		mainTab.add(infoPanel);
		infoPanel.setBackground(lightGray);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		infoPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, borderColor));
		
		textField = new JTextField();
		textField.setBorder(new LineBorder(borderColor));
		
		textField.addKeyListener(new ChangeMonthListener());
		
		textField.setColumns(10);
		textField.setBounds(285, 460, 1100, 20);
		mainTab.add(textField);
	}
	
	private class ChangeMonthListener implements KeyListener {
		 public void keyTyped(KeyEvent e) {
		        // Invoked when a key has been typed.
		    }

	    public void keyPressed(KeyEvent e) {

	    }

	    public void keyReleased(KeyEvent e) {
	    	boolean actionTaken = false;
	        // Invoked when a key has been pressed.
	        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	        	calendarInstance.add(Calendar.DAY_OF_YEAR, DISPLAYED_DAYS_NUM);
	        	actionTaken = true;
	        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
	        	calendarInstance.add(Calendar.DAY_OF_YEAR, -DISPLAYED_DAYS_NUM);
	        	actionTaken = true;
	        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    	String inputString = textField.getText() + NEW_LINE;
		    	textField.setText(EMPTY_STRING);
		    	actionsTextArea.setText(inputString);
		    	
		    	//calling controller
		    	try {
					currentState = mainController.executeCommand(inputString, defaultStorage);
					actionTaken = true;
				} catch (IOException | JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
	        if (actionTaken) {
	        	rowHeaderTable.setCalendarInstance(calendarInstance);
	        	rowHeaderTable.refresh();
	        	renderCalendar();
	        }
	    }
	}
	
	private void renderCalendar() {
		
		tblCalendar.removeAll();
		
		infoPanel.removeAll();
		
		this.refreshMonth();
		
		this.renderEvents();
		
		this.renderInfoSection();
		
		this.renderTabSection();
		
		tblCalendar.revalidate();
		tblCalendar.repaint();
    	
		infoPanel.revalidate();
		infoPanel.repaint();
	}
	
	private void renderEvents() {
		ArrayList<Event> displayedEvents = currentState.displayedEvents;
		for (int i = 0; i < displayedEvents.size(); i++) {
			Event event = displayedEvents.get(i);
			if (event.isDeadline()) {
	    		createDeadlineEvent(event);
	    	} else if (event.isEvent()) {
	    		createSpecificEvent(event);
	    	}
		}
	}
	
	private void renderInfoSection() {
		if (currentState.hasSingleEventSelected()) {
			displayEventDetails(currentState.getSingleSelectedEvent());
		} else if (currentState.hasMultipleEventSelected()) {
			displayMultipleEventsDetails(currentState.getAllSelectedEvents());
		}
	}
	
	private void renderTabSection() {
		completedTab.setSelected(false);
		incompletedTab.setSelected(false);
		undeterminedTab.setSelected(false);
		if (currentState.isCompletedSelected()) {
			completedTab.setSelected(true);
		} else if (currentState.isIncompletedSelected()) {
			incompletedTab.setSelected(true);
		} else if (currentState.isUndeterminedSelected()) {
			undeterminedTab.setSelected(true);
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
		areaScrollPane.setBounds(285, 424, 1162, 37);
	    
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
		stblCalendar = new JScrollPane(tblCalendar); //The scrollpane of the above table`
		
		stblCalendar.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, borderColor));
		stblCalendar.setBackground(Color.WHITE);
		stblCalendar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		calendarPanel = new JPanel();
		calendarPanel.setBackground(Color.WHITE);
		calendarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
		rowHeaderTable = new RowNumberTable(tblCalendar, calendarInstance, this.DISPLAYED_DAYS_NUM);
		rowHeaderTable.setGridColor(borderColor);
		rowHeaderTable.refresh();
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
		toggleButton = new JToggleButton("GO");
		toggleButton.setBounds(1383, 460, 64, 20);
		mainTab.add(toggleButton);
		toggleButton.setForeground(Color.WHITE);
		toggleButton.setBorder(null);
		toggleButton.setBackground(new Color(28, 192, 159));
		mainTab.add(calendarPanel);
		calendarPanel.setLayout(null);
		calendarPanel.add(stblCalendar);
		lblMonth = new JLabel();
		lblMonth.setBounds(0, 436, 286, 44);
		mainTab.add(lblMonth);
		lblMonth.setForeground(fontColor);
		lblMonth.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));

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
		int xOffset = (int) (eventWidth * hour);
		int yOffset = (int) eventHeight * dayDifference ;
		
		JTextField currentEvent = createEventBlock(EMPTY_STRING, xOffset, yOffset, (int) eventWidth, (int) eventHeight, lightRed);
		JTextField currentEventName = createEventBlock(deadline.getName(), xOffset, yOffset, (int) eventWidth, (int) eventHeight, transperantColor);
		tblCalendar.add(currentEvent);
		tblCalendar.add(currentEventName);
		tblCalendar.setComponentZOrder(currentEventName, 1);
	}
	
	private double getEventHeight() {
		double height = tblCalendar.getHeight() / (double) DISPLAYED_DAYS_NUM;
		return height;
	}
	
	private double getEventWidth() {
		double width = tblCalendar.getWidth() / (double) 24;
		return width;
	}
	
	private void createSpecificEvent(Event specificEvent) {
		updateStartCalendar();
		updateEndCalendar();

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
		int dayDifference, hour, minute, yOffset, xOffset;
		double xMultiplier;
		Color currentColor;
		// TODO: Create a dictionary String: Color for each event
		if (coloredEvents.containsKey(specificEvent.getName())) {
			currentColor = coloredEvents.get(specificEvent.getName());
		} else {
			currentColor = randomColors.get((int)(Math.random() * randomColors.size()));
			coloredEvents.put(specificEvent.getName(), currentColor);
		}
		
		while (startEventCalendar.get(Calendar.DAY_OF_YEAR) < endEventCalendar.get(Calendar.DAY_OF_YEAR)) {
			double currentEventWidth = eventWidth;
			dayDifference = startEventCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR);
			hour = startEventCalendar.get(Calendar.HOUR_OF_DAY);
			minute = startEventCalendar.get(Calendar.MINUTE);
			yOffset = (int) eventHeight * dayDifference;
			xOffset = (int) (eventWidth * (hour + minute / 60.0));
			xMultiplier = (24 - startEventCalendar.get(Calendar.HOUR_OF_DAY) +
					(60 - startEventCalendar.get(Calendar.MINUTE)) / 60.0);
			currentEventWidth *= xMultiplier;
			JTextField currentEvent = createEventBlock(EMPTY_STRING, xOffset, yOffset, (int) currentEventWidth, (int) eventHeight, currentColor);

			tblCalendar.add(currentEvent);
			startEventCalendar.set(Calendar.DAY_OF_YEAR, startEventCalendar.get(Calendar.DAY_OF_YEAR) + 1);
			startEventCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startEventCalendar.set(Calendar.MINUTE, 0);
			startEventCalendar.set(Calendar.SECOND, 0);
		}
		
		dayDifference = endEventCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR);

		hour = startEventCalendar.get(Calendar.HOUR_OF_DAY);
		minute = startEventCalendar.get(Calendar.MINUTE);
		yOffset = (int) eventHeight * dayDifference;
		xOffset = (int) (eventWidth * (hour + minute / 60.0));

		xMultiplier = (endEventCalendar.get(Calendar.HOUR_OF_DAY) - startEventCalendar.get(Calendar.HOUR_OF_DAY) +
				(endEventCalendar.get(Calendar.MINUTE) - startEventCalendar.get(Calendar.MINUTE)) / 60.0);
		eventWidth *= xMultiplier;
		JTextField currentEvent = createEventBlock(EMPTY_STRING, xOffset, yOffset, (int) eventWidth, (int) eventHeight, currentColor);
		JTextField currentEventName = createEventBlock(specificEvent.getName(), xOffset, yOffset, (int) eventWidth, (int) eventHeight, transperantColor);
		tblCalendar.add(currentEvent);
		tblCalendar.add(currentEventName);
		tblCalendar.setComponentZOrder(currentEventName, 1);

	}
	
	private JTextField createEventBlock(String name, int xOffset, int yOffset, int eventWidth, int eventHeight, Color color) {
		JTextField currentEvent = new JTextField();
		double ratio = 8.42;	// the ratio of the name that would fits in one block
	
		System.out.println(name + name.length());
		if (name.length() != 0){
			if (eventWidth/name.length() < ratio) {
				System.out.println((int)(eventWidth/ratio));
				currentEvent.setText(name.substring(0, ((int)(eventWidth/ratio)-2)) + "...");
			} else {
				currentEvent.setText(name);
			}
		}
		
		currentEvent.setBounds(xOffset, yOffset, (int) eventWidth, (int) eventHeight);
		if (color != null) {
			currentEvent.setBackground(color);
		}
		currentEvent.setHorizontalAlignment(JTextField.CENTER);
		currentEvent.setBorder(null);
		currentEvent.setEditable(false);
		currentEvent.setForeground(Color.BLACK);
		return currentEvent;
	}
	
	
	private void setBoundsCalendarComponents() {
		calendarPanel.setBounds(0, 482, 1467, 279);
		tblCalendar.setSize(100, 100);
		stblCalendar.setBounds(10, 11, 1447, 260);
	}
	
	private void setCalendarHeaders() {
		SimpleDateFormat format = getHeaderTableFormat();
		String formatted = EMPTY_STRING;
		Calendar calendarClone = (Calendar) calendarInstance.clone();
		
		for (int i=0; i < 24; i++) {
			formatted = format.format(calendarClone.getTime());
			calendarClone.set(Calendar.HOUR_OF_DAY, i);
			calendarClone.set(Calendar.MINUTE, 0);
			calendarClone.set(Calendar.SECOND, 0);
			
			formatted = format.format(calendarClone.getTime());
						
        	mtblCalendar.addColumn(formatted);
        }
		JTableHeader header = tblCalendar.getTableHeader();
		header.setBackground(Color.WHITE);
		header.setForeground(fontColor);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderColor));
	}
	
	private SimpleDateFormat getHeaderTableFormat() {
		SimpleDateFormat format = new SimpleDateFormat("H:mm");
		return format;
	}
	
	private SimpleDateFormat getHeaderFormat() {
		SimpleDateFormat format = new SimpleDateFormat("MMMM / yyyy");
		return format;
	}
	
	private void setCalendarRows() {
		mtblCalendar.setRowCount(DISPLAYED_DAYS_NUM);
		rowHeaderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowHeaderTable.setRowSelectionAllowed(true);
		rowHeaderTable.setColumnSelectionAllowed(true);
		stblCalendar.setRowHeaderView(rowHeaderTable);
	}
	
	private void setSingleCellSelection() {
        tblCalendar.setColumnSelectionAllowed(false);
        tblCalendar.setRowSelectionAllowed(false);
        tblCalendar.setRowHeight((stblCalendar.getHeight() - 20) / DISPLAYED_DAYS_NUM);
	}
}
