package main;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/*
 *	Use a JTable as a renderer for row numbers of a given main table.
 *  This table must be added to the row header of the scrollpane that
 *  contains the main table.
 */
public class RowNumberTable extends JTable
	implements ChangeListener, PropertyChangeListener, TableModelListener
{
	private JTable main;
	
	public RowNumberTable(JTable table, Calendar calendarInstance, int numberOfDays)
	{
		this.main = table;
		main.addPropertyChangeListener( this );
		main.getModel().addTableModelListener( this );

		setFocusable( false );
		setAutoCreateColumnsFromModel( false );
		setSelectionModel( main.getSelectionModel() );


		TableColumn column = new TableColumn();
		column.setHeaderValue("");
		addColumn( column );
		column.setCellRenderer(new RowNumberRenderer(calendarInstance, numberOfDays));

		getColumnModel().getColumn(0).setPreferredWidth(50);
		setPreferredScrollableViewportSize(getPreferredSize());
	}
	
	
	@Override
	public void addNotify()
	{
		super.addNotify();

		Component c = getParent();

		//  Keep scrolling of the row table in sync with the main table.

		if (c instanceof JViewport)
		{
			JViewport viewport = (JViewport)c;
			viewport.addChangeListener( this );
		}
	}

	/*
	 *  Delegate method to main table
	 */
	@Override
	public int getRowCount()
	{
		return main.getRowCount();
	}

	@Override
	public int getRowHeight(int row)
	{
		int rowHeight = main.getRowHeight(row);

		if (rowHeight != super.getRowHeight(row))
		{
			super.setRowHeight(row, rowHeight);
		}

		return rowHeight;
	}

	/*
	 *  No model is being used for this table so just use the row number
	 *  as the value of the cell.
	 */
	@Override
	public Object getValueAt(int row, int column)
	{
		return Integer.toString(row + 1);
	}

	/*
	 *  Don't edit data in the main TableModel by mistake
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}

	/*
	 *  Do nothing since the table ignores the model
	 */
	@Override
	public void setValueAt(Object value, int row, int column) {}
//
//  Implement the ChangeListener
//
	public void stateChanged(ChangeEvent e)
	{
		//  Keep the scrolling of the row table in sync with main table

		JViewport viewport = (JViewport) e.getSource();
		JScrollPane scrollPane = (JScrollPane)viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}
//
//  Implement the PropertyChangeListener
//
	public void propertyChange(PropertyChangeEvent e)
	{
		//  Keep the row table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName()))
		{
			setSelectionModel( main.getSelectionModel() );
		}

		if ("rowHeight".equals(e.getPropertyName()))
		{
			repaint();
		}

		if ("model".equals(e.getPropertyName()))
		{
			main.getModel().addTableModelListener( this );
			revalidate();
		}
	}

//
//  Implement the TableModelListener
//
	@Override
	public void tableChanged(TableModelEvent e)
	{
		revalidate();
	}

	/*
	 *  Attempt to mimic the table header renderer
	 */
	private static class RowNumberRenderer extends DefaultTableCellRenderer
	{
		private Calendar calendarInstance;
		private int numberOfDays;
		public RowNumberRenderer(Calendar calendarInstance, int numberOfDays)
		{
			this.calendarInstance = calendarInstance;
			this.numberOfDays = numberOfDays;
			setHorizontalAlignment(JLabel.CENTER);
		}
		
		private SimpleDateFormat getHeaderTableFormat() {
			SimpleDateFormat format = new SimpleDateFormat("E (d)");
			return format;
		}
		
		private SimpleDateFormat getHeaderFormat() {
			SimpleDateFormat format = new SimpleDateFormat("MMMM / yyyy");
			return format;
		}

		public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if (table != null)
			{
				JTableHeader header = table.getTableHeader();

				if (header != null)
				{
					Color fontColor = new Color(103, 106, 108);
					setForeground(fontColor);
					setBackground(Color.WHITE);
					setFont(header.getFont());
				}
			}

			if (isSelected)
			{
				setFont( getFont().deriveFont(Font.BOLD) );
			}
			
			if (value == null) {
				setText("");
			} else {
				SimpleDateFormat format = getHeaderTableFormat();
				String formatted = format.format(calendarInstance.getTime());
				Calendar calendarClone = (Calendar) calendarInstance.clone();
			
				calendarClone.set(Calendar.DAY_OF_MONTH, calendarClone.get(Calendar.DAY_OF_MONTH) + row);
				formatted = format.format(calendarClone.getTime());
	        	setText(formatted);
		        
			}
			Color borderColor = new Color(231, 234, 236);
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderColor));

			return this;
		}
	}
}