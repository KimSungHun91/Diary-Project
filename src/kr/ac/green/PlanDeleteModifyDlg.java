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

	// ���� ��ư�� �������
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
		// North�� ����
		lblTitle = new JLabel("����");
		tfTitle = new JTextField(20); // ���� �Է�

		// Center�� �����Է�
		taInput = new JTextArea(10, 20);
		taInput.setLineWrap(true);

		// south�� ����

		lblDday = new JLabel("D-day");
		rbTrue = new JRadioButton("���");
		rbFalse = new JRadioButton("�̵��");
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

		// Font ����
		lblTitle.setFont(font);

		// btn ��� �ݱ�
		if (flag) {
			btnConfirm = new JButton("����");
		} else {
			btnConfirm = new JButton("Ȯ��");
		}

		btnDelete = new JButton("����");
		btnCancle = new JButton("���");

	}

	private void setDisplay() {
		setLayout(new BorderLayout());

		// North ����
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

		// Center ����
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(taInput, BorderLayout.CENTER);

		JScrollPane scroll = new JScrollPane(taInput);


		pnlCenter.add(scroll);

		add(pnlCenter, BorderLayout.CENTER);

		// South ���� ��ư

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

		// Panel ������� ����
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
			// ����->Ȯ�ι�ư���� �����߰�,
			// ����(ToDoList)�� �ٽ� ���� �����ؾ��մϴ�.
			// �׷��� �Ķ���ͷ� �޾ƿ� dayComponent����
			// getYear..���ͷ� ��, ��, �� �ް�,
			// tfTitle.getText(), taInput.getText()�� ������
			// ���ο� ToDoList������ ���� �ʿ� �����մϴ�.
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

			// dayComponent�� testMap�� ���������ϱ�
			// ������ �Ķ���� dayComponent�� �����մϴ�
			// �׸��� ���� �����ؼ� ������Ʈ�� �߰��� ���·�
			// ���������մϴ�...���󰡸�
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
					// ������ ���̰ų� ����Ǿ����� ���� �߰� ����, YES_NO_CANCEL
					if (toDoList == null || isChanged()) {
						int result = JOptionPane.showConfirmDialog(null, "���� ������ �ֽ��ϴ�. �����Ͻðڽ��ϱ�?", "Question",
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
						// ���� �߰�
						if (result == JOptionPane.YES_OPTION) {
							modify();
							if (toDoListDlg != null) {
								toDoListDlg.reset();
							}
							dispose();
							// �߰� ���ϰ� â ��(�޸����� �������)
						} else if (result == JOptionPane.NO_OPTION) {
							toDoListDlg.setEnabled(true);
							dispose();
							try {
								// new ToDoListDlg(dayComponent);
							} catch (Exception e2) {
							}
							// �߰� ���ϰ� â �״��(�޸����� ���)
						} else if (result == JOptionPane.CANCEL_OPTION) {

						}
					} else {
						// ������ �ִµ� ���� �������� �״�� ����
						toDoListDlg.setEnabled(true);
						dispose();
					}
					// �⺻ �ƹ��͵� �ȳ��� ���¿��� ����ϸ� �ٷ� ����
				} else if (isEmpty() && toDoList == null) {
					toDoListDlg.setEnabled(true);
					dispose();
				}
				
				
				
				
				
				
				
				// if (!isEmpty()) {
				// if (isChanged()) {
				// int result = JOptionPane.showConfirmDialog(null, "����� ������
				// �ֽ��ϴ�. ���� �Ͻðڽ��ϱ�?", "Information",
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
//					int result = JOptionPane.showConfirmDialog(null, "�Էµ� ������ �ֽ��ϴ�. �׷��� ��� �Ͻðڽ��ϱ�?", "Information",
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
				// int result = JOptionPane.showConfirmDialog(null, "�Էµ� ������
				// �ֽ��ϴ�. �׷��� ��� �Ͻðڽ��ϱ�?", "Information",
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

	// �����ϴ���
	public boolean isChanged() {
		// toDoList�� �����, ������ �Է��� tfTitle�� ���� equals�� �� + ���뵵 �Ȱ��� equals�� ��
		// ����� ���� ���� ���� equals�� ���ѰŶ� ���������� = ����Ȱ�
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

		// ��Ҵ� ���� ���� �׳� â ���ְԸ�
		// ��ҹ�ư �̺�Ʈ ó��
		if (btnCancle == e.getSource()) {
			// ������ �� �׳� ��Ҵ����� ������Ʈ���� Ʈ����ĳġ�� ���
			// ����ĭ�� ������� ������ ����� ��������
			if (!isEmpty()) {
				// ������ ���̰ų� ����Ǿ����� ���� �߰� ����, YES_NO_CANCEL
				if (toDoList == null || isChanged()) {
					int result = JOptionPane.showConfirmDialog(this, "���� ������ �ֽ��ϴ�. �����Ͻðڽ��ϱ�?", "Question",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					// ���� �߰�
					if (result == JOptionPane.YES_OPTION) {
						modify();
						if (toDoListDlg != null) {
							toDoListDlg.reset();
						}
						dispose();
						// �߰� ���ϰ� â ��(�޸����� �������)
					} else if (result == JOptionPane.NO_OPTION) {
						toDoListDlg.setEnabled(true);
						dispose();
						try {
							// new ToDoListDlg(dayComponent);
						} catch (Exception e2) {
						}
						// �߰� ���ϰ� â �״��(�޸����� ���)
					} else if (result == JOptionPane.CANCEL_OPTION) {

					}
				} else {
					// ������ �ִµ� ���� �������� �״�� ����
					toDoListDlg.setEnabled(true);
					dispose();
				}
				// �⺻ �ƹ��͵� �ȳ��� ���¿��� ����ϸ� �ٷ� ����
			} else if (isEmpty() && toDoList == null) {
				toDoListDlg.setEnabled(true);
				dispose();
			}
		}

		// ���� ������ �ִ��� Ȯ���ϰ�
		else if (isEmpty()) {
			JOptionPane.showMessageDialog(this, "������ �Է��ϼ���.", "Information", JOptionPane.INFORMATION_MESSAGE);
			tfTitle.requestFocus();
		} else {
			// Ȯ�ι�ư �̺�Ʈ ó��
			if (btnConfirm == e.getSource()) {
				// null�̸� �ϴ� �׳� ����
				if (toDoList == null) {
					modify();
					toDoListDlg.setEnabled(true);
					if (toDoListDlg != null) {
						toDoListDlg.reset();
					}
					dispose();
				}
				// ����� ������ ������� Ȯ�� ��, �����Ҳ��� Ȯ��
				else if (isChanged()) {
					int result = JOptionPane.showConfirmDialog(this, "���� ������ �ֽ��ϴ�. �����Ͻðڽ��ϱ�?", "Question",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						modify();
						toDoListDlg.setEnabled(true);
						if (toDoListDlg != null) {
							toDoListDlg.reset();
						}
						dispose();
					}
					// ����� ���� ������ ������ �׳� �Ѿ
				} else {
					toDoListDlg.setEnabled(true);
					dispose();
				}
			}

			// ������ư �̺�Ʈ ó��
			else if (btnDelete == e.getSource()) {
				// null �ƴҶ��� ����, ���ϸ� ������Ʈ ��
				if (toDoListDlg != null) {
					int result = JOptionPane.showConfirmDialog(this, "�����Ͻðڽ��ϱ�?", "Question", JOptionPane.YES_NO_OPTION,
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