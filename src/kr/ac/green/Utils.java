package kr.ac.green;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.Closeable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

public class Utils {
	// 폰트
	public static Font f1 = new Font("맑은 고딕", Font.BOLD, 18);
	public static Font f5 = new Font("맑은 고딕", Font.BOLD, 14);
	// 색상
	public static Color lightGrey = new Color(213, 213, 213);
	public static Color darkGrey = new Color(69,69,69);
	//넘김 버튼 이미지 객체
	public static ImageIcon left = new ImageIcon("Next.png");
	public static ImageIcon right = new ImageIcon("Pre.png");
	public static ImageIcon data = new ImageIcon("Data.png");
	// Button 배경색 흰색
	public static void setBtnWhite(JButton btn) {
		btn.setBackground(Color.WHITE);
		btn.setFont(f5);
		btn.setForeground(darkGrey);
	}
	
	private static final Dimension LABEL_SIZE = new Dimension(70, 25);
	
	// Button 색 변경
	public static void setBtnGrey(JButton btn) {
		btn.setBackground(darkGrey);
		btn.setBorderPainted(false);
		btn.setFont(f1);
		btn.setForeground(Color.WHITE);
	}
	
	//버튼 테두리 없애기, 배경색 지우기
	public static void setBtn(JButton btn) {
		btn.setBorder(null);
		btn.setBackground(null);
	}

	// Panel 배경색 흰색
	public static void setPnlWhite(JPanel pnl) {
		pnl.setOpaque(true);	
		pnl.setBackground(Color.WHITE);
	}
	// Label 배경색 흰색
	public static void setLblWhite(JLabel lbl) {
		lbl.setOpaque(true);
		lbl.setBackground(Color.WHITE);
	}
	
	//아이콘 설정
	public static void setIcon(JFrame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("diary.jpg");
		frame.setIconImage(img);
	}
	
	public static JLabel getLabel(String str) {		
		JLabel lbl = new JLabel(str, JLabel.LEFT);
		lbl.setPreferredSize(LABEL_SIZE);

		return lbl;
	}
	
	public static boolean isEmpty(JTextComponent input) {
		String text = input.getText().trim();
		return (text.length() == 0) ? true : false;
	}
	
	public static void closeAll(Closeable... list) {
		for (Closeable temp : list) {
			try {
				temp.close();
			} catch (Exception e) {}
		}
	}
}
