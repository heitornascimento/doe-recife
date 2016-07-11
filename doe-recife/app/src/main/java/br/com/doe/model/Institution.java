package br.com.doe.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;

/**
 * @author heitornsouza Copyright 2013. All rights reserved
 */
public class Institution implements Parcelable, ClusterItem {

	private String address;
	private String cidade;
	private String cnpj;
	private String codigo;
	private String donation_preference;
	private String email;
	private String fax;
	private String fone;
	private String razao_social;
	private String rd;
	private int id;
	private List<Donation> donationList = new ArrayList<Donation>();

	private String latitude;
	private String longitude;

	public int index;

	public String getLat() {
		return latitude;
	}

	public void setLat(String lat) {
		this.latitude = lat;
	}

	public String getLgn() {
		return longitude;
	}

	public void setLgn(String lgn) {
		this.longitude = lgn;
	}

	public List<Donation> getDonationList() {
		return donationList;
	}

	public void setDonationList(Donation donation) {
		this.donationList.add(donation);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRazao_social() {
		return razao_social;
	}

	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}

	public String getRd() {
		return rd;
	}

	public void setRd(String rd) {
		this.rd = rd;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDonation_preference() {
		return donation_preference;
	}

	public void setDonation_preference(String donation_preference) {
		this.donation_preference = donation_preference;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public static Parcelable.Creator<Institution> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.address);
		dest.writeString(this.cidade);
		dest.writeString(this.cnpj);
		dest.writeString(this.codigo);
		dest.writeString(this.donation_preference);
		dest.writeString(this.email);
		dest.writeString(this.fax);
		dest.writeString(this.fone);
		dest.writeString(this.razao_social);
		dest.writeString(this.rd);
		dest.writeInt(this.id);
		dest.writeList(this.donationList);
		dest.writeString(this.latitude);
		dest.writeString(this.longitude);
	}

	public static final Parcelable.Creator<Institution> CREATOR = new Creator<Institution>() {

		@Override
		public Institution[] newArray(int size) {
			return new Institution[size];
		}

		@Override
		public Institution createFromParcel(Parcel source) {

			Institution institution = new Institution();
			institution.address = source.readString();
			institution.cidade = source.readString();
			institution.cnpj = source.readString();
			institution.codigo = source.readString();
			institution.donation_preference = source.readString();
			institution.email = source.readString();
			institution.fax = source.readString();
			institution.fone = source.readString();
			institution.razao_social = source.readString();
			institution.rd = source.readString();
			institution.id = source.readInt();
			institution.donationList = source.readArrayList(Donation.class
					.getClassLoader());
			institution.latitude = source.readString();
			institution.longitude = source.readString();

			return institution;
		}
	};

	public boolean equals(Object o) {

		Institution instObj = (Institution) o;

		if (instObj.getId() == this.getId()) {
			return true;
		} else {
			return false;
		}

	}

	;

	@Override
	public LatLng getPosition() {
		return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
	}
}
