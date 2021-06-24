package kr.ac.green;

import java.io.Serializable;

public class User implements Serializable {
	private String name;
	private String pw;
	
	// �̸����� �ش� ���� Ȯ���ϱ� ���� ������
	public User(String name) {
		setName(name);
	}
	
	public User(String name, String pw) {
		setName(name);
		setPw(pw);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	// �̸����� ���� ã�� ���� equals
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof User)) {
			return false;
		}
		User user = (User)obj;
		return name.equals(user.getName());
	}
}
