package com.simulation.model;

import java.util.ArrayList;
import java.util.List;

import static com.simulation.model.HealthStatus.*;

public class City {
	public int size;
	Person[][] population;
	public boolean hasSickPeople;
	public int accumulatedSick;
	public int accumulatedDead;
	public int accumulatedCured;
	public List<Person> initiallySick;

	public City(int size, List<Person> initiallySick) {

		if (size < 1) {
			throw new IllegalArgumentException("The size is too small for the city");
		}
		if (size == 1) {
			System.out.println("The population of the city is 1.");
		}
		this.size = size;
		this.accumulatedDead = 0;
		this.accumulatedSick = 0;
		this.accumulatedCured = 0;
		this.initiallySick = initiallySick;
		hasSickPeople = initiallySick.isEmpty() ? false : true;
		definePopulation(size, initiallySick);
		System.out.println("Defining new city");
		System.out.println(this.toString());

	}

	// Initilize the population, based on the provided parameters
	private void definePopulation(int size, List<Person> initiallySick) {
		population = new Person[size][size];
		for (int n = 0; n < size; n++) {
			for (int y = 0; y < size; y++) {
				population[n][y] = new Person(n, y, HEALTHY);
			}
		}
		addInitiallySick(initiallySick);
	}

	private void addInitiallySick(List<Person> initiallySick) {
		for (Person sickCitizen : initiallySick) {
			population[sickCitizen.horisontalLocation][sickCitizen.verticalLocation] = sickCitizen;
			this.accumulatedSick++;
		}
	}

	@Override
	public String toString() {
		StringBuilder cityRow = new StringBuilder();
		for (int n = 0; n < size; n++) {
			for (Person person : population[n]) {
				cityRow.append(healthStatusPrintOut(person.healthStatus.toString()));
			}
			cityRow.append("\n");
		}
		return cityRow.toString();
	}

	public Person[][] getPopulation() {
		return population;
	}

	public boolean hasSickPeople() {
		return hasSickPeople;
	}

	public boolean hasHadEpidemy() {
		int populationSize = this.size * this.size;
		if ((populationSize / 2) < accumulatedSick) {
			return true;
		}
		return false;
	}

	public List<Person> getNeighbours(int vert, int row) {

		List<Person> neighbors = new ArrayList<Person>();
		if (size == 1) {
			return neighbors;
		}
		// Add all neighbors from a higher row if exist
		if (vert + 1 < size) {
			neighbors.add(population[vert + 1][row]);
			if (row + 1 < size) {
				neighbors.add(population[vert + 1][row + 1]);
			}
			if (row - 1 >= 0) {
				neighbors.add(population[vert + 1][row - 1]);
			}
		}

		// Add all neighbors from a below row if exist
		if (vert - 1 >= 0) {
			neighbors.add(population[vert - 1][row]);
			if (row + 1 < size) {
				neighbors.add(population[vert - 1][row + 1]);
			}
			if (row - 1 >= 0) {
				neighbors.add(population[vert - 1][row - 1]);
			}
		}

		// Add left and right neighbors if present
		if (row + 1 < size) {
			neighbors.add(population[vert][row + 1]);
		}
		if (row - 1 >= 0) {
			neighbors.add(population[vert][row - 1]);
		}
		return neighbors;
	}

	// Printout the citizens Health Status
	private String healthStatusPrintOut(String healthStatus) {
		switch (healthStatus) {
		case "HEALTHY":
			return " [H]";
		case "SICK":
			return " [S]";
		case "CURED":
			return " [C]";
		case "DEAD":
			return " [D]";
		default:
			return "UNKNOWN";
		}
	}
}
