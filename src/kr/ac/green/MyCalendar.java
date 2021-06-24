package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MyCalendar extends JFrame implements ActionListener {
	private int date;
	private String userName;

	private JLabel lblYear;
	private JLabel lblMonth;

	private JButton btnMoveYear;
	private JButton btnPreMonth;
	private JButton btnNextMonth;
	private JButton btnToday;
	private JButton btnDeleteAll;
	private JButton btnPlusPlan;

	private JPanel pnlAll;

	private MonthPanel calendar;
	private HashMap<Integer, Vector<ToDoList>> mapDiaryData;
	private Vector<ToDoList> vecToDo;

	public MyCalendar(HashMap<Integer, Vector<ToDoList>> mapDiaryData, String userName) {
		this.mapDiaryData = mapDiaryData;
		this.userName = userName;
		init();
		setDisplay();
		addListeners();
		showFrame();
	}

	public MyCalendar(MonthPanel calendar, HashMap<Integer, Vector<ToDoList>> mapDiaryData) {
		this.mapDiaryData = mapDiaryData;
		this.calendar = calendar;
		init();
		setDisplay();
		addListeners();
		showFrame();
	}

	public String getUserName() {
		return userName;
	}

	public void init() {
		btnPreMonth = new JButton(Utils.left);
		btnNextMonth = new JButton(Utils.right);
		Utils.setBtnWhite(btnPreMonth);
		Utils.setBtn(btnPreMonth);
		Utils.setBtnWhite(btnNextMonth);
		Utils.setBtn(btnNextMonth);

		// 김정규 추가
		btnMoveYear = new JButton("Year");
		Utils.setBtnGrey(btnMoveYear);

		btnToday = new JButton("Today");
		Utils.setBtnGrey(btnToday);

		Font font = new Font(Font.DIALOG, Font.BOLD, 30);
		if (calendar == null) {
			Calendar c = Calendar.getInstance();

			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			lblYear = new JLabel(String.valueOf(year));
			lblYear.setFont(font);
			lblMonth = new JLabel(String.valueOf(month));
			lblMonth.setFont(font);

			calendar = new MonthPanel(year, month, mapDiaryData, this);
		} else {
			lblYear = new JLabel(String.valueOf(calendar.getYear()));
			lblYear.setFont(font);
			lblMonth = new JLabel(String.valueOf(calendar.getMonth() + 1));
			lblMonth.setFont(font);

			calendar = new MonthPanel(calendar.getYear(), calendar.getMonth() + 1, mapDiaryData, this);
		}

		// @ 박철진 추가
		btnPlusPlan = new JButton("일정추가");
		// Button 색 변경
		Utils.setBtnGrey(btnPlusPlan);

		btnDeleteAll = new JButton("모든 일정 삭제");
		Utils.setBtnGrey(btnDeleteAll);
	}

	public void setDisplay() {
		Utils.setIcon(this);

		JPanel pnlNorth = new JPanel();
		pnlNorth.add(btnMoveYear);
		pnlNorth.add(btnToday);
		pnlNorth.add(btnPreMonth);
		// pnlNorth.add(btnPreMonth);
		pnlNorth.add(lblYear);
		pnlNorth.add(new JLabel("."));
		pnlNorth.add(lblMonth);
		// pnlNorth.add(btnNextMonth);
		pnlNorth.add(btnNextMonth);
		pnlNorth.add(btnPlusPlan);
		pnlNorth.add(btnDeleteAll);

		// @박철진 패널 배경색 흰색으로 변경
		Utils.setPnlWhite(pnlNorth);
		Utils.setPnlWhite(calendar);

		// 김정규 DDAy추가
		JPanel pnlEast = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pnlGrid = new JPanel(new GridLayout(0, 1));
		pnlEast.setBackground(Color.WHITE);
		pnlGrid.setBackground(Color.WHITE);
		TitledBorder tBorder = new TitledBorder(new LineBorder(Color.GRAY, 4), "D-Day");
		tBorder.setTitleFont(new Font(Font.DIALOG, Font.BOLD, 30));
		pnlEast.setBorder(tBorder);

		Set<Integer> sets = mapDiaryData.keySet();
		TreeSet<String> DdaySet = new TreeSet<String>();
		HashMap<Integer, Vector<String>> tempMap = new HashMap<Integer, Vector<String>>();

		for (Integer num : sets) {
			Vector<ToDoList> temp = mapDiaryData.get(num);
			Vector<String> testVector = new Vector<String>();
			for (ToDoList list : temp) {
				if (list.isDday()) {
					int Dday = Dday(list.getYear(), list.getMonth(), list.getDay());
					if (Dday < 0) {
						testVector.add("D" + Dday + "  " + list.getShortTitle());
						tempMap.put((-Dday), testVector);
					} else if (Dday == 0) {
						DdaySet.add("D-Day  " + list.getShortTitle());
					} else {
					}
				}
			}
		}
		// 김정규 추가추가
		for (String str : DdaySet) {
			JLabel lbl = new JLabel(str);
			lbl.setFont(new Font(Font.DIALOG, Font.BOLD, 17));
			lbl.setForeground(Color.GREEN);
			lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
			pnlGrid.add(lbl);
		}

		Set<Integer> testInt = tempMap.keySet();
		TreeSet<Integer> testTree = new TreeSet();
		for (int num : testInt) {
			testTree.add(num);
		}

		for (int num : testTree) {
			Vector<String> vec = tempMap.get(num);
			for (String str : vec) {
				JLabel lbl = new JLabel(str);
				lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
				lbl.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
				pnlGrid.add(lbl);
			}

		}

		// if(num < 13 || num == 0){
		// pnlEast.add(pnlGrid);
		// pnlAll.add(pnlEast, BorderLayout.EAST);
		// } else {
		// JScrollPane scroll = new JScrollPane(pnlGrid);
		// scroll.setPreferredSize(new Dimension(278, 460));
		// scroll.setBorder(null);
		// pnlEast.add(scroll);
		// pnlAll.add(pnlEast, BorderLayout.EAST);
		// }

		// scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// JScrollPane sc = new JScrollPane(pnlGrid,
		// ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
		// ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// sc.setPreferredSize(new Dimension(278, 460));
		// sc.setBorder(null);
		pnlEast.add(pnlGrid);
		JScrollPane scroll = new JScrollPane(pnlEast);
		scroll.setPreferredSize(new Dimension(300, 300));
		add(pnlNorth, BorderLayout.NORTH);
		pnlAll = new JPanel(new BorderLayout());
		pnlAll.add(pnlNorth, BorderLayout.NORTH);
		pnlAll.add(calendar, BorderLayout.CENTER);
		pnlAll.add(scroll, BorderLayout.EAST);
		add(pnlAll, BorderLayout.CENTER);

		btnPlusPlan.addActionListener(this);
		btnToday.addActionListener(this);
		btnPreMonth.addActionListener(this);
		btnNextMonth.addActionListener(this);
		btnMoveYear.addActionListener(this);
		btnDeleteAll.addActionListener(this);

	}

	public void showFrame() {
		setTitle("To-Do Calendar");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		// 김정구 추가

		if (src == btnPreMonth || src == btnNextMonth) {
			int year = Integer.parseInt(lblYear.getText());
			int month = Integer.parseInt(lblMonth.getText());
			if (src == btnPreMonth) {
				if (year == 2020 && month == 1) {
					JOptionPane.showConfirmDialog(this, "2020년 보다 전으로 돌아 갈수 없습니다.", "경고", JOptionPane.YES_OPTION,
							JOptionPane.ERROR_MESSAGE);
				} else {
					month--;
					if (month < 1) {
						month = 12;
						year--;
					}
				}
			} else if (src == btnNextMonth) {
				if (year == 2025 && month == 12) {
					JOptionPane.showConfirmDialog(this, "2026년은 안옵니다.", "경고", JOptionPane.YES_OPTION,
							JOptionPane.ERROR_MESSAGE);
				} else {
					month++;
					if (month > 12) {
						month = 1;
						year++;
					}
				}
			}
			changeCalendar(year, month);
		}

		if (src == btnPlusPlan) {
			new PlanFrame(this);
			setEnabled(false);
		}

		if (src == btnMoveYear) {
			this.setEnabled(false);
			new Year(this, calendar.getYear());
		}

		if (src == btnToday) {
			Calendar c = Calendar.getInstance();
			int thisYear = c.get(Calendar.YEAR);
			int thisMonth = c.get(Calendar.MONTH) + 1;

			changeCalendar(thisYear, thisMonth);
		}

		if (src == btnDeleteAll) {
			try {
				if (mapDiaryData.size() > 0) {
					int result = JOptionPane.showConfirmDialog(this, "Are you sure?", "question",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						mapDiaryData.clear();
						reSet();
					}
				} else {
					JOptionPane.showMessageDialog(this, "Check your input");
				}
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(this, "Check your input");
			}
		}
	}

	public void changeCalendar(int year, int month) {
		calendar.setYear(year);
		calendar.setMonth(month);
		calendar.reset();
		lblYear.setText(String.valueOf(year));
		lblMonth.setText(String.valueOf(month));
	}

	public HashMap<Integer, Vector<ToDoList>> getMap() {
		return mapDiaryData;
	}

	public MonthPanel getMyCalrendar() {
		return calendar;
	}

	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				int result = JOptionPane.showConfirmDialog(MyCalendar.this, "프로그램을 종료 하시겠습니까?", "question",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					saveDate(mapDiaryData);
					System.exit(0);
				} else if (result == JOptionPane.NO_OPTION) {}
			}
		});
		// btnPlusPlan.addActionListener(this);
		// btnToday.addActionListener(this);
		// btnPreMonth.addActionListener(this);
		// btnNextMonth.addActionListener(this);
		// btnMoveYear.addActionListener(this);
		// btnDeleteAll.addActionListener(this);
	}

	private void saveDate(HashMap<Integer, Vector<ToDoList>> map) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream(".\\" + getUserName() + "\\ToDoList.dat");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
			oos.flush();
			oos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Utils.closeAll(oos, fos);
		}
	}

	// public void move(int year, int month){
	// calendar.setYear(year);
	// calendar.setMonth(month);
	// calendar.reset();
	// lblYear.setText(String.valueOf(year));
	// lblMonth.setText(String.valueOf(month));
	// }

	public void reSet() {
		pnlAll.removeAll();
		init();
		setDisplay();
		pnlAll.updateUI();
	}

	public int Dday(int mYear, int mMonth, int mDay) {
		try {
			TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
			Calendar today = Calendar.getInstance(tz);
			Calendar dday = Calendar.getInstance(tz);
			dday.set(mYear, mMonth - 1, mDay);
			long day = dday.getTimeInMillis() / 86400000;
			long tday = today.getTimeInMillis() / 86400000;
			long count = tday - day;
			return (int) count;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public MonthPanel setMyCalrendar(MonthPanel calrendar) {
		this.calendar = calrendar;
		return calendar;
	}

}

// pnlEast.add(pnlGrid);
// JScrollPane scroll = new JScrollPane(pnlEast);
// scroll.setPreferredSize(new Dimension(300, 300));
// scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);