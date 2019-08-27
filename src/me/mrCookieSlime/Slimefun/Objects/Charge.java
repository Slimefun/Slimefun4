package me.mrCookieSlime.Slimefun.Objects;

public class Charge {
	
	private double energy;
	private double capacity;
	
	public Charge(double energy, double capacity) {
		this.energy = energy;
		this.capacity = capacity;
	}
	
	public double getStoredEnergy() {
		return energy;
	}
	
	public double getCapacity() {
		return capacity;
	}

}
