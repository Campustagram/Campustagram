package com.campustagram.core.demo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * created on 2018/09/28
 * 
 * @author Ahmet ŞEN
 *
 */
@Entity
public class Car implements Serializable {

	@Id
	@SequenceGenerator(name = "car_seq", sequenceName = "car_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_seq")
	private Long id;
	private String brand;
	private int year;
	private String color;
	private int price;
	private boolean sold;

	public Car() {
	}

	public Car(String brand, int year, String color) {
		this.brand = brand;
		this.year = year;
		this.color = color;
	}

	public Car(String brand, int year, String color, int price, boolean sold) {
		this.brand = brand;
		this.year = year;
		this.color = color;
		this.price = price;
		this.sold = sold;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Car other = (Car) obj;
		if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

}