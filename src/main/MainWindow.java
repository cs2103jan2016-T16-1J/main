package main;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;

import controller.Controller;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import java.awt.Container;

import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import command.*;


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
	private static JTextArea actionsTextArea;
	private Controller mainController;
	private State currentState;
	private Calendar calendarInstance;
	
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
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {

		/*_________ Testing ADD ______________*/
		//Creating object manually
		Controller controller = new Controller();
		State completeState = new State();
		Event testNewEvent = new Event();
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
		
		storage.Storage.createFile();
		
		testNewEvent.setName("TEST EVENT NAME");
		testNewEvent.setDescription("This is a test event created in main");
		testNewEvent.setLocation("Supahotfire's house");
		testNewEvent.setStatus(Event.Status.COMPLETE);
		testNewEvent.setStartTime(aTime);
		testNewEvent.setEndTime(aTime);
		Command adding = new Add(testNewEvent);
		//Add twice to test delete once
		completeState = adding.execute(completeState);
		completeState = adding.execute(completeState);
		
		Command deleting = new Delete(testNewEvent);
		completeState = deleting.execute(completeState);
		
		for(Event e: completeState.displayedEvents){
			result = result + e.printEvent();
			System.out.println(result);
		}
	
		finalResult = result;
		
		/*_________ Testing ADD ______________*/
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					
					//for Testing Purposes
					//actionsTextArea.append(finalResult);

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
		initializeController();
		
		intializeState();
		
		initializeMainWindow();
		
		initializeTabButtons();
		
		initializeMainTab();
		
		initializeDetailScrollbar();
		
		initializeInputField();
		
		initializeOutputField();
		
		initializeCalendar();
	}
	
	private void initializeController() {
		mainController = new Controller();
	}
	
	private void intializeState() {
		currentState = new State();
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
		    	
		    	//calling controller
		    	currentState = mainController.executeCommand(inputString);
		    	renderCalendar();
		    }
		};
		return action;
	}
	
	private void renderCalendar() {
		for (Event event : currentState.displayedEvents){
	    	actionsTextArea.append(event.printEvent());
	    	String category = event.getCategory();
	    	if (category == "DEADLINE") {
	    		//Add specific category action
	    		addDeadlineToTimetable(event);
	    	}
	    	
    	}
        System.out.println("");
	}
	
	private void initializeOutputField() {
		actionsTextArea = new JTextArea("BRUH");
		actionsTextArea.setBounds(275, 531, 771, 188);
		actionsTextArea.setEditable(false);
		mainTab.add(actionsTextArea);
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
	
	private void addDeadlineToTimetable(Event deadline) {
		double eventHeight = getDeadlineHeight();
		double eventWidth = getDeadlineWidth();
		
		Calendar startCalendar = (Calendar) calendarInstance.clone();
		Date startDate = startCalendar.getTime();
		
		Calendar endCalendar = (Calendar) calendarInstance.clone();
		endCalendar.set(Calendar.DAY_OF_MONTH, calendarInstance.get(Calendar.DAY_OF_MONTH) + DISPLAYED_DAYS_NUM);
		endCalendar.set(Calendar.HOUR_OF_DAY, 0);
		endCalendar.set(Calendar.MINUTE, 0);
		Date endDate = endCalendar.getTime();
		
		Date deadlineDate = deadline.getEndTime();
		Calendar deadlineCalendar = (Calendar) calendarInstance.clone();
		deadlineCalendar.setTime(deadlineDate);

		if (deadlineDate.after(startDate) && deadlineDate.before(endDate)) {
			JTextField currentEvent = new JTextField(deadline.getName());
			int dayDifference = deadlineCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR);
			int hour = deadlineCalendar.get(Calendar.HOUR_OF_DAY);
			int xOffset = (int) eventWidth * dayDifference;
			int yOffset = (int) eventHeight * hour;
 			currentEvent.setBounds(xOffset, yOffset, (int) eventWidth, (int) eventHeight);
			currentEvent.setColumns(10);
			currentEvent.setBackground(Color.GREEN);
			tblCalendar.add(currentEvent);
		}
	}
	
	private double getDeadlineHeight() {
		double height = tblCalendar.getHeight() / (double) 24;
		return height;
	}
	
	private double getDeadlineWidth() {
		double width = tblCalendar.getWidth() / (double) DISPLAYED_DAYS_NUM;
		return width;
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
		SimpleDateFormat format = getHeaderFormat();
		String formatted = format.format(calendarInstance.getTime());
		Calendar calendarClone = (Calendar) calendarInstance.clone();
		for (int i=0; i < DISPLAYED_DAYS_NUM; i++) {
			formatted = format.format(calendarClone.getTime());
			calendarClone.set(Calendar.DAY_OF_MONTH, calendarClone.get(Calendar.DAY_OF_MONTH) + 1);
        	mtblCalendar.addColumn(formatted);
        }
	}
	
	private SimpleDateFormat getHeaderFormat() {
		SimpleDateFormat format = new SimpleDateFormat("E (d)");
		return format;
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
