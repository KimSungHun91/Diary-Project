package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ToDoListDlg extends JDialog implements ActionListener {
	private JLabel lblTitle;
	private JLabel lblName;

	private JButton btnAdd;
	private JButton btnDelAll;

	private Vector<String> todoTitle = new Vector<String>();
	private Vector<ToDoList> list;
	private HashMap<Integer, Vector<ToDoList>> mapDiaryData;

	private DayPanel owner;
	private Year ownerY;
	private int date;
	private boolean changed = false;

	private JPanel pnlAll;

	// 이걸로 생성되고(DayComponent의 정보를 가짐)
	public ToDoListDlg(DayPanel owner) {
		this.owner = owner;
		init();
		setDisplay();
		addListeners();
		showDlg();
	}
	
	public ToDoListDlg(DayPanel owner, Year ownerY){
		this.owner = owner;
		this.ownerY = ownerY;
		init();
		setDisplay();
		addListeners();
		showDlg();
	}
	
	private void changEnable(){
		if(ownerY == null){
			owner.testEnable();
		} else {
			ownerY.setEnabled(true);
		}
	}
	
	private void init() {
		// testMap = new HashMap<Integer, Vector<ToDoList>>();
		// 초기화
		Font font = new Font(Font.DIALOG, Font.BOLD, 20);
		lblTitle = new JLabel("TodoList");
		lblTitle.setFont(font);

		mapDiaryData = owner.getMap();
		date = owner.getDate();
		list = mapDiaryData.get(date);

		pnlAll = new JPanel(new BorderLayout());
		btnAdd = new JButton("Add");
		btnDelAll = new JButton("DelAll");
	}

	private void setDisplay() {
		// list = testMap.get(1);
		// System.out.println(list);

		// 배치
		// BorderLayout.NORTH : 제목
		JPanel pnlNorth = new JPanel();
		pnlNorth.add(lblTitle);

		// BorderLayout.CENTER : UserList
		// 3항 연산자로 그리드레이아웃 사이즈 조절
		int size = list != null && list.size() < 5 ? 4 : 0;
		JPanel pnlCenter = new JPanel(new GridLayout(size, 1));
		if (list != null) {
			for (ToDoList todoList : list) {
				JPanel pnlTemp = new JPanel();
				JPanel pnl = new JPanel();
				pnl.setPreferredSize(new Dimension(250, 30));
				lblName = new JLabel(todoList.getShortTitle());
				pnl.add(lblName);
				todoTitle.add(lblName.getText());
				pnl.setBackground(Color.WHITE);
				pnl.setBorder(new LineBorder(Color.GRAY, 1));

				pnl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						pnl.setBackground(Color.RED);
					}

					@Override
					public void mouseExited(MouseEvent e) {
						pnl.setBackground(Color.WHITE);
					}

					@Override
					public void mousePressed(MouseEvent e) {
						setEnabled(false);
						new PlanDeleteModifyDlg(ToDoListDlg.this, mapDiaryData, todoList, date, true);
					}
				});
				pnlTemp.add(pnl);
				pnlCenter.add(pnlTemp);
			}
		}

		// BorderLayout.SOUTH : 버튼
		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnAdd);
		pnlSouth.add(btnDelAll);

		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.add(pnlNorth, BorderLayout.NORTH);
		
		JScrollPane scroll = new JScrollPane(pnlCenter);
		scroll.setBorder(new LineBorder(Color.BLACK, 1));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		pnlMain.add(scroll, BorderLayout.CENTER);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		// pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));

		pnlAll.add(pnlMain);
		add(pnlAll, BorderLayout.CENTER);

		// 버튼 배경색 변경
		Utils.setBtnWhite(btnAdd);
		Utils.setBtnWhite(btnDelAll);
		Utils.setLblWhite(lblTitle);
		Utils.setPnlWhite(pnlMain);
		Utils.setPnlWhite(pnlNorth);
		Utils.setPnlWhite(pnlCenter);
		Utils.setPnlWhite(pnlSouth);
		Utils.setPnlWhite(pnlAll);
	}

	private void addListeners() {
		// Add 버튼 연결
		btnAdd.addActionListener(this);
		// DelAll 버튼 연결
		btnDelAll.addActionListener(this);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
//				owner.testEnable();
				changEnable();
				
				if(ownerY != null){
					owner.lblVisible(false);
				}
				dispose();
			}
		});
		// addWindowListener(new WindowAdapter() {
		// @Override
		// public void windowClosing(WindowEvent we) {
		// int result = JOptionPane.showConfirmDialog(ToDoListDlg.this, "exit?",
		// "question",
		// JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		// if (result == JOptionPane.YES_OPTION) {
		// dispose();
		// }
		// }
		// });
	}

	private void showDlg() {
		setTitle("TodoList");
		setSize(310, 310);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void reset() {
		pnlAll.removeAll();
		init();
		setDisplay();
		addListeners();
		pnlAll.updateUI();
		owner.reset();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == btnAdd) {
			// 클래스 이름 바꿔야 할듯
			// PlanDeleteModify(정보 수정 추가 삭제) 하는
			// 창을 띄웁니다, 생성자파라미터는 DayComponent owner..
			// 따라가면
			// setVisible(false);
			setEnabled(false);
			new PlanDeleteModifyDlg(this, this.owner, false);
		} else {
			// 모두 삭제 이벤트 처리(수정)
			try {
				if (list.size() > 0) {
					int result = JOptionPane.showConfirmDialog(this, "Are you sure?", "question",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						mapDiaryData.get(date).removeAll(list);
						reset();
						//owner.testEnable();
						changEnable();
						dispose();
					}
				} else {
					JOptionPane.showMessageDialog(this, "Check your input");
				}
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(this, "Check your input");
			}
		}
	}

}
