package com.github.syuchan1005.gitprefix.ui;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import javax.swing.Icon;
import javax.swing.table.DefaultTableModel;

public class EmojiTableModel extends DefaultTableModel {
	private static String[] columnNames = {"Icon", "Name"};
	private static Class[] columnClasses = {Icon.class, String.class};

	public EmojiTableModel(Object[][] data) {
		super(data, columnNames);
	}

	@Override
	public Object getValueAt(int row, int column) {
		Object valueAt = super.getValueAt(row, column);
		return valueAt instanceof EmojiUtil.EmojiData ? ((EmojiUtil.EmojiData) valueAt).getIcon() : valueAt;
	}

	public Object getTrueValueAt(int row, int column) {
		return super.getValueAt(row, column);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
