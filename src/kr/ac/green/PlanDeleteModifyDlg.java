package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PlanDeleteModifyDlg extends JDialog implements ActionListener {

	private JTextField tfTitle;
	private JTextArea taInput;
	private JLabel lblTitle;

	private int date;
	private ToDoList toDoList;
	private HashMap<Integer, Vector<ToDoList>> mapDiaryData;
	private ToDoListDlg toDoListDlg;
	private DayPanel dayPanel;
	private boolean changed = false;

	private JLabel lblDday;
	private JRadioButton rbTrue;
	private JRadioButton rbFalse;

	// 삭제 버튼이 없어야함
	private JButton btnConfirm;
	private JButton btnDelete;
	private JButton btnCancle;
	private boolean flag;

	public static final Font font = new Font(Font.DIALOG, Font.BOLD, 14);

	public PlanDeleteModifyDlg(ToDoListDlg toDoListDlg, HashMap<Integer, Vector<ToDoList>> mapDiaryData, ToDoList toDoList,
			int date, boolean flag) {
		this.toDoListDlg = toDoListDlg;
		this.mapDiaryData = mapDiaryData;
		this.date = date;
		this.toDoList = toDoList;
		this.flag = flag;
		init();
		setDisplay();
		addListeners();
		showDlg();
	}

	public PlanDeleteModifyDlg(ToDoListDlg toDoListDlg, DayPanel dayPanel, boolean flag) {
		this.toDoListDlg = toDoListDlg;
		this.dayPanel = dayPanel;
		this.flag = flag;
		init();
		setDisplay();
		addListeners();
		showDlg();
	}

	public HashMap<Integer, Vector<ToDoList>> getTestMap() {
		return mapDiaryData;
	}

	public int getDate() {
		return date;
	}

	private void init() {
		// North에 제목
		lblTitle = new JLabel("제목");
		tfTitle = new JTextField(20); // 제목 입력

		// Center에 내용입력
		taInput = new JTextArea(10, 20);
		taInput.setLineWrap(true);

		// south에 일정

		lblDday = new JLabel("D-day");
		rbTrue = new JRadioButton("등록");
		rbFalse = new JRadioButton("미등록");
		try {
			if (toDoList.getDday()) {
				rbTrue.setSelected(true);
			} else {
				rbFalse.setSelected(true);
			}
		} catch (NullPointerException e) {
			rbFalse.setSelected(true);
		}

		ButtonGroup group = new ButtonGroup();
		group.add(rbTrue);
		group.add(rbFalse);

		// Font 설정
		lblTitle.setFont(font);

		// btn 등록 닫기
		if (flag) {
			btnConfirm = new JButton("수정");
		} else {
			btnConfirm = new JButton("확인");
		}

		btnDelete = new JButton("삭제");
		btnCancle = new JButton("취소");

	}

	private void setDisplay() {
		setLayout(new BorderLayout());

		// North 제목
		JPanel pnlNorth = new JPanel();
		JPanel pnlTitle = new JPanel();
		pnlTitle.add(lblTitle);

		pnlNorth.add(pnlTitle);
		pnlNorth.add(tfTitle);
		if (toDoList != null) {
			tfTitle.setText(toDoList.getTitle());
			taInput.setText(toDoList.getContents());
		}

		add(pnlNorth, BorderLayout.NORTH);

		// Center 내용
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(taInput, BorderLayout.CENTER);

		JScrollPane scroll = new JScrollPane(taInput);


		pnlCenter.add(scroll);

		add(pnlCenter, BorderLayout.CENTER);

		// South 일정 버튼

		JPanel pnlSouth = new JPanel(new BorderLayout());
		JPanel pnlSouthRb = new JPanel();
		pnlSouthRb.add(lblDday);
		pnlSouthRb.add(rbTrue);
		pnlSouthRb.add(rbFalse);
		JPanel pnlSouthflow = new JPanel(new FlowLayout());
		pnlSouthflow.add(btnConfirm);

		if (flag) {
			pnlSouthflow.add(btnDelete);
		}
		pnlSouthflow.add(btnCancle);

		pnlSouth.add(pnlSouthRb, BorderLayout.NORTH);
		pnlSouth.add(pnlSouthflow, BorderLayout.SOUTH);

		add(pnlSouth, BorderLayout.SOUTH);

		Calendar today = Calendar.getInstance();
		Calendar dday = Calendar.getInstance();
		int count;
		if (toDoList == null) {
			dday.set(dayPanel.getYear(), dayPanel.getMonth(), dayPanel.getDay());
			long day = dday.getTimeInMillis() / 86400000;
			long tday = today.getTimeInMillis() / 86400000;
			count = (int) (tday - day);
			if (count > 0) {
				rbTrue.setEnabled(false);
				rbFalse.setEnabled(false);
			}
		} else {
			dday.set(toDoList.getYear(), toDoList.getMonth() - 1, toDoList.getDay());
			long day = dday.getTimeInMillis() / 86400000;
			long tday = today.getTimeInMillis() / 86400000;
			count = (int) (tday - day);
			if (count > 0) {
				rbTrue.setEnabled(false);
				rbFalse.setEnabled(false);
			}
		}

		// Panel 흰색으로 변경
		rbFalse.setBackground(Color.WHITE);
		rbTrue.setBackground(Color.WHITE);
		Utils.setBtnWhite(btnCancle);
		Utils.setBtnWhite(btnConfirm);
		Utils.setBtnWhite(btnDelete);
		Utils.setPnlWhite(pnlNorth);
		Utils.setPnlWhite(pnlSouth);
		Utils.setPnlWhite(pnlTitle);
		Utils.setPnlWhite(pnlCenter);
		Utils.setPnlWhite(pnlSouthflow);
		Utils.setPnlWhite(pnlSouthRb);

	}

	private void modify() {
		if (toDoList != null) {
			int idx = mapDiaryData.get(date).indexOf(toDoList);
			toDoList.setTitle(tfTitle.getText());
			toDoList.setContents(taInput.getText());
			toDoList.setDday(rbTrue.isSelected());
			toDoListDlg.setEnabled(true);
		} else {
			// 수정->확인버튼으로 변경했고,
			// 정보(ToDoList)를 다시 만들어서 저장해야합니다.
			// 그래서 파라미터로 받아온 dayComponent에서
			// getYear..겟터로 년, 월, 일 받고,
			// tfTitle.getText(), taInput.getText()의 값으로
			// 새로운 ToDoList정보를 만들어서 맵에 저장합니다.
			int year = dayPanel.getYear();
			int month = dayPanel.getMonth() + 1;
			int day = dayPanel.getDay();

			StringBuffer buf = new StringBuffer();
			buf.append(year);
			buf.append(month);
			buf.append(day);
			int num = Integer.parseInt(buf.toString());
			ToDoList toDo = new ToDoList(year, month, day, tfTitle.getText(), taInput.getText(), rbTrue.isSelected());

			mapDiaryData = dayPanel.getMap();
			Vector<ToDoList> vec = mapDiaryData.getOrDefault(num, new Vector<>());
			vec.add(toDo);
			mapDiaryData.put(num, vec);
			dayPanel.reset();

			// dayComponent의 testMap에 저장했으니까
			// 생성자 파라미터 dayComponent로 생성합니다
			// 그리고 새로 생성해서 컴포넌트가 추가된 상태로
			// 보여지게합니다...따라가면
			toDoListDlg.setEnabled(true);
			toDoListDlg.reset();
			// new ToDoListDlg(dayComponent);
			dispose();
		}
	}

	private void addListeners() {
		btnConfirm.addActionListener(this);
		btnDelete.addActionListener(this);
		btnCancle.addActionListener(this);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
			
				if (!isEmpty()) {
					// 정보가 널이거나 변경되었으면 정보 추가 할지, YES_NO_CANCEL
					if (toDoList == null || isChanged()) {
						int result = JOptionPane.showConfirmDialog(null, "변경 사항이 있습니다. 저장하시겠습니까?", "Question",
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
						// 정보 추가
						if (result == JOptionPane.YES_OPTION) {
							modify();
							if (toDoListDlg != null) {
								toDoListDlg.reset();
							}
							dispose();
							// 추가 안하고 창 끔(메모장의 저장안함)
						} else if (result == JOptionPane.NO_OPTION) {
							toDoListDlg.setEnabled(true);
							dispose();
							try {
								// new ToDoListDlg(dayComponent);
							} catch (Exception e2) {
							}
							// 추가 안하고 창 그대로(메모장의 취소)
						} else if (result == JOptionPane.CANCEL_OPTION) {

						}
					} else {
						// 정보는 있는데 변경 안했으면 그대로 종료
						toDoListDlg.setEnabled(true);
						dispose();
					}
					// 기본 아무것도 안넣은 상태에서 취소하면 바로 종료
				} else if (isEmpty() && toDoList == null) {
					toDoListDlg.setEnabled(true);
					dispose();
				}
				
				
				
				
				
				
				
				// if (!isEmpty()) {
				// if (isChanged()) {
				// int result = JOptionPane.showConfirmDialog(null, "변경된 내용이
				// 있습니다. 종료 하시겠습니까?", "Information",
				// JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				// if (result == JOptionPane.YES_OPTION) {
				// toDoListDlg.setEnabled(true);
				// dispose();
				// } else if (result == JOptionPane.NO_OPTION) {
				//
				// }
				// }
				// } else
//				if (isEmpty() && Utils.isEmpty(taInput)) {
//					toDoListDlg.setEnabled(true);
//					dispose();
//				} else {
//					int result = JOptionPane.showConfirmDialog(null, "입력된 내용이 있습니다. 그래도 취소 하시겠습니까?", "Information",
//							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//					if (result == JOptionPane.YES_OPTION) {
//						toDoListDlg.setEnabled(true);
//						dispose();
//					} else if (result == JOptionPane.NO_OPTION) {
//
//					}
//				}

				// if (isEmpty() && Utils.isEmpty(taInput)){
				// toDoListDlg.setEnabled(true);
				// dispose();
				// } else {
				// int result = JOptionPane.showConfirmDialog(null, "입력된 내용이
				// 있습니다. 그래도 취소 하시겠습니까?", "Information",
				// JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				// if(result == JOptionPane.YES_OPTION){
				// toDoListDlg.setEnabled(true);
				// dispose();
				// } else if(result == JOptionPane.NO_OPTION){
				//
				// }
				// }
			}
		});

	}

	private void delete() {
		if (toDoList != null) {
			mapDiaryData.get(date).remove(toDoList);
		}
	}

	private void showDlg() {
		setTitle("ToDoList Repair");
		setSize(300, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	public boolean isEmpty() {
		if (Utils.isEmpty(tfTitle)) {
			return true;
		} else {
			return false;
		}
	}

	// 여기하는중
	public boolean isChanged() {
		// toDoList의 제목과, 유저가 입력한 tfTitle의 제목 equals로 비교 + 내용도 똑같이 equals로 비교
		// 제목과 내용 각각 위에 equals로 비교한거랑 같지않으면 = 변경된거
		boolean checkFlag = false;
		if (toDoList.getDday() == true && rbTrue.isSelected() == false) {
			checkFlag = true;
		} else if (toDoList.getDday() == false && rbTrue.isSelected() == true) {
			checkFlag = true;
		} else {
			checkFlag = false;
		}

		if (!toDoList.getTitle().equals(tfTitle.getText()) || !toDoList.getContents().equals(taInput.getText())
				|| checkFlag) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// 취소는 따로 떼서 그냥 창 꺼주게만
		// 취소버튼 이벤트 처리
		if (btnCancle == e.getSource()) {
			// 공백일 때 그냥 취소누르면 널포인트떠서 트라이캐치로 잡고
			// 제목칸이 비어있지 않으면 물어보고 정보저장
			if (!isEmpty()) {
				// 정보가 널이거나 변경되었으면 정보 추가 할지, YES_NO_CANCEL
				if (toDoList == null || isChanged()) {
					int result = JOptionPane.showConfirmDialog(this, "변경 사항이 있습니다. 저장하시겠습니까?", "Question",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					// 정보 추가
					if (result == JOptionPane.YES_OPTION) {
						modify();
						if (toDoListDlg != null) {
							toDoListDlg.reset();
						}
						dispose();
						// 추가 안하고 창 끔(메모장의 저장안함)
					} else if (result == JOptionPane.NO_OPTION) {
						toDoListDlg.setEnabled(true);
						dispose();
						try {
							// new ToDoListDlg(dayComponent);
						} catch (Exception e2) {
						}
						// 추가 안하고 창 그대로(메모장의 취소)
					} else if (result == JOptionPane.CANCEL_OPTION) {

					}
				} else {
					// 정보는 있는데 변경 안했으면 그대로 종료
					toDoListDlg.setEnabled(true);
					dispose();
				}
				// 기본 아무것도 안넣은 상태에서 취소하면 바로 종료
			} else if (isEmpty() && toDoList == null) {
				toDoListDlg.setEnabled(true);
				dispose();
			}
		}

		// 제목에 공백이 있는지 확인하고
		else if (isEmpty()) {
			JOptionPane.showMessageDialog(this, "제목을 입력하세요.", "Information", JOptionPane.INFORMATION_MESSAGE);
			tfTitle.requestFocus();
		} else {
			// 확인버튼 이벤트 처리
			if (btnConfirm == e.getSource()) {
				// null이면 일단 그냥 저장
				if (toDoList == null) {
					modify();
					toDoListDlg.setEnabled(true);
					if (toDoListDlg != null) {
						toDoListDlg.reset();
					}
					dispose();
				}
				// 제목과 내용이 변경된지 확인 후, 저장할껀지 확인
				else if (isChanged()) {
					int result = JOptionPane.showConfirmDialog(this, "변경 사항이 있습니다. 저장하시겠습니까?", "Question",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						modify();
						toDoListDlg.setEnabled(true);
						if (toDoListDlg != null) {
							toDoListDlg.reset();
						}
						dispose();
					}
					// 제목과 내용 변경이 없으면 그냥 넘어감
				} else {
					toDoListDlg.setEnabled(true);
					dispose();
				}
			}

			// 삭제버튼 이벤트 처리
			else if (btnDelete == e.getSource()) {
				// null 아닐때만 삭제, 안하면 널포인트 뜸
				if (toDoListDlg != null) {
					int result = JOptionPane.showConfirmDialog(this, "삭제하시겠습니까?", "Question", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						delete();
						if (toDoListDlg != null) {
							toDoListDlg.reset();
						}
						toDoListDlg.setEnabled(true);
						dispose();
					}
				} else {
				}
			}
		}
	}
}