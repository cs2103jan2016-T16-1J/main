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


public class MainWindow {

	private JFrame frame;
	private int WINDOW_X = 100;
	private int WINDOW_Y = 100;
	private int WINDOW_WIDTH = 650;
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
		frame = new JFrame();
		frame.setBounds(WINDOW_X, WINDOW_Y, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(118, 0, 656, 561);
		frame.getContentPane().add(panel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 229, 538);
		panel.add(scrollPane);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(0, 541, 669, 20);
		panel.add(textField);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("New toggle button");
		tglbtnNewToggleButton.setBounds(0, 23, 121, 23);
		frame.getContentPane().add(tglbtnNewToggleButton);
		
		JToggleButton tglbtnNewToggleButton_1 = new JToggleButton("New toggle button");
		tglbtnNewToggleButton_1.setBounds(0, 0, 121, 23);
		frame.getContentPane().add(tglbtnNewToggleButton_1);
		
		JToggleButton tglbtnNewToggleButton_2 = new JToggleButton("New toggle button");
		tglbtnNewToggleButton_2.setBounds(0, 53, 121, 23);
		frame.getContentPane().add(tglbtnNewToggleButton_2);
	}
}
