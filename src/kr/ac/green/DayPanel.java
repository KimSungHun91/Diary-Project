package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class DayPanel extends JPanel {
	private int year;
	private int month;
	private int day;
	private int dayOfWeek;
	private DayKind kind;
	private JLabel lbl;
	private TitledBorder tBorder;
	private HashMap<Integer, Vector<ToDoList>> mapDiaryData;
	private int date;

	private MyCalendar myCalendar;

	public DayPanel(int year, int month, int day, HashMap mapDiaryData, MyCalendar myCalendar) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.mapDiaryData = mapDiaryData;
		this.myCalendar = myCalendar;

		Calendar c = Calendar.getInstance();
		int today = c.get(Calendar.DAY_OF_MONTH);
		int thisMonth = c.get(Calendar.MONTH);
		int thisYear = c.get(Calendar.YEAR);

		c.set(year, month, day);
		dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		if (today == day && thisMonth == month && thisYear == year) {
			kind = DayKind.TODAY;
		} else {
			switch (dayOfWeek) {
			case Calendar.SUNDAY:
				kind = DayKind.SUN;
				break;
			case Calendar.SATURDAY:
				kind = DayKind.SAT;
				break;
			default:
				kind = DayKind.NORMAL;
				break;
			}
		}
		init();
		setDisplay();
		addListeners();

	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	private void init() {
		setBackground(Color.WHITE);

		tBorder = new TitledBorder(new LineBorder(Color.WHITE, 0), String.valueOf(day));
		// 여기서 색깔을 결정 하나 부다
		tBorder.setTitleColor(getDayFontColor());
		lbl = new JLabel("", JLabel.CENTER);
		lbl.setBorder(null);
		setBorder(tBorder);

		// String strYear = String.valueOf(year);
		// String strMonth = String.valueOf(month+1);
		// String strDay = String.valueOf(day);
		// String gogo = strYear+strMonth+strDay+".dat";
		// File dir = new File(".", gogo);
		//
		// if(dir.exists()){
		// lbl.setText("안녕하세요");
		// }

		// File dir = new File(".", "2021611.dat");
		// System.out.println(dir.exists());

		// if (testMap.containsKey(date)) {
		// String titles = "";
		// for (int i = 0; i < testMap.get(date).size(); i++) {
		// titles += testMap.get(date).get(i).getTitle() + "\n";
		// }
		// taTest.setText(titles);
		// }

		String strYear = String.valueOf(year);
		String strMonth = String.valueOf(month + 1);
		String strDay = String.valueOf(day);
		date = Integer.parseInt(strYear + strMonth + strDay);

		if (mapDiaryData.containsKey(date)) {
			Vector<ToDoList> list = mapDiaryData.get(date);
			String infoCount = String.valueOf(list.size());
			if (list.size() > 0) {
				lbl.setIcon(Utils.data);
				lbl.setText(infoCount);
				setBackground(Color.YELLOW);
			} else if (list.size() <= 0) {
				mapDiaryData.remove(date);
				lbl.setText("");
				lbl.getDisabledIcon();
				setBackground(Color.WHITE);
			}
		}

		//
		// if (testMap.containsKey(date)) {
		// Vector<ToDoList> list = testMap.get(date);
		// String infoCount = String.valueOf(list.size());
		// lbl.setText(infoCount);
		// lbl.setIcon(Utils.data);
		// setBackground(Color.YELLOW);
		// }

		// date = Integer.parseInt(strYear + strMonth + strDay);
		// list = testMap.get(date);
		// int size = list != null && list.size() < 5 ? 4 : 0;
		//
		// if (testMap.containsKey(date)) {
		// JPanel pnlMain = new JPanel(new GridLayout(size, 1));
		// pnlMain.setBackground(Color.WHITE);
		// if (list.size() < 5) {
		// for (ToDoList toDoList : list) {
		// JPanel pnl = new JPanel();
		// JLabel lbl = new JLabel("");
		// lbl.setText(toDoList.getTitle());
		// pnl.setBackground(Color.WHITE);
		// pnl.add(lbl);
		// pnlMain.add(lbl);
		// }
		// } else {
		// JPanel pnl = new JPanel();
		// JLabel lbl = new JLabel("...");
		// pnl.setBackground(Color.WHITE);
		// pnl.add(lbl);
		// pnlMain.add(pnl);
		// }
		// setLayout(new BorderLayout());
		// add(pnlMain, BorderLayout.NORTH);
		// }

	}

	private void setDisplay() {
		setLayout(new BorderLayout());
		add(lbl, BorderLayout.NORTH);
	}

	private void addListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) {
				setBorder(new TitledBorder(new LineBorder(Color.BLUE, 2), String.valueOf(day)));

			}

			@Override
			public void mouseExited(MouseEvent me) {
				setBorder(tBorder);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				try {
					// 마우스로 눌렀을 때 ToDoListDlg가 뜹니다
					// 생성될때 몇년몇월며칠인인지 알아야하니까
					// DayComponent를 파라미터로 생성됩니다.. 따라가보면
					if(myCalendar.isEnabled()){
						new ToDoListDlg(DayPanel.this);
						myCalendar.setEnabled(false);
					}


				} catch (NullPointerException me) {
				}
			}

		});

	}

	public Color getDayFontColor() {
		Color color;

		switch (kind) {
		case SAT:
			color = Color.BLUE;
			break;
		case SUN:
		case HOLY:
			color = Color.RED;
			break;
		case TODAY:
			color = Color.GREEN;
			break;
		case EVENT:
			color = Color.ORANGE;
			break;
		default:
			color = Color.BLACK;
		}
		return color;
	}

	public void setEventText(String text) {
		lbl.setText(text);
	}

	public String getEventText() {
		return lbl.getText();
	}

	public void setDayFont(Font font) {
		tBorder.setTitleFont(font);
	}

	public Font getDayFont() {
		return tBorder.getTitleFont();
	}

	public void setKind(DayKind kind) {
		this.kind = kind;
	}

	public DayKind getKind() {
		return kind;
	}

	public HashMap<Integer, Vector<ToDoList>> getMap() {
		return mapDiaryData;
	}

	public int getDate() {
		return date;
	}

	public void reset() {
		removeAll();
		init();
		setDisplay();
		updateUI();
		myCalendar.reSet();
	}

	public void testEnable() {
		myCalendar.setEnabled(true);
	}

	public void testReset() {
		myCalendar.reSet();
	}

	public void lblVisible(boolean flag) {
		lbl.setVisible(flag);
	}
}
