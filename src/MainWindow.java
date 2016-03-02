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
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JTree;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;


public class MainWindow {

	private JFrame frame;
	private JPanel mainTab;
	private int WINDOW_X = 100;
	private int WINDOW_Y = 100;
	private int WINDOW_WIDTH = 800;
	private int WINDOW_HEIGHT = 600;
	private JTextField textField;


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
	}
	
	private void initializeMainWindow() {
		frame = new JFrame();
		frame.setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	}
	
	private void initializeTabButtons() {
		JToggleButton tglbtnNewToggleButton = new JToggleButton("New toggle button");
		tglbtnNewToggleButton.setBounds(0, 68, 121, 23);
		frame.getContentPane().add(tglbtnNewToggleButton);
		
		JToggleButton tglbtnNewToggleButton_1 = new JToggleButton("New toggle button");
		tglbtnNewToggleButton_1.setBounds(0, 0, 121, 23);
		frame.getContentPane().add(tglbtnNewToggleButton_1);
		
		JToggleButton tglbtnNewToggleButton_2 = new JToggleButton("New toggle button");
		tglbtnNewToggleButton_2.setBounds(0, 34, 121, 23);
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
		scrollPane.setBounds(0, 0, 229, 538);
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
		textArea.setBounds(241, 342, 405, 196);
		textArea.setEditable(false);
		mainTab.add(textArea);
	}
}
