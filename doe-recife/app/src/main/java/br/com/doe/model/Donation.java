package br.com.doe.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author heitornsouza Copyright 2013. All rights reserved
 */
public class Donation implements Parcelable {

	private int id;
	private int user_id;
	private int institution_id;
	private String donation_description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getInstitution_id() {
		return institution_id;
	}

	public void setInstitution_id(int institution_id) {
		this.institution_id = institution_id;
	}

	public String getDonation_description() {
		return donation_description;
	}

	public void setDonation_description(String donation_description) {
		this.donation_description = donation_description;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(this.donation_description);
		dest.writeInt(this.id);
		dest.writeInt(this.institution_id);
		dest.writeInt(this.user_id);

	}

	public static final Parcelable.Creator<Donation> CREATOR = new Creator<Donation>() {

		@Override
		public Donation[] newArray(int size) {
			return new Donation[size];
		}

		@Override
		public Donation createFromParcel(Parcel source) {

			Donation donation = new Donation();
			donation.donation_description = source.readString();
			donation.id = source.readInt();
			donation.institution_id = source.readInt();
			donation.user_id = source.readInt();

			return donation;
		}
	};

}
