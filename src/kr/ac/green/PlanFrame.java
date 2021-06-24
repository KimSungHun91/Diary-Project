// ���� ���缭 ���� ������! ������

package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PlanFrame extends JDialog implements ActionListener {
	private PlanPanel Planpnl;
	private JButton btnOK;
	private JButton btnCancle;
	private MyCalendar owner;

	public PlanFrame(MyCalendar owner) {
		super(owner);
		this.owner = owner;
		Planpnl = new PlanPanel();
		init();
		setDisplay();
		addListeners();
		showFrame();
	}

	private void init() {
		btnOK = new JButton("���");
		btnCancle = new JButton("���");
		Utils.setBtnWhite(btnOK);
		Utils.setBtnWhite(btnCancle);

		btnOK.addActionListener(this);
		btnCancle.addActionListener(this);
	}

	private void setDisplay() {
		add(Planpnl, BorderLayout.CENTER);
		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnOK);
		pnlSouth.add(btnCancle);

		// Panel ��� ����
		Utils.setPnlWhite(pnlSouth);

		add(pnlSouth, BorderLayout.SOUTH);
	}

	// ������ �߰�
	// *****************************************++++++++++++++++++++++++++++++
	@Override
	public void actionPerformed(ActionEvent e) {
		if (btnOK == e.getSource()) {
			if (isEmpty(Planpnl.getTitle())) {
				int year = Planpnl.getCbYear();
				int month = Planpnl.getCbMonth();
				int day = Planpnl.getCbDay();

				StringBuffer buf = new StringBuffer();
				buf.append(year);
				buf.append(month);
				buf.append(day);
				int num = Integer.parseInt(buf.toString());
				String title = Planpnl.getTitle();
				String contents = Planpnl.getContents();

				ToDoList toDo = new ToDoList(year, month, day, title, contents, Planpnl.checkRbTrue());

				HashMap<Integer, Vector<ToDoList>> map = owner.getMap();
				Vector<ToDoList> vec = map.get(num);

				if (vec == null) {
					vec = new Vector<ToDoList>();
					vec.add(toDo);
					map.put(num, vec);
				} else {
					vec.add(toDo);
					map.put(num, vec);
				}
				owner.reSet();
				owner.changeCalendar(year, month);
				owner.setEnabled(true);
				dispose();

			} else {
				JOptionPane.showMessageDialog(this, "������ �Է��ϼ���.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
			// @��ö�� ��ҹ�ư �� ���� ������ ������ �˾�â
		} else if (btnCancle == e.getSource()) {
			if (isEmpty(Planpnl.getTitle())) {
				int result = JOptionPane.showConfirmDialog(this, "�Էµ� ������ �ֽ��ϴ�. �׷��� ��� �Ͻðڽ��ϱ�?", "Information",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					owner.setEnabled(true);
					dispose();
				}
			} else {
				owner.setEnabled(true);
				dispose();
			}
		}

	}

	// @��ö�� ������ ����� ���â
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if (isEmpty(Planpnl.getTitle())) {
					int result = JOptionPane.showConfirmDialog(null, "�Էµ� ������ �ֽ��ϴ�. �׷��� ��� �Ͻðڽ��ϱ�?", "Information",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						owner.setEnabled(true);
						dispose();

					} else if (result == JOptionPane.NO_OPTION) {

					}
				} else {
					owner.setEnabled(true);
					dispose();
				}
			}
		});

	}

	// @Override
	// public void actionPerformed(ActionEvent e) {
	// if(btnOK == e.getSource()) {
	// if(isEmpty(Planpnl.getTitle())){
	// int year = Planpnl.getCbYear();
	// int month = Planpnl.getCbMonth();
	// int day = Planpnl.getCbDay();
	//
	// StringBuffer buf = new StringBuffer();
	// buf.append(year);
	// buf.append(month);
	// buf.append(day);
	// int num = Integer.parseInt(buf.toString());
	// String title = Planpnl.getTitle();
	// String contents = Planpnl.getContents();
	//
	// ToDoList toDo = new ToDoList(year, month, day, title, contents,
	// Planpnl.checkRbTrue());
	//
	// HashMap<Integer, Vector<ToDoList>> map = owner.getMap();
	// Vector<ToDoList> vec = map.get(num);
	//
	// if(vec == null){
	// vec = new Vector<ToDoList>();
	// vec.add(toDo);
	// map.put(num, vec);
	// } else {
	// vec.add(toDo);
	// map.put(num, vec);
	// }
	// owner.reSet();
	// owner.changeCalendar(year, month);
	// dispose();
	//
	// } else {
	// JOptionPane.showMessageDialog (
	// this,
	// "������ ��ĭ�ϼ� �����ϴ�",
	// "Information",
	// JOptionPane.INFORMATION_MESSAGE
	// );
	// }
	//
	// } else if(btnCancle == e.getSource()){
	// dispose();
	// }
	// }

	public boolean isEmpty(String input) {
		// �翷 ������ �� �ڸ� �ؽ�Ʈ�� ���̰� 0�̶�� �� �ʵ�� ����ִ�.
		input.trim();
		return (input.length() == 0) ? false : true;
	}

	private void showFrame() {
		setTitle("Plus ToDoList");
		setSize(300, 400);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(true);
		setVisible(true);
	}
}
