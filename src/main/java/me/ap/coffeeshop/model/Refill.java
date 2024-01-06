package me.ap.coffeeshop.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Refill {
	
	@Id
	@GeneratedValue
	private long id;
	
	private LocalDateTime time;
	
	@ManyToOne
	private Machine machine;

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

	@Override
	public String toString() {
		return "Refill [id=" + id + ", time=" + time + ", machine=" + machine + "]";
	}
	
}
