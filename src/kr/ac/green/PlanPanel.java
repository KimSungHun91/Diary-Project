package kr.ac.green;

// ������
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
		// North�� ����
		lblTitle = new JLabel("����");
		tfTitle = new JTextField(20); // ���� �Է�

		// Center�� �����Է�
		taInput = new JTextArea(10, 20);
		taInput.setLineWrap(true);

		// south�� ����
		lblPlanDate = new JLabel("����");
		lblDday = new JLabel("D-day");
		// Font ����
		lblTitle.setFont(font);
		lblPlanDate.setFont(font);
		lblDday.setFont(font);

		// @��ö�� ComboBox
		// ����------------------------------------------------------����
		todayMonth = todayCal.get(Calendar.MONTH) + 1;// 0~11���� �����ޱ� ������ +1
		todayYear = todayCal.get(Calendar.YEAR);
		// ��,�� �⺻ ���ð��� ���� ��¥

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

		// -----------------------------------------------------------------��

		rbTrue = new JRadioButton("���");
		rbFalse = new JRadioButton("�̵��", true);
		rbTrue.setBackground(Color.WHITE);
		rbFalse.setBackground(Color.WHITE);
		ButtonGroup group = new ButtonGroup();
		group.add(rbTrue);
		group.add(rbFalse);

		// �� �� ��
	}

	// �޺��ڽ� �� ���� �� �� �����ϱ�
	private void addListener() {
	      cbYear.addActionListener(this);
	      cbMonth.addActionListener(this);
	      cbDay.addActionListener(this);
	   }

	   @Override
	   public void actionPerformed(ActionEvent e) {
	      // actionPerformed���� �ҽ��� �޺��ڽ� ���� ���ϸ� �̺�Ʈ ������ �ڵ����� ������

	      Object src = e.getSource();
	      // �޺��ڽ����� ������ �� �����µ�
	      // day�� ���� ������ ���� ���;��ϴϱ� �ؿ��� ���� �� �ʱ�ȭ
	      int year = (int) cbYear.getSelectedItem();
	      int month = (int) cbMonth.getSelectedItem() - 1;
	      
	      // day��¥ �ش� ��, ���� �°� �����ϱ�(ö���̰� �� �κ� �״��)
	      // ���� �����ϸ� ���� ����Ǹ� �Ǵϱ� cbMonth�̺�Ʈ ó���� �־���
	      if (src == cbMonth) {
	         cbDay.removeAllItems();
	         todayCal.set(year, month, 1);
	         int lastDay = todayCal.getActualMaximum(Calendar.DAY_OF_MONTH);

	         for (int day = 0; day < lastDay; day++) {
	            cbDay.addItem(day + 1);
	         }
	      }
	      
	      // ���� cbMonth �����ϸ� ó���� cbDay.removeAllItems();������ ���� ���
	      // cbDay.getSelectedItem()���� null���� ������Ʈ �����
	      try {
	         // ���ó�¥ :  ���� ��¥ 1�� Calendar��ü���� �޾ƿ�
	         Calendar today = Calendar.getInstance();
	         
	         int day = (int) cbDay.getSelectedItem() + 1;
	         // Ÿ�ٳ�¥ : �츮�� �޺��ڽ��� ������ ��¥ 2�� Calendar��ü�� �־���
	         Calendar target = Calendar.getInstance();
	         target.set(year, month, day);
	         
	         // Ÿ�ٳ�¥�� ���� ��¥ �������� Ȯ���ؼ� boolean������ ����..
	         boolean enable = target.after(today);
	         
	         // Ÿ�ٳ�¥�� ���� ��¥ ���� -> true -> setEnabled(true) -> �޺��ڽ� Ȱ��ȭ
	         // Ÿ�ٳ�¥�� ���� ��¥ ���� -> false -> setEnabled(false) -> �޺��ڽ� ��Ȱ��ȭ
	         rbFalse.setSelected(true);
	         rbTrue.setEnabled(enable);
	         rbFalse.setEnabled(enable);
	      } catch (Exception e2) {}
	   }


	private void setDisplay() {
		setLayout(new BorderLayout());

		// North ����
		JPanel pnlNorth = new JPanel();
		JPanel pnlTitle = new JPanel();
		pnlTitle.add(lblTitle);

		pnlNorth.add(pnlTitle);
		pnlNorth.add(tfTitle);

		add(pnlNorth, BorderLayout.NORTH);

		// Center ����
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(taInput, BorderLayout.CENTER);

		JScrollPane scroll = new JScrollPane(taInput);
		scroll.setBorder(new LineBorder(Color.BLACK, 1));

		pnlCenter.add(scroll);

		add(pnlCenter, BorderLayout.CENTER);

		// South ���� ��ư
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

		// ��ư �� ���� ����
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

	// @��ö�� String �迭���� �迭 ���� �ٲٴٺ��� �������� int ������ ����ȯ �ٷ� ����.
	// �̰�
	// �߰���++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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

	// ������ 0614 �߰���
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
//// �ش� ���� ���� ���� : Sun->1 ...Sat->7
// firstDay = cal.get(Calendar.DAY_OF_WEEK);
// endOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
// days = new DayComponent[endOfMonth];
// lineCount = getLine();
