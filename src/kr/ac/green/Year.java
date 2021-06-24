// Year에서 다시 Month로 돌아가는 방법이 있어야함(어떤식으로 해야할까?)

package kr.ac.green;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Year extends JDialog implements ActionListener {

	private JLabel lblYear;

	private JButton btnPreYear;
	private JButton btnNextYear;

	private MonthPanel[] calandars;
	private int year;

	private MyCalendar owner;

	private JPanel pnl;

	public Year(MyCalendar owner, int year) {
		super(owner);
		this.owner = owner;
		this.year = year;
		init();
		setDisplay();
		addListener();
		showFrame();
	}

	private void init() {

		lblYear = new JLabel(String.valueOf(year), JLabel.CENTER);
		lblYear.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		// @박철진 Button 이미지 변경
		btnPreYear = new JButton(Utils.left);
		btnNextYear = new JButton(Utils.right);

		Utils.setBtnWhite(btnPreYear);
		Utils.setBtn(btnPreYear);
		Utils.setBtnWhite(btnNextYear);
		Utils.setBtn(btnNextYear);

		calandars = new MonthPanel[12];
		for (int i = 0; i < 12; i++) {
			calandars[i] = new MonthPanel(year, i + 1, owner.getMap(), owner);
			calandars[i].daysVisible(false);
			DayPanel[] days = calandars[i].getDays();
			for(DayPanel day : days){
				day.addMouseListener(new MouseAdapter() {
					// 이거 완전 수정 해야함 엄청 수정해야함 그냥 새로 생각해야함
					@Override
					public void mouseClicked(MouseEvent me) {
						if (day == me.getSource()) {
							setEnabled(false);
							new ToDoListDlg(day, Year.this);
//							setEnabled(false);
//							calandars[num].setMonth(calandars[num].getMonth() + 1);
//							owner.setMyCalrendar(calandars[num]);
//							owner.reSet();
//							
//							owner.setEnabled(true);
//							dispose();
						} 
					}
				});
				
				
			}
			
			
			
		}

	}

	private void setDisplay() {

		JPanel pnlCenter = new JPanel(new GridLayout(4, 3));

		for (int i = 0; i < calandars.length; i++) {
			JPanel pnlTemp = new JPanel(new BorderLayout());
			pnlTemp.setBackground(Color.WHITE);
			JLabel lbl = new JLabel((i + 1) + "월", JLabel.CENTER);
			lbl.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

			int num = i;
			lbl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent me) {
					if (lbl == me.getSource()) {
						lbl.setBorder(new LineBorder(Color.GRAY, 5));
					}
				}

				@Override
				public void mouseExited(MouseEvent me) {
					if (lbl == me.getSource()) {
						lbl.setBorder(new EmptyBorder(0, 0, 0, 0));
					}
				}

				// 이거 완전 수정 해야함 엄청 수정해야함 그냥 새로 생각해야함
				@Override
				public void mouseClicked(MouseEvent me) {
					if (lbl == me.getSource()) {
						calandars[num].setMonth(calandars[num].getMonth() + 1);
						owner.setMyCalrendar(calandars[num]);
						owner.reSet();
						
						owner.setEnabled(true);
						dispose();
					} 
				}
			});

			pnlTemp.add(lbl, BorderLayout.NORTH);
			pnlTemp.add(calandars[i]);
			pnlCenter.add(pnlTemp);
		}

		JPanel pnlNorth = new JPanel();
		pnlNorth.add(btnPreYear);
		pnlNorth.add(lblYear);
		pnlNorth.add(btnNextYear);
		// @박철진 pnlNorth 배경색 흰색 변경
		Utils.setPnlWhite(pnlNorth);

		pnl = new JPanel(new BorderLayout());
		pnl.add(pnlNorth, BorderLayout.NORTH);
		pnl.add(pnlCenter);
		add(pnl);
		// add(pnlNorth, BorderLayout.NORTH);
		// add(pnlCenter, BorderLayout.CENTER);
	}

	// 수정해줘야함(김정규)
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		// if(year == 2020 && month == 1){
		// JOptionPane.showConfirmDialog(this, "2020년 보다 전으로 돌아 갈수 없습니다.", "경고",
		// JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE);
		// } else {
		// month--;
		// if (month < 1) {
		// month = 12;
		// year--;
		// }
		// }

		// int year = Integer.parseInt(lblYear.getText());
		if (src == btnPreYear) {
			if (year == 2020) {
				JOptionPane.showConfirmDialog(this, "2020년 보다 전으로 돌아 갈수 없습니다.", "경고", JOptionPane.YES_OPTION,
						JOptionPane.ERROR_MESSAGE);
			} else {
				year--;
			}
		} else if (src == btnNextYear) {
			if (year == 2025) {
				JOptionPane.showConfirmDialog(this, "2026년은 안옵니다.", "경고", JOptionPane.YES_OPTION,
						JOptionPane.ERROR_MESSAGE);
			} else {
				year++;
			}

		}

		lblYear.setText(String.valueOf(year));
		pnl.removeAll();
		init();
		setDisplay();
		addListener();
		pnl.updateUI();

	}

	public void addListener() {
		btnNextYear.addActionListener(this);
		btnPreYear.addActionListener(this);
		
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				owner.setEnabled(true);
				dispose();
			}
		});

		for (MonthPanel calendar : calandars) {
			calendar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent me) {
					if (calendar == me.getSource()) {
						calendar.setBorder(new LineBorder(Color.GRAY, 5));
					}
				}

				@Override
				public void mouseExited(MouseEvent me) {
					if (calendar == me.getSource()) {
						calendar.setBorder(new EmptyBorder(0, 0, 0, 0));
					}
				}

				// 이거 완전 수정 해야함 엄청 수정해야함 그냥 새로 생각해야함
				@Override
				public void mouseClicked(MouseEvent me) {
					if (calendar == me.getSource()) {
						calendar.setMonth(calendar.getMonth() + 1);
						owner.setMyCalrendar(calendar);
						owner.reSet();
						owner.setEnabled(true);
						dispose();
					}

				}
			});
		}
	}

	private void showFrame() {
		setTitle("Year");
		setSize(800, 800);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	// public static void main(String[] args){
	// new Year();
	// }

}
