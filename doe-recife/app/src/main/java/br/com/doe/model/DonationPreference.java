package br.com.doe.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author heitornsouza Copyright 2013. All rights reserved
 */
@SuppressLint("ParcelCreator")
public class 	DonationPreference implements Parcelable {

	public String brinquedos;
	public String cesta_basica;
	public String created_at;
	public String id;
	public String material_de_higiene_pessoal;
	public String material_de_limpeza;
	public String material_pedagogico;
	public String outros;
	public String recursos_financeiros;
	public String remedio;
	public String toalha_de_banho;
	public String toalha_de_mesa;
	public String updated_at;

	@SuppressLint("ParcelCreator")
	public DonationPreference() {
	}

	@SuppressLint("ParcelCreator")
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(brinquedos);
		dest.writeString(cesta_basica);
		dest.writeString(created_at);
		dest.writeString(id);
		dest.writeString(material_de_higiene_pessoal);
		dest.writeString(material_de_limpeza);
		dest.writeString(material_pedagogico);
		dest.writeString(outros);
		dest.writeString(recursos_financeiros);
		dest.writeString(remedio);
		dest.writeString(toalha_de_banho);
		dest.writeString(toalha_de_mesa);
		dest.writeString(updated_at);
	}

	public static final Parcelable.Creator<DonationPreference> CREATOR = new Creator<DonationPreference>() {

		public DonationPreference createFromParcel(Parcel source) {
			DonationPreference dp = new DonationPreference();
			dp.brinquedos = source.readString();
			dp.cesta_basica = source.readString();
			dp.created_at = source.readString();
			dp.id = source.readString();
			dp.material_de_higiene_pessoal = source.readString();
			dp.material_de_limpeza = source.readString();
			dp.material_pedagogico = source.readString();
			dp.outros = source.readString();
			dp.recursos_financeiros = source.readString();
			dp.remedio = source.readString();
			dp.toalha_de_banho = source.readString();
			dp.toalha_de_mesa = source.readString();
			dp.updated_at = source.readString();

			return dp;
		}

		@Override
		public DonationPreference[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DonationPreference[size];
		}
	};

}
