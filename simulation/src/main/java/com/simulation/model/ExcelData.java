package com.simulation.model;

import java.util.List;

public class ExcelData {
	public int seed;
	public double spreadProbability;
	public double fatalProbability;
	public int minDays;
	public int maxDays;
	public int citySize;
	public int totalCured;
	public int totalDied;
	public int totalSick;
	public int simulationDays;

	public double averageSick;
	public double averageCured;
	public double averageFatal;
	public List<Person> initiallySick;

	public ExcelData(int seed, int citySize, double averageSick, double averageCured, double averageFatal,
			double spreadProbability, double fatalProbability, int minDays, int maxDays, List<Person> initiallySick,
			int totalCured, int totalDied, int totalSick, int simulationDays) {
		this.seed = seed;
		this.citySize = citySize;
		this.averageSick = averageSick;
		this.averageCured = averageCured;
		this.averageFatal = averageFatal;
		this.spreadProbability = spreadProbability;
		this.fatalProbability = fatalProbability;
		this.minDays = minDays;
		this.maxDays = maxDays;
		this.initiallySick = initiallySick;
		this.totalCured = totalCured;
		this.totalDied = totalDied;
		this.totalSick = totalSick;
		this.simulationDays = simulationDays;
	}

	public String getInitiallySickAsAString() {
		StringBuilder result = new StringBuilder();

		for (Person person : this.initiallySick) {
			result.append("(" + person.verticalLocation + " : " + person.horisontalLocation + ")");
		}
		return result.toString();
	}

}
