package com.prs.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class LineItem {
	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="RequestID")
	private Request request;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ProductID")
	protected Product product;
	protected double quantity;

	public LineItem() {
		super();
	}

	public LineItem(int id, Request request, String productId, double quantity) {
		super();
		this.id = id;
		this.request = request;
		//this.product = product;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProductId() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	public double getTotal() {
		double total = product.getPrice() * quantity;
		return total;
	}

	@Override
	public String toString() {
		return "lineItem [id=" + id + ", request=" + request + ", productI=" + product + ", quantity=" + quantity
				+ "]";
	}

}
