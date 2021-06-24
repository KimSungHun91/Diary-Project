package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SelectUserForm extends JFrame implements ActionListener {
	private JLabel lblTitle;
	private JLabel lblName;
	private JButton btnAdd;
	private JButton btnDel;

	private Vector<String> userNames = new Vector<String>();
	// ����� �̸� �ߺ� ���Ҳ��� ������Map<String(name), User>���� �ϴ°͵� ������
	private Vector<User> list;
	private final String USER_DATA = "userData.dat";

	private boolean changed = false;

	private HashMap<Integer, Vector<ToDoList>> mapDiaryData; // ����ڰ� � ������ ��� �մ�
	private String password;

	public SelectUserForm() {
		loadData();
		init();
		setDisplay();
		addListeners();
		showFrame();
	}

	public Vector<User> getList() {
		return list;
	}

	// ���� ������ �ε�, ObjectInputStream
	private void loadData() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		list = new Vector<User>();
		Object obj = null;

		try {
			fis = new FileInputStream(USER_DATA);
			ois = new ObjectInputStream(fis);
			obj = ois.readObject();
			list.addAll((Vector<User>) obj);
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} finally {
			Utils.closeAll(ois, fis);
		}
	}

	// �̰� �ϳ��� ���� �� ���� �ٵ�!!
	private void loadToDo(String userName) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		mapDiaryData = new HashMap<Integer, Vector<ToDoList>>();
		try {
			fis = new FileInputStream(".\\" + userName + "\\ToDoList.dat");
			ois = new ObjectInputStream(fis);
			mapDiaryData = (HashMap<Integer, Vector<ToDoList>>) ois.readObject();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			Utils.closeAll(fis, ois);
		}
	}

	// ���� ������ ����, ObjectOutputStream
	public void saveData() {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File dir = new File("");

		try {
			fos = new FileOutputStream(USER_DATA);
			oos = new ObjectOutputStream(fos);

			oos.writeObject(list);
			oos.flush();
			oos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Utils.closeAll(oos, fos);
		}
	}

	private void init() {
		// testMap = new HashMap<Integer, Vector<ToDoList>>();
		// �ʱ�ȭ
		Font font = new Font(Font.DIALOG, Font.BOLD, 20);
		lblTitle = new JLabel("Diary");
		lblTitle.setFont(font);

		btnAdd = new JButton("Add");
		btnDel = new JButton("del");
	}

	private void setDisplay() {
		// ������ ����
		Utils.setIcon(this);

		// ��ġ
		// BorderLayout.NORTH : ����
		JPanel pnlNorth = new JPanel();
		pnlNorth.add(lblTitle);

		// BorderLayout.CENTER : UserList
		// 3�� �����ڷ� �׸��巹�̾ƿ� ������ ����
		int size = list != null && list.size() < 5 ? 4 : 0;
		JPanel pnlCenter = new JPanel(new GridLayout(size, 1));
		for (User user : list) {
			JPanel pnlTemp = new JPanel();
			JPanel pnl = new JPanel();
			pnl.setPreferredSize(new Dimension(250, 30));
			lblName = new JLabel(user.getName());
			pnl.add(lblName);
			userNames.add(lblName.getText());
			pnl.setBackground(Color.WHITE);
			pnl.setBorder(new LineBorder(Color.GRAY, 1));

			pnl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					pnl.setBackground(Color.DARK_GRAY);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					pnl.setBackground(Color.WHITE);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// ���콺�� ��������Ʈ���� �ϳ� �����ϸ� �α��θ޼ҵ� ȣ��
					// � ����� �������� ������ �Ķ���ͷ� ��
					login(user.getName());
				}
			});
			pnlTemp.add(pnl);
			Utils.setPnlWhite(pnlTemp);
			pnlCenter.add(pnlTemp);
		}

		// BorderLayout.SOUTH : ��ư
		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnAdd);
		pnlSouth.add(btnDel);
		

		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.add(pnlNorth, BorderLayout.NORTH);
		pnlMain.add(new JScrollPane(pnlCenter), BorderLayout.CENTER);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));

		add(pnlMain, BorderLayout.CENTER);

		// ��ư �� ���� ����
		Utils.setBtnWhite(btnAdd);
		Utils.setBtnWhite(btnDel);
		try {
			Utils.setLblWhite(lblName);
		} catch (NullPointerException e) {}

		Utils.setLblWhite(lblTitle);
		Utils.setPnlWhite(pnlMain);
		Utils.setPnlWhite(pnlCenter);
		Utils.setPnlWhite(pnlNorth);
		Utils.setPnlWhite(pnlSouth);
	}

	private void addListeners() {
		// Add ��ư ����
		btnAdd.addActionListener(this);
		// Del ��ư ����
		btnDel.addActionListener(this);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				int result = JOptionPane.showConfirmDialog(SelectUserForm.this, "exit?", "question",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					if (changed) {
						setVisible(false);
						// saveData();
					}
					System.exit(0);
				}
			}
		});
	}

	private void showFrame() {
		setTitle("Login");
		setSize(310, 310);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	// @��ö�� PasswordField �߰��ؼ� ��й�ȣ ǥ�� ****���� ����
	private void login(String name) {
		JPasswordField pf = new JPasswordField();
		JPanel pnlMsg = new JPanel(new GridLayout(0, 1));
		pnlMsg.add(new Label("input your passoword", JLabel.CENTER));
		pnlMsg.add(pf);
		pnlMsg.setFont(new Font(Font.DIALOG, Font.BOLD, 13));

		int result = JOptionPane.showConfirmDialog(null, pnlMsg, "Enter password", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			password = new String(pf.getPassword());
		}
		// @��ö��--------------------------------------------------------------�������
		// ����

		boolean flag = false;
		if (password != null) {
			String msg = "welcome!!";
			User user = findUser(name);
			flag = true;
			if (!password.equals(user.getPw())) {
				flag = false;
				msg = "check your password";
			}

			JOptionPane.showMessageDialog(this, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
			if (flag) {
				loadToDo(name);
				new MyCalendar(mapDiaryData, name);
				dispose();
			}
		}
	}
	
	// private void login(String name) {
	// // �α��� �� ��й�ȣ üũ
	// String password = JOptionPane.showInputDialog(this, "input your
	// password", "check password",
	// JOptionPane.INFORMATION_MESSAGE);
	// boolean flag = false;
	// if (password != null) {
	// String msg = "welcome!!";
	// User user = findUser(name);
	// flag = true;
	// if (!password.equals(user.getPw())) {
	// flag = false;
	// msg = "check your password";
	// }
	//
	// JOptionPane.showMessageDialog(this, msg, "Information",
	// JOptionPane.INFORMATION_MESSAGE);
	// if (flag) {
	// loadToDo(name);
	// new CopyTestMyCalendar(testMap, name);
	// dispose();
	// }
	// }
	// }

	
	// private void delete() {
	// // ���� ���� �� ���ߴ��̾�α׷� �ٽ� �ѹ� Ȯ�� ��
	// int result = JOptionPane.showConfirmDialog(this, "do you really want to
	// delete?", "Question",
	// JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	// // YES_OPTION�̸� ��й�ȣ Ȯ�� �� ���� ����
	// if (result == JOptionPane.YES_OPTION) {
	// String password = JOptionPane.showInputDialog(this, "input your
	// password", "check password",
	// JOptionPane.INFORMATION_MESSAGE);
	// }
	// }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		// Add ��ư �̺�Ʈ ó��, ����â���� �Ѿ
		if (src == btnAdd) {
			setVisible(false);
			new JoinDlg(this);
		}
		// del ��ư �̺�Ʈ ó��, ��������
		if (src == btnDel) {
			setVisible(false);
			new DeleteDlg(this);
		}
	}

	public User findUser(String userName) {
		int idx = list.indexOf(new User(userName));
		if (idx >= 0) {
			return list.get(idx);
		} else {
			return null;
		}
	}

	public void addUser(User user) {
		if (findUser(user.getName()) == null) {
			list.add(user);
			saveData();

			File dir = new File(user.getName());
			dir.mkdir();

			// mkUserDir(list);
			// changed = true;
		}
	}

	/////////////////// 0614 ������ �����Ϸ�
	public void removeUser(User user) {
		list.remove(user);
		saveData();
		File f = new File(".\\" + user.getName() + "\\ToDoList.dat");
		File dir = new File(user.getName());
		f.delete();
		dir.delete();
		// changed = true;
	}

	public static void main(String[] args) {
		new SelectUserForm();
	}
}
