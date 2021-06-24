package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MonthPanel extends JPanel {

	private int year;
	private int month;

	private DayPanel[] days;

	private String[] strDay = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

	private int lineCount = 5;
	private int firstDay;
	private int endOfMonth;

	private JLabel[] lblWeek;

	private MyCalendar myCalendar;
	private HashMap<Integer, String> mapDiaryData;

	public MonthPanel(int year, int month, HashMap mapDiaryData, MyCalendar myCalendar) {
		this.year = year;
		this.month = month - 1;
		this.mapDiaryData = mapDiaryData;
		this.myCalendar = myCalendar;
		init();
		setDisplay();
		addListeners();
	}
	// ���� ������ �߰��ؼ� �ذ��غ���
	

	private void init() {

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);

		// �ش� ���� ���� ���� : Sun->1 ...Sat->7
		firstDay = cal.get(Calendar.DAY_OF_WEEK);
		endOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		days = new DayPanel[endOfMonth];
		lineCount = getLine();

		// @��ö�� North�� ���� �г� �ֱ�
		lblWeek = new JLabel[strDay.length];
		for (int idx = 0; idx < lblWeek.length; idx++) {
			lblWeek[idx] = new JLabel(strDay[idx], JLabel.CENTER);
			lblWeek[idx].setPreferredSize(new Dimension(50, 30));
			lblWeek[idx].setOpaque(true);
			lblWeek[idx].setForeground(Color.WHITE);
			lblWeek[idx].setFont(new Font("MapoGoldenPier", Font.BOLD, 15));
			lblWeek[idx].setBorder(new LineBorder(Color.WHITE, 2));
			if (strDay[idx].equals("Sun")) {
				// �Ͽ��� ���̺��̸� ������ ���������� ����
				lblWeek[idx].setBackground(new Color(241, 95, 95));
			} else if (strDay[idx].equals("Sat")) {
				// ����� ���̺��̸� ������ �Ķ������� ����
				lblWeek[idx].setBackground(new Color(36, 120, 255));
			} else {
				// ������ ���� ���̺��� ������ ���������� ����
				lblWeek[idx].setBackground(new Color(76, 76, 76));
			}
		}

	}

	private void setDisplay() {
		setLayout(new BorderLayout());
		JPanel pnlCal = new JPanel(new GridLayout(lineCount, 7));
		// @��ö�� Ķ���� �׵θ��� ������ �߰�
		pnlCal.setBorder(new LineBorder(Color.black, 1));
		Utils.setPnlWhite(pnlCal);
		// @��ö��
		JPanel pnlWeek = new JPanel(new GridLayout(1, 7));
		for (int idx = 0; idx < lblWeek.length; idx++) {
			pnlWeek.add(lblWeek[idx]);
		}
		add(pnlWeek, BorderLayout.NORTH);

		int total = lineCount * 7;

		// ���� ��ĭ
		for (int i = 1; i < firstDay; i++, total--) {
			pnlCal.add(new JLabel());
		}

		// ��¥ ä���
		for (int i = 1; i <= endOfMonth; i++, total--) {
			days[i - 1] = new DayPanel(year, month, i, mapDiaryData, myCalendar);
			pnlCal.add(days[i - 1]);
		}

		// ������ ��
		for (int i = 0; i < total; i++) {
			pnlCal.add(new JLabel());
		}

		add(pnlCal, BorderLayout.CENTER);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		// reset();
	}

	public int getMonth() {
		return month;

	}

	public void setMonth(int month) {
		this.month = month - 1;
		// reset();
	}

	public void reset() {
		removeAll();
		init();
		setDisplay();
		updateUI();
	}

	public String getToday(int num) {
		return strDay[num - 1];
	}
	/*
	 * 
	 * �޷��� ���μ� ���� ���� �ϼ� ���� �� 31 6 �� 30 6 �� 31 6
	 * 
	 * 
	 * 
	 * 
	 */

	private int getLine() {
		int line = 5;

		if (firstDay == Calendar.FRIDAY) {
			if (endOfMonth == 31) {
				line = 6;
			}
		} else if (firstDay == Calendar.SATURDAY) {
			if (endOfMonth >= 30) {
				line = 6;
			}
		}

		return line;
	}

	private void addListeners() {

	}

	public DayPanel[] getDays(){
		return days;
	}
	public void daysVisible(boolean flag){
		for(DayPanel day : days){
			day.lblVisible(flag);
		}
	}
}
