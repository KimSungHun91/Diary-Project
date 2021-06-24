package kr.ac.green;

// 옛날거
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class PlanPanel extends JPanel implements ActionListener {

	private JTextField tfTitle;
	private JTextArea taInput;
	private JLabel lblPlanDate;
	private JLabel lblTitle;
	private JLabel lblDday;
	private JComboBox cbYear;
	private JComboBox cbMonth;
	private JComboBox cbDay;

	private JRadioButton rbTrue;
	private JRadioButton rbFalse;

	private int todayYear;
	private int todayMonth;

	private Calendar todayCal = Calendar.getInstance();

	public static final Font font = new Font(Font.DIALOG, Font.BOLD, 14);

	public PlanPanel() {
		init();
		setDisplay();
		showFrame();
		addListener();
	}

	private void init() {
		// North에 제목
		lblTitle = new JLabel("제목");
		tfTitle = new JTextField(20); // 제목 입력

		// Center에 내용입력
		taInput = new JTextArea(10, 20);
		taInput.setLineWrap(true);

		// south에 일정
		lblPlanDate = new JLabel("일정");
		lblDday = new JLabel("D-day");
		// Font 설정
		lblTitle.setFont(font);
		lblPlanDate.setFont(font);
		lblDday.setFont(font);

		// @박철진 ComboBox
		// 수정------------------------------------------------------시작
		todayMonth = todayCal.get(Calendar.MONTH) + 1;// 0~11값을 돌려받기 때문에 +1
		todayYear = todayCal.get(Calendar.YEAR);
		// 년,월 기본 선택값은 현재 날짜

		cbYear = new JComboBox();
		for (int i = 2020; i < 2026; i++) {
			cbYear.addItem(i);
		}

		cbMonth = new JComboBox();
		for (int i = 0; i < 12; i++) {
			cbMonth.addItem(i + 1);
		}

		cbYear.setSelectedItem(todayYear);
		cbMonth.setSelectedItem(todayMonth);

		cbDay = new JComboBox();
		todayCal.set((int) cbYear.getSelectedItem(), (int) cbMonth.getSelectedItem() - 1, 1);
		int month = todayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < month; i++) {
			cbDay.addItem(i + 1);
		}

		
		Calendar c = Calendar.getInstance();
		cbYear.setSelectedItem(Integer.valueOf((int) cbYear.getSelectedItem()));
		cbMonth.setSelectedItem(Integer.valueOf((int) cbMonth.getSelectedItem()));
		cbDay.setSelectedItem(c.get(Calendar.DAY_OF_MONTH));

		// -----------------------------------------------------------------끝

		rbTrue = new JRadioButton("등록");
		rbFalse = new JRadioButton("미등록", true);
		rbTrue.setBackground(Color.WHITE);
		rbFalse.setBackground(Color.WHITE);
		ButtonGroup group = new ButtonGroup();
		group.add(rbTrue);
		group.add(rbFalse);

		// 년 월 일
	}

	// 콤보박스 월 마다 일 수 변경하기
	private void addListener() {
	      cbYear.addActionListener(this);
	      cbMonth.addActionListener(this);
	      cbDay.addActionListener(this);
	   }

	   @Override
	   public void actionPerformed(ActionEvent e) {
	      // actionPerformed에서 소스랑 콤보박스 연결 안하면 이벤트 있으면 자동으로 수행함

	      Object src = e.getSource();
	      // 콤보박스에서 선택한 값 들고오는데
	      // day는 새로 설정한 값들 들고와야하니까 밑에서 선언 및 초기화
	      int year = (int) cbYear.getSelectedItem();
	      int month = (int) cbMonth.getSelectedItem() - 1;
	      
	      // day날짜 해당 년, 월에 맞게 설정하기(철진이가 한 부분 그대로)
	      // 월을 변경하면 일이 변경되면 되니까 cbMonth이벤트 처리에 넣어줌
	      if (src == cbMonth) {
	         cbDay.removeAllItems();
	         todayCal.set(year, month, 1);
	         int lastDay = todayCal.getActualMaximum(Calendar.DAY_OF_MONTH);

	         for (int day = 0; day < lastDay; day++) {
	            cbDay.addItem(day + 1);
	         }
	      }
	      
	      // 저거 cbMonth 실행하면 처음에 cbDay.removeAllItems();때문에 값이 없어서
	      // cbDay.getSelectedItem()에서 null떠서 널포인트 잡아줌
	      try {
	         // 오늘날짜 :  오늘 날짜 1번 Calendar객체에서 받아옴
	         Calendar today = Calendar.getInstance();
	         
	         int day = (int) cbDay.getSelectedItem() + 1;
	         // 타겟날짜 : 우리가 콤보박스로 설정할 날짜 2번 Calendar객체에 넣어줌
	         Calendar target = Calendar.getInstance();
	         target.set(year, month, day);
	         
	         // 타겟날짜가 오늘 날짜 이후인지 확인해서 boolean값으로 리턴..
	         boolean enable = target.after(today);
	         
	         // 타겟날짜가 오늘 날짜 이후 -> true -> setEnabled(true) -> 콤보박스 활성화
	         // 타겟날짜가 오늘 날짜 이전 -> false -> setEnabled(false) -> 콤보박스 비활성화
	         rbFalse.setSelected(true);
	         rbTrue.setEnabled(enable);
	         rbFalse.setEnabled(enable);
	      } catch (Exception e2) {}
	   }


	private void setDisplay() {
		setLayout(new BorderLayout());

		// North 제목
		JPanel pnlNorth = new JPanel();
		JPanel pnlTitle = new JPanel();
		pnlTitle.add(lblTitle);

		pnlNorth.add(pnlTitle);
		pnlNorth.add(tfTitle);

		add(pnlNorth, BorderLayout.NORTH);

		// Center 내용
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(taInput, BorderLayout.CENTER);

		JScrollPane scroll = new JScrollPane(taInput);
		scroll.setBorder(new LineBorder(Color.BLACK, 1));

		pnlCenter.add(scroll);

		add(pnlCenter, BorderLayout.CENTER);

		// South 일정 버튼
		JPanel pnlSouth = new JPanel(new BorderLayout());

		JPanel pnlPlanDate = new JPanel(new GridLayout(0, 1));
		pnlPlanDate.add(lblPlanDate);
		pnlPlanDate.add(lblDday);

		JPanel pnlBt = new JPanel(new GridLayout(0, 1));
		JPanel pnlcb = new JPanel();
		pnlcb.add(cbYear);
		pnlcb.add(cbMonth);
		pnlcb.add(cbDay);

		JPanel pnlRb = new JPanel();
		pnlRb.add(rbTrue);
		pnlRb.add(rbFalse);

		pnlBt.add(pnlcb);
		pnlBt.add(pnlRb);

		pnlSouth.add(pnlBt);
		pnlSouth.add(pnlPlanDate, BorderLayout.WEST);

		add(pnlSouth, BorderLayout.SOUTH);

		// 버튼 및 배경색 변경
		Utils.setPnlWhite(pnlNorth);
		Utils.setPnlWhite(pnlSouth);
		Utils.setPnlWhite(pnlTitle);
		Utils.setPnlWhite(pnlcb);
		Utils.setPnlWhite(pnlPlanDate);
		Utils.setPnlWhite(pnlCenter);
		Utils.setPnlWhite(pnlBt);
		Utils.setPnlWhite(pnlRb);
		cbDay.setBackground(Color.WHITE);
		cbYear.setBackground(Color.WHITE);
		cbMonth.setBackground(Color.WHITE);
	}

	private void showFrame() {
		setSize(300, 400);
		setVisible(true);
	}

	// @박철진 String 배열에서 배열 없이 바꾸다보니 에러떠서 int 값으로 형변환 바로 해줌.
	// 이거
	// 추가함++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public int getCbYear() {
		// String str = (String)cbYear.getSelectedItem();
		int num = (int) cbYear.getSelectedItem();
		return num;
	}

	public int getCbMonth() {
		// String str = (String)cbMonth.getSelectedItem();
		int num = (int) cbMonth.getSelectedItem();
		return num;
	}

	public int getCbDay() {
		// String str = (String)cbDay.getSelectedItem();
		int num = (int) cbDay.getSelectedItem();
		return num;
	}

	// 김정규 0614 추가함
	public String getTitle() {
		return tfTitle.getText();
	}

	public String getContents() {
		return taInput.getText();
	}

	public boolean checkRbTrue() {
		return rbTrue.isSelected();
	}

}

// Calendar cal = Calendar.getInstance();
// cal.set(year, month, 1);
//
//// 해당 달의 시작 요일 : Sun->1 ...Sat->7
// firstDay = cal.get(Calendar.DAY_OF_WEEK);
// endOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
// days = new DayComponent[endOfMonth];
// lineCount = getLine();
