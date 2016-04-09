package de.jenswurm.assertifier.testbeans;

import java.math.BigDecimal;
import java.util.Date;

public class Purchase {

	private int id;
	private Date date;
	private int numberOfUnits;
	private Product product;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getNumberOfUnits() {
		return numberOfUnits;
	}
	public void setNumberOfUnits(int numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	

}
