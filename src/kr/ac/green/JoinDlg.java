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

public class JoinDlg extends JDialog implements ActionListener {
	public static final int NAME = 0;
	public static final int PW = 1;
	public static final int RE = 2;
	public String[] names = { "Name", "Password", "Retry" };
	private JLabel lblTitle;
	private JButton btnAdd;
	private JButton btnCancel;
	private JTextComponent[] inputs;
	private SelectUserForm owner;

	public JoinDlg(SelectUserForm owner) {
		super(owner, "JoinUs", true);
		this.owner = owner;
		init();
		setDisplay();
		addListener();
		showDlg();
	}

	private void init() {
		// 초기화
		Font font = new Font(Font.DIALOG, Font.BOLD, 20);
		lblTitle = new JLabel("Join Diary");
		lblTitle.setFont(font);

		btnAdd = new JButton("Add");
		btnCancel = new JButton("Cancel");

		inputs = new JTextComponent[] { new JTextField(10), new JPasswordField(10), new JPasswordField(10) };
	}

	private void setDisplay() {
		
		// 배치
		// BorderLayout.NORTH : 제목
		JPanel pnlNorth = new JPanel();
		pnlNorth.add(lblTitle);		

		// BorderLayout.CENTER : 이름, 비밀번호 입력 필드
		JPanel pnlCenter = new JPanel(new GridLayout(0, 1));
		for (int i = 0; i < names.length; i++) {
			JPanel pnl = new JPanel();
			pnl.add(Utils.getLabel(names[i]));
			pnl.add(inputs[i]);
			Utils.setPnlWhite(pnl);
			pnlCenter.add(pnl);
		}

		// BorderLayout.SOUTH : 버튼
		JPanel pnlBtns = new JPanel();
		pnlBtns.add(btnAdd);
		pnlBtns.add(btnCancel);

		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.add(pnlNorth, BorderLayout.NORTH);
		pnlMain.add(pnlCenter, BorderLayout.CENTER);
		pnlMain.add(pnlBtns, BorderLayout.SOUTH);

		add(pnlMain, BorderLayout.CENTER);

		// 버튼 및 레이블 배경색 변경
		Utils.setPnlWhite(pnlCenter);
		Utils.setPnlWhite(pnlMain);
		Utils.setPnlWhite(pnlNorth);
		Utils.setPnlWhite(pnlBtns);
		Utils.setBtnWhite(btnAdd);
		Utils.setBtnWhite(btnCancel);
		Utils.setLblWhite(lblTitle);

	}

	private void addListener() {
		btnAdd.addActionListener(this);
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

		// Add 버튼 이벤트 처리 : 회원가입 완료 -> 가입한 정보 파일로 저장
		if (src == btnAdd) {
			boolean flag = true;
			String msg = "Join OK";

			// 정보가 입력되지 않은 상태이면, Focus
			for (int i = 0; flag && i < inputs.length; i++) {
				if (Utils.isEmpty(inputs[i])) {
					flag = false;
					msg = "missing input : " + names[i];
					inputs[i].requestFocus();
				}
			}

			// 이름 체크
			if (flag) {
				User user = owner.findUser(inputs[NAME].getText());
				if (user != null) {
					flag = false;
					msg = "Warning : Name alreadt existed";
					inputs[NAME].requestFocus();
				}
			}

			// 비번 체크
			if (flag) {
				if (!inputs[PW].getText().equals(inputs[RE].getText())) {
					flag = false;
					msg = "check your password";
					inputs[PW].requestFocus();
				}
			}

			// 가입 성공
			if (flag) {
				User user = new User(inputs[NAME].getText(), inputs[PW].getText());
				owner.addUser(user);
				dispose();
				// owner.saveData();
				new SelectUserForm();
			}
			JOptionPane.showMessageDialog(this, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
		} else {
			closeDlg();
		}
	}
}
