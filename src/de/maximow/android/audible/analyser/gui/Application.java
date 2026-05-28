package de.maximow.android.audible.analyser.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import de.maximow.android.audible.analyser.gui.helpers.WordWrapRenderer;
import de.maximow.android.audible.analyser.logs.LogParser;
import de.maximow.android.audible.analyser.logs.StandardLog;

public class Application {

	private JFrame frmAudibleAnalyser;
	private JTable logTable;
	private JFileChooser fileChooser = new JFileChooser();
	private JTextField filterField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frmAudibleAnalyser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Application() {
		initialize();
	}

	private void initialize() {
		frmAudibleAnalyser = new JFrame();
		frmAudibleAnalyser.setTitle("Audible Analyser");
		frmAudibleAnalyser.setBounds(100, 100, 900, 600);
		frmAudibleAnalyser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fileChooser.setFileFilter(new FileNameExtensionFilter(null, "log", "log0"));
		fileChooser.setMultiSelectionEnabled(true);

		JMenuBar menuBar = new JMenuBar();
		frmAudibleAnalyser.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Save");
		mnNewMenu.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Load");
		mnNewMenu.add(mntmNewMenuItem_3);

		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Export");
		mnNewMenu.add(mntmNewMenuItem_4);

		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Parse Logs");
		mntmNewMenuItem_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (fileChooser.showOpenDialog(mntmNewMenuItem_1) == JFileChooser.APPROVE_OPTION) {
					DefaultTableModel model = (DefaultTableModel) logTable.getModel();
					model.setRowCount(0);

					List<StandardLog> logs = LogParser.parseStandardLogs(fileChooser.getSelectedFiles()[0]);
					for (int i = 0; i < logs.size(); i++) {
						StandardLog log = logs.get(i);
						model.addRow(new Object[] { i + 1, log.getDate(), log.getProcess(), log.getType(), log.getWorker(), log.getDescription() });
					}
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem = new JMenuItem("Parse Databases");
		mnNewMenu.add(mntmNewMenuItem);
		frmAudibleAnalyser.getContentPane().setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		frmAudibleAnalyser.getContentPane().add(scrollPane, BorderLayout.CENTER);

		logTable = new JTable();
		scrollPane.setViewportView(logTable);

		DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {},
				new String[] { "#", "Date", "Process", "Type", "Worker", "Description" }) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 0) {
					return Integer.class;
				}
				return super.getColumnClass(columnIndex);
			}
		};
		logTable.setModel(tableModel);
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
		logTable.setRowSorter(sorter);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		centerRenderer.setVerticalAlignment(SwingConstants.TOP);

		TableColumnModel columnModel = logTable.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(25);
		columnModel.getColumn(0).setCellRenderer(centerRenderer);
		columnModel.getColumn(1).setPreferredWidth(300);
		columnModel.getColumn(1).setCellRenderer(centerRenderer);
		columnModel.getColumn(2).setPreferredWidth(50);
		columnModel.getColumn(2).setCellRenderer(centerRenderer);
		columnModel.getColumn(3).setPreferredWidth(25);
		columnModel.getColumn(3).setCellRenderer(centerRenderer);
		columnModel.getColumn(4).setPreferredWidth(50);
		columnModel.getColumn(4).setCellRenderer(centerRenderer);
		columnModel.getColumn(5).setPreferredWidth(400);
		columnModel.getColumn(5).setCellRenderer(new WordWrapRenderer());

		JPanel panel = new JPanel();
		frmAudibleAnalyser.getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Log Filter");
		panel.add(lblNewLabel);

		filterField = new JTextField();
		panel.add(filterField);
		filterField.setToolTipText("Text Filter");
		filterField.setColumns(10);

		filterField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = filterField.getText();
				if (text.trim().length() == 0) {
					sorter.setRowFilter(null); // Show everything
				} else {
					String userInput = filterField.getText();
					String safeRegex = "(?i)" + Pattern.quote(userInput);
					sorter.setRowFilter(RowFilter.regexFilter(safeRegex));
				}
			}
		});
	}
}
