package me.mrCookieSlime.Slimefun.Objects;

public class Charge {
	
	double charge, capacity;
	
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
