package me.ap.coffeeshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CoffeeType {

	@Id
	private long id;

	private String name;

	private int preparationTime;

	private int coffeeAmount;

	private double price;

	private String imagePath;

	public String getServletImagePath() {
		int start = imagePath.lastIndexOf("/");
		if (start < 0)
			return "";
		else {
			String s = "files";
			s = s.concat(imagePath.substring(start, imagePath.length()));
			return s;
		}
	}

	private boolean deleted;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(int preparationTime) {
		this.preparationTime = preparationTime;
	}

	public int getCoffeeAmount() {
		return coffeeAmount;
	}

	public void setCoffeeAmount(int coffeeAmount) {
		this.coffeeAmount = coffeeAmount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "CoffeeType [id=" + id + ", name=" + name + ", preparationTime=" + preparationTime + ", coffeeAmount="
				+ coffeeAmount + ", price=" + price + ", imagePath=" + imagePath + ", deleted=" + deleted + "]";
	}

}
