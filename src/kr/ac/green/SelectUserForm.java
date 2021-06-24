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
	// 사용자 이름 중복 안할꺼면 맵으로Map<String(name), User>으로 하는것도 좋을듯
	private Vector<User> list;
	private final String USER_DATA = "userData.dat";

	private boolean changed = false;

	private HashMap<Integer, Vector<ToDoList>> mapDiaryData; // 사용자가 어떤 정보를 담고 잇다
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

	// 유저 데이터 로드, ObjectInputStream
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

	// 이거 하나로 만들 수 있을 텐뎅!!
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

	// 유저 데이터 저장, ObjectOutputStream
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
		// 초기화
		Font font = new Font(Font.DIALOG, Font.BOLD, 20);
		lblTitle = new JLabel("Diary");
		lblTitle.setFont(font);

		btnAdd = new JButton("Add");
		btnDel = new JButton("del");
	}

	private void setDisplay() {
		// 아이콘 변경
		Utils.setIcon(this);

		// 배치
		// BorderLayout.NORTH : 제목
		JPanel pnlNorth = new JPanel();
		pnlNorth.add(lblTitle);

		// BorderLayout.CENTER : UserList
		// 3항 연산자로 그리드레이아웃 사이즈 조절
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
					// 마우스로 유저리스트에서 하나 선택하면 로그인메소드 호출
					// 어떤 사람을 누르는지 조건을 파라미터로 줌
					login(user.getName());
				}
			});
			pnlTemp.add(pnl);
			Utils.setPnlWhite(pnlTemp);
			pnlCenter.add(pnlTemp);
		}

		// BorderLayout.SOUTH : 버튼
		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnAdd);
		pnlSouth.add(btnDel);
		

		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.add(pnlNorth, BorderLayout.NORTH);
		pnlMain.add(new JScrollPane(pnlCenter), BorderLayout.CENTER);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));

		add(pnlMain, BorderLayout.CENTER);

		// 버튼 및 배경색 변경
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
		// Add 버튼 연결
		btnAdd.addActionListener(this);
		// Del 버튼 연결
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

	// @박철진 PasswordField 추가해서 비밀번호 표시 ****으로 수정
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
		// @박철진--------------------------------------------------------------여기까지
		// 수정

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
	// // 로그인 시 비밀번호 체크
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
	// // 계정 삭제 시 컨펌다이얼로그로 다시 한번 확인 후
	// int result = JOptionPane.showConfirmDialog(this, "do you really want to
	// delete?", "Question",
	// JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	// // YES_OPTION이면 비밀번호 확인 후 계정 삭제
	// if (result == JOptionPane.YES_OPTION) {
	// String password = JOptionPane.showInputDialog(this, "input your
	// password", "check password",
	// JOptionPane.INFORMATION_MESSAGE);
	// }
	// }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		// Add 버튼 이벤트 처리, 가입창으로 넘어감
		if (src == btnAdd) {
			setVisible(false);
			new JoinDlg(this);
		}
		// del 버튼 이벤트 처리, 계정삭제
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

	/////////////////// 0614 김정규 수정완료
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
