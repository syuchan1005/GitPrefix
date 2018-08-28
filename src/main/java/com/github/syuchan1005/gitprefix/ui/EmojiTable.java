package com.github.syuchan1005.gitprefix.ui;

import com.github.syuchan1005.gitprefix.EmojiUtil;
import com.github.syuchan1005.gitprefix.util.Debouncer;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.table.JBTable;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class EmojiTable extends JBTable {
	private Debouncer debouncer = new Debouncer();

	public EmojiTable(EmojiListEditor editor, Object[][] objects) {
		super(new EmojiTableModel(objects));
		EmojiTable emojiTable = this;

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean isShiftDown = (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != 0;
				if (!isShiftDown) return;
				int row = emojiTable.rowAtPoint(e.getPoint());
				int col = emojiTable.columnAtPoint(e.getPoint());
				Object value = ((EmojiTableModel) emojiTable.dataModel).getTrueValueAt(row, col);
				String data = null;
				if (value instanceof EmojiUtil.EmojiData) {
					data = ((EmojiUtil.EmojiData) value).getCode();
				} else if (value instanceof String) {
					data = ":" + value + ":";
				}
				if (data == null) return;
				StringSelection ss = new StringSelection(data);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
				JBLabel copiedLabel = editor.getCopiedLabel();
				copiedLabel.setVisible(true);
				Color foreground = copiedLabel.getForeground();
				debouncer.debounce(Void.class, () -> copiedLabel.setVisible(false), 1500, TimeUnit.MILLISECONDS);
			}
		});
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableRowSorter<? extends TableModel> sorter = new TableRowSorter<>(this.dataModel);
		this.setRowSorter(sorter);

		TableColumn iconColumn = this.getColumnModel().getColumn(0);
		iconColumn.setMinWidth(30);
		iconColumn.setMaxWidth(30);
		iconColumn.setResizable(false);
		TableColumn nameColumn = this.getColumnModel().getColumn(1);
		nameColumn.setResizable(false);
	}
}
