package me.mrCookieSlime.Slimefun.Objects;

public class Charge {
	
	private double charge;
	private double capacity;
	
	public Charge(double charge, double capacity) {
		this.charge = charge;
		this.capacity = capacity;
	}
	
	public double getStoredEnergy() {
		return charge;
	}
	
	public double getCapacity() {
		return capacity;
	}

}
