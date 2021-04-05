package com.simulation.model;

public class Person {
	public int verticalLocation;
	public int horisontalLocation;
	public HealthStatus healthStatus;
	public int sickDays;
	public boolean hasBeenCountedIfSick;

	public Person(int verticalLocation, int horisontalLocation) {
		this(verticalLocation, horisontalLocation, HealthStatus.SICK);
	}

	public Person(int verticalLocation, int horisontalLocation, HealthStatus healthStatus) {
		this.verticalLocation = verticalLocation;
		this.horisontalLocation = horisontalLocation;
		this.healthStatus = healthStatus;
		this.sickDays = 0;
		this.hasBeenCountedIfSick = false;

	}

	@Override
	public String toString() {
		return "Person{" + "verticalLocation=" + verticalLocation + ", horisontalLocation=" + horisontalLocation
				+ ", healthStatus=" + healthStatus + ", sickDays=" + sickDays + '}';
	}
}
