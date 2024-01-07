package me.ap.coffeeshop.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CoffeeOrder {

	@Id
	@GeneratedValue
	private long id;

	private LocalDateTime time;
	private LocalDateTime finishTime;

	@ManyToOne
	private CoffeeType coffeeType;

	@ManyToOne
	private Machine machine;

	private int tableNumber;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public CoffeeType getCoffeeType() {
		return coffeeType;
	}

	public void setCoffeeType(CoffeeType coffeeType) {
		this.coffeeType = coffeeType;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public LocalDateTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalDateTime finishTime) {
		this.finishTime = finishTime;
	}

	@Override
	public String toString() {
		return "CoffeeOrder [id=" + id + ", time=" + time + ", finishTime=" + finishTime + ", coffeeType=" + coffeeType
				+ ", machine=" + machine + ", tableNumber=" + tableNumber + "]";
	}

}
