package me.ap.coffeeshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Machine {
	
	@Id
	private long id;
	
	private int capacity;
	
	public Machine() {
		super();
	}

	public Machine(long id, int capacity) {
		super();
		this.id = id;
		this.capacity = capacity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "Machine [id=" + id + ", capacity=" + capacity + "]";
	}

}
