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


public class MainWindow {

	private JFrame frame;
	private JPanel mainTab;
	private int WINDOW_X = 100;
	private int WINDOW_Y = 100;
	private int WINDOW_WIDTH = 800;
	private int WINDOW_HEIGHT = 600;
	private JTextField textField;
	private JTable calendarTable;
	private JPanel calendarPanel;
	
	static JLabel lblMonth;
	static JButton btnPrev, btnNext;
	static JTable tblCalendar;
	static JFrame frmMain;
	static Container pane;
	static DefaultTableModel mtblCalendar; //Table model
	static JScrollPane stblCalendar; //The scrollpane
	static JPanel pnlCalendar; //The panel
	static int realDay, realMonth, realYear, currentMonth, currentYear;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
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
		mainTab.setBounds(118, 0, 656, 561);
		frame.getContentPane().add(mainTab);
	}
	
	private void initializeDetailScrollbar() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 229, 530);
		mainTab.add(scrollPane);
	}
	
	private void initializeInputField() {
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(0, 541, 669, 20);
		mainTab.add(textField);
	}
	
	private void initializeOutputField() {
		JTextArea textArea = new JTextArea("BRUH");
		textArea.setBounds(239, 342, 417, 188);
		textArea.setEditable(false);
		mainTab.add(textArea);
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
		calendarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
		mainTab.add(calendarPanel);
		calendarPanel.add(btnPrev);
		calendarPanel.add(lblMonth);
		calendarPanel.add(btnNext);
		calendarPanel.add(stblCalendar);
	}
	
	private void setBoundsCalendarComponents() {
		calendarPanel.setBounds(239, 0, 417, 331);
		lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 100, 25);
		btnPrev.setBounds(10, 25, 50, 25);
		btnNext.setBounds(260, 25, 50, 25);
		stblCalendar.setBounds(10, 50, 200, 200);
	}
	
	private void setCalendarHeaders() {
		String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
		for (int i=0; i < 7; i++) {
        	mtblCalendar.addColumn(headers[i]);
        }	
	}
	
	private void setCalendarRows() {
		mtblCalendar.setRowCount(20);
	}
	
	private void setSingleCellSelection() {
        tblCalendar.setColumnSelectionAllowed(true);
        tblCalendar.setRowSelectionAllowed(true);
        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCalendar.setRowHeight(38);
	}
	
}
