package com.simulation.service;

import static com.simulation.model.HealthStatus.*;

import java.util.ArrayList;
import java.util.Random;

import com.simulation.model.City;
import com.simulation.model.DailyData;
import com.simulation.model.ExcelData;
import com.simulation.model.Person;

public class SimulationService {

	double probabilityOfSpreading;
	double probabilityOfDeath;
	int minSickDays;
	int maxSickDays;
	double probabilityOfCure;
	Random generator;
	int dayOfSimulation;
	int seed;

	public SimulationService(double probabilityOfSpreading, double probabilityOfDeath, int minSickDays, int maxSickDays,
			int seed) {
		this.probabilityOfSpreading = probabilityOfSpreading;
		this.probabilityOfDeath = probabilityOfDeath;

		if (minSickDays > maxSickDays) {
			this.minSickDays = maxSickDays;
			this.maxSickDays = minSickDays;
			System.out.println("MinDays is higher than maxDays, swapping the values between the two");
		} else {
			this.minSickDays = minSickDays;
			this.maxSickDays = maxSickDays;
		}

		this.probabilityOfCure = Math.pow(1 - probabilityOfDeath, maxSickDays) / (maxSickDays - minSickDays + 1);
		this.dayOfSimulation = 0;
		/**
		 * The part above is probability of survival. That probability is then divided
		 * by the days a person can be sick and become cured
		 */
		this.seed = seed;
		this.generator = new Random(seed);
	}

	public void simulateADay(City city, ArrayList<ExcelData> excelData) {
		System.out.println("Start of day: " + dayOfSimulation);
		DailyData dailyData = new DailyData();

		for (Person[] street : city.getPopulation()) {
			for (Person person : street) {
				person.hasBeenCountedIfSick = false;
				switch (person.healthStatus) {
				case SICK:
					simulateSpreading(person, city, dailyData);
					simulateHealth(person, city, dailyData);
					person.sickDays++;
				default:
					break;
				}
			}
		}
		city.accumulatedSick = city.accumulatedSick + dailyData.gotSickToday;
		city.accumulatedDead = city.accumulatedDead + dailyData.diedToday;
		city.accumulatedCured = city.accumulatedCured + dailyData.gotCuredToday;
		if (city.accumulatedDead > city.accumulatedSick) {
			throw new RuntimeException("There are more dead than there were sick in the city");
		}
		if (dailyData.totalSickEndDay == 0) {
			city.hasSickPeople = false;
		}
		if (!city.hasSickPeople) {
			runFinished(city, excelData);
		}
		System.out.println("Day of simulation: " + dayOfSimulation + " " + dailyData.toString());
		System.out
				.println("AccumulatedDead: " + city.accumulatedDead + " " + "AccumulatedSick: " + city.accumulatedSick);
		//System.out.println(city.toString());
		dayOfSimulation++;
	}

	private void runFinished(City city, ArrayList<ExcelData> excelData) {

		double averageDeaths = (Double.valueOf(city.accumulatedDead)) / dayOfSimulation;
		double averageSick = (Double.valueOf(city.accumulatedSick)) / dayOfSimulation;
		double averageCured = (Double.valueOf(city.accumulatedCured)) / dayOfSimulation;

		System.out.println("This disease has caused an epidemy: " + city.hasHadEpidemy());
		System.out.println("Average deaths per day: " + averageDeaths);
		System.out.println("Average sickness spreads per day: " + averageSick);
		System.out.println("Average cures per day: " + averageCured);
		
		excelData.add(new ExcelData(seed, city.size, averageSick, averageCured, averageDeaths, probabilityOfSpreading,
				probabilityOfDeath, minSickDays, maxSickDays, city.initiallySick, city.accumulatedCured,
				city.accumulatedDead, city.accumulatedSick, dayOfSimulation));

	}

	private void simulateSpreading(Person person, City city, DailyData dailyData) {
		if (person.sickDays == 0) {
			countSickIfNotCounted(person, city, dailyData);
			return;
		}
		if (person.sickDays == maxSickDays) {
			// The person will either die or become healthy hence nothing is needed.
			return;
		}
		for (Person neighbour : city.getNeighbours(person.verticalLocation, person.horisontalLocation)) {
			if (neighbour.healthStatus.equals(HEALTHY)) {
				if (probabilityOfSpreading >= generator.nextDouble()) {
					dailyData.oneMoreSick();
					neighbour.healthStatus = SICK;
					countSickIfNotCounted(neighbour, city, dailyData);
				}
			}
		}
	}

	private void simulateHealth(Person person, City city, DailyData dailyData) {
		if (person.sickDays == 0) {
			return;
		}
		if (person.sickDays < minSickDays) {
			if (probabilityOfDeath >= generator.nextDouble()) {
				dailyData.oneMoreDead();
				person.healthStatus = DEAD;
			} else {
				countSickIfNotCounted(person, city, dailyData);
			}
		}
		if (person.sickDays >= minSickDays && person.sickDays < maxSickDays) {
			Double outcome = generator.nextDouble();
			if (probabilityOfCure >= outcome) {
				dailyData.oneMoreCured();
				person.healthStatus = CURED;
			} else if (probabilityOfCure + probabilityOfDeath >= outcome) {
				dailyData.oneMoreDead();
				person.healthStatus = DEAD;
			} else {
				countSickIfNotCounted(person, city, dailyData);
			}
		}
		if (person.sickDays == maxSickDays) {
			// check if he dies or cures.
			if (probabilityOfDeath >= generator.nextDouble()) {
				dailyData.oneMoreDead();
				person.healthStatus = DEAD;
			} else {
				person.healthStatus = CURED;
				dailyData.oneMoreCured();
			}
		}
	}

	private void countSickIfNotCounted(Person person, City city, DailyData dailyData) {
		if (!person.hasBeenCountedIfSick) {
			dailyData.totalSickEndDay++;
			person.hasBeenCountedIfSick = true;
			city.hasSickPeople = true;
		}
	}
}
