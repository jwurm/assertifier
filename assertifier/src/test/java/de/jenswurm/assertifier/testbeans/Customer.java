package de.jenswurm.assertifier.testbeans;

import java.util.ArrayList;
import java.util.List;

public class Customer {

	private int id;

	private String givenName;

	private String surname;

	private List<Purchase> purchases = new ArrayList<Purchase>();

	public Customer(String givenName, String surName) {
		this.givenName = givenName;
		this.surname = surName;
	}

	public String getGivenName() {
		return givenName;
	}

	public int getId() {
		return id;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public String getSurname() {
		return surname;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public void setId(int id) {
		this.id = id;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	

}
