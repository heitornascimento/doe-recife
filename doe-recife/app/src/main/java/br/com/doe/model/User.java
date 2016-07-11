package br.com.doe.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heitornsouza Copyright 2013. All rights reserved
 */
public class User extends BaseObservable implements Parcelable {

	/*
	 * Some attributes are not in the camel case because of GSON converting
	 * match.
	 */
	private String email;
	private String name;
	private int id;
	private String mAuthToken;
	private int mDonationAmount;
	private String donations_preference;
	private List<Donation> donationList = new ArrayList<Donation>();
	private List<Institution> institutionList = new ArrayList<Institution>();


    private String password;

    public User(){

    }

	public User(String email, String password){
        this.email = email;
        this.password = password;
	}

    public void setPassword(String password){
        this.password = password;
    }

    @Bindable
    public String getPassword(){
        return password;
    }

	public List<Donation> getDonationList() {
		return donationList;
	}

	public void setDonationList(Donation donation) {
		this.donationList.add(donation);
	}

	public List<Institution> getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(Institution inst) {
		this.institutionList.add(inst);
	}

    @Bindable
	public String getEmail() {
		return email;
	}

	public void setEmail(String mEmail) {
		this.email = mEmail;
	}

	public String getName() {
		return name;
	}

	public void setmName(String mName) {
		this.name = mName;
	}

	public int getmId() {
		return id;
	}

	public void setmId(int mId) {
		this.id = mId;
	}

	public String getAuthToken() {
		return mAuthToken;
	}

	public void setmAuthToken(String mAuthToken) {
		this.mAuthToken = mAuthToken;
	}

	public int getDonationAmount() {
		return mDonationAmount;
	}

	public void setmDonationAmount(int mDonationAmount) {
		this.mDonationAmount = mDonationAmount;
	}

	public String getDonationsPreference() {
		return donations_preference;
	}

	public void setmDonationsPreference(String mDonationsPreference) {
		this.donations_preference = mDonationsPreference;
	}

	public static Parcelable.Creator<User> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(email);
		dest.writeString(name);
		dest.writeInt(id);
		dest.writeString(mAuthToken);
		dest.writeInt(mDonationAmount);
		dest.writeString(donations_preference);
		dest.writeList(this.donationList);
		dest.writeList(this.institutionList);
	}

	public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}

		@Override
		public User createFromParcel(Parcel source) {

			User user = new User();
			user.email = source.readString();
			user.name = source.readString();
			user.id = source.readInt();
			user.mAuthToken = source.readString();
			user.mDonationAmount = source.readInt();
			user.donations_preference = source.readString();
			user.donationList = source.readArrayList(User.class
					.getClassLoader());
			user.institutionList = source.readArrayList(User.class
					.getClassLoader());

			return user;
		}
	};

}
