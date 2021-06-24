package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class DeleteDlg extends JDialog implements ActionListener {
	public static final int NAME = 0;
	public static final int PW = 1;
	public String[] names = { "Name", "Password" };
	private JLabel lblTitle;
	private JButton btnDel;
	private JButton btnCancel;
	private JTextComponent[] inputs;
	// private User user;
	private SelectUserForm owner;

	public DeleteDlg(SelectUserForm owner) {
		super(owner, "Delete User", true);
		this.owner = owner;
		// this.user = user;
		init();
		setDisplay();
		addListener();
		showDlg();
	}

	private void init() {
		// �ʱ�ȭ
		Font font = new Font(Font.DIALOG, Font.BOLD, 20);
		lblTitle = new JLabel("Delete UserInfo");
		lblTitle.setFont(font);

		btnDel = new JButton("Delete");
		btnCancel = new JButton("Cancel");

		inputs = new JTextComponent[] { new JTextField(10), new JPasswordField(10) };
	}

	private void setDisplay() {
		// ��ġ
		// BorderLayout.NORTH : ����
		JPanel pnlNorth = new JPanel();
		pnlNorth.add(lblTitle);

		// BorderLayout.CENTER : �̸�, ��й�ȣ �Է� �ʵ�
		JPanel pnlCenter = new JPanel(new GridLayout(0, 1));
		for (int i = 0; i < names.length; i++) {
			JPanel pnl = new JPanel();
			pnl.add(Utils.getLabel(names[i]));
			pnl.add(inputs[i]);
			Utils.setPnlWhite(pnl);
			pnlCenter.add(pnl);
		}

		// BorderLayout.SOUTH : ��ư
		JPanel pnlBtns = new JPanel();
		pnlBtns.add(btnDel);
		pnlBtns.add(btnCancel);

		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.add(pnlNorth, BorderLayout.NORTH);
		pnlMain.add(pnlCenter, BorderLayout.CENTER);
		pnlMain.add(pnlBtns, BorderLayout.SOUTH);

		add(pnlMain, BorderLayout.CENTER);
		
		// ��ư �� ���� ����
		Utils.setBtnWhite(btnCancel);
		Utils.setBtnWhite(btnDel);
		Utils.setLblWhite(lblTitle);
		Utils.setPnlWhite(pnlMain);
		Utils.setPnlWhite(pnlCenter);
		Utils.setPnlWhite(pnlBtns);
		Utils.setPnlWhite(pnlNorth);
		
	}

	private void addListener() {
		btnDel.addActionListener(this);
		btnCancel.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				closeDlg();
			}
		});
	}

	private void showDlg() {
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	public void closeDlg() {
		owner.setVisible(true);
		dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		// btnDel ��ư �̺�Ʈ ó�� : ȸ��Ż�� �Ϸ� -> Ż���� ���� ���Ϸ� ����
		if (src == btnDel) {
			boolean flag = true;
			String msg = "GoodBye~~";
			User user = owner.findUser(inputs[NAME].getText());

			// ������ �Էµ��� ���� �����̸�, Focus
			for (int i = 0; flag && i < inputs.length; i++) {
				if (Utils.isEmpty(inputs[i])) {
					flag = false;
					msg = "missing input : " + names[i];
					inputs[i].requestFocus();
				}
			}

			// �̸� üũ
			if (flag) {
				if (user == null) {
					flag = false;
					msg = "Warning : Check your name";
					inputs[NAME].requestFocus();
				}
			}

			// ��� üũ
			if (flag) {
				if (inputs[PW].getText().equals(user.getPw())) {
				} else {
					flag = false;
					msg = "Warning : check your password";
					inputs[PW].requestFocus();
				}
			}
			JOptionPane.showMessageDialog(this, msg, "Information", JOptionPane.INFORMATION_MESSAGE);

			if (flag) {
//				System.out.println(user.getName());
				owner.removeUser(user);
				dispose();
				new SelectUserForm();
			}
		} else {
			closeDlg();
		}
	}

}
