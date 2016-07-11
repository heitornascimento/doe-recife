package br.com.doe.model;

/**
 * @author heitornsouza Copyright 2013. All rights reserved
 */
public class UserDonation {

	private String type;
	private int count;

	private int index;

	public UserDonation(String type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public boolean equals(Object o) {

		UserDonation ud = (UserDonation) o;

		if (ud.getType().equals(this.type)) {
			return true;
		} else {
			return false;
		}

	}

}
