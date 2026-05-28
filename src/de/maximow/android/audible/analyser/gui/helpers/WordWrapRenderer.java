package de.maximow.android.audible.analyser.gui.helpers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class WordWrapRenderer extends JTextArea implements TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WordWrapRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		setText(value != null ? value.toString() : "");
		setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);

		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}

		int preferredHeight = getPreferredSize().height;
		if (table.getRowHeight(row) != preferredHeight && preferredHeight > 0) {
			table.setRowHeight(row, preferredHeight);
		}

		return this;
	}
}
