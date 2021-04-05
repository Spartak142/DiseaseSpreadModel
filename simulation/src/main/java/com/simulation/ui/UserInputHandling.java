package com.simulation.ui;

import static com.simulation.model.HealthStatus.SICK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.simulation.model.Person;

public class UserInputHandling {

	private boolean useDefaults;

	public UserInputHandling() {
		useDefaults = false;
	}

	public void useDefaults() {
		System.out.println("This simulation requires several parameters to be set. Press 'enter' to continue.");
		System.out.println("To skip setting the values and use the default type 'default' and press 'enter'");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		if (input.equalsIgnoreCase("default")) {
			useDefaults = true;
			System.out.println("The default values will be used");
		}
	}

	public List<Person> userInputForInitiallySick(int citySize) {
		List<Person> initiallySick = new ArrayList<>();
		;
		if (!useDefaults) {
			try {
				Scanner in = new Scanner(System.in);
				System.out.println(
						"Enter where intially sick people are located. For each person enter vertical and horizontal locations separated by ':' Example: 1:2");
				System.out
						.println("Enter all the people in one line separated by a single space, Example: 1:2 1:3 3:1");
				System.out.println(
						"Note that city locations are between 0:0 and " + (citySize - 1) + ":" + (citySize - 1));
				String locationOfInitiallySick = in.nextLine();
				String[] locations = locationOfInitiallySick.split(" ");
				for (String s : locations) {
					String[] coordinates = s.split(":");
					int vertical = Integer.parseInt(coordinates[0]);
					int horizontal = Integer.parseInt(coordinates[1]);
					if (vertical >= citySize || horizontal >= citySize) {
						System.out
								.println("The suggested location {vertical=" + vertical + "}, horizontal={" + horizontal
										+ "} is outside of the city. Locations within the city are between 0:0 and "
										+ (citySize - 1) + ":" + (citySize - 1));
						System.out.println();
						return userInputForInitiallySick(citySize);
					} else {
						initiallySick.add(new Person(vertical, horizontal));
					}
				}
				return initiallySick;
			} catch (Exception e) {
				System.out.println("Please use the correct format for initial locations! Example: 0:0 1:2 2:1");
				return userInputForInitiallySick(citySize);
			}
		}
		return Arrays.asList(new Person(0, 0, SICK));
	}

	public int userInputForCitySize() {
		if (!useDefaults) {
			try {
				Scanner in = new Scanner(System.in);
				System.out.println("Enter the size of the City");
				System.out.println("");
				int citySize = in.nextInt();
				if (citySize <= 0) {
					System.out.println("The size of a city population has to be above 0");
					return userInputForCitySize();
				} else {
					return citySize;
				}
			} catch (Exception e) {
				System.out.println("Please enter a valid integer");
				return userInputForCitySize();
			}
		}
		System.out.println(
				"The defaults are: citySize= 5, spreadProbability=0,5, fatalProbability=0,1, minSickDays=2, maxSickDays=4");
		System.out.println("There is only one sick person by default placed in coordinates 0:0");
		return 5;
	}

	public double userInputForSpreadingProbability() {
		if (!useDefaults) {
			try {
				Scanner in = new Scanner(System.in);
				System.out.println(
						"Enter the spread probabiity of a desease (per day), values between 0 and 1 separate decimals by ,");
				System.out.println("");
				double spreadingProbability = in.nextDouble();
				if (0 <= spreadingProbability && spreadingProbability <= 1) {
					return spreadingProbability;
				} else {
					System.out.println(
							"The provided probability is out of range of normal probability values between 0 and 1");
					return userInputForSpreadingProbability();

				}
			} catch (Exception e) {
				System.out.println("Please enter a valid double");
				return userInputForSpreadingProbability();
			}
		}
		return Double.valueOf(0.5);
	}

	public double userInputForFatalProbability() {
		if (!useDefaults) {
			try {
				Scanner in = new Scanner(System.in);

				System.out.println(
						"Enter the fatal probability (per day), values between 0 and 1 separate decimals by ,");
				System.out.println("");
				double fatalProbability = in.nextDouble();
				if (0 <= fatalProbability && fatalProbability <= 1) {
					return fatalProbability;
				} else {
					System.out
							.println("The provided probability is out of range of normal probability between 0 and 1");
					return userInputForFatalProbability();

				}
			} catch (Exception e) {
				System.out.println("Please enter a valid probability");
				return userInputForFatalProbability();
			}
		}
		return Double.valueOf(0.1);
	}

	public int userInputForMinDays() {
		if (!useDefaults) {
			try {
				Scanner in = new Scanner(System.in);

				System.out.println("Enter the minimum number of days a person can be sick");
				System.out.println("");
				int minDays = in.nextInt();
				if (minDays <= 0) {
					System.out.println("The minimum number of days a person can be sick has to be above 0");
					return userInputForMinDays();

				} else {
					return minDays;
				}
			} catch (Exception e) {
				System.out.println("Please enter a valid number of days");
				return userInputForMinDays();
			}
		}
		return 2;
	}

	public int userInputForMaxDays() {
		if (!useDefaults) {
			try {
				Scanner in = new Scanner(System.in);

				System.out.println("Enter the maximum number of days a person can be sick");
				System.out.println("");
				int maxDays = in.nextInt();
				if (maxDays <= 0) {
					System.out.println("The maximum number of days a person can be sick has to be above 0.");
					return userInputForMaxDays();

				} else {
					return maxDays;
				}
			} catch (Exception e) {
				System.out.println("Please enter a valid number of days");
				return userInputForMaxDays();
			}
		}
		return 4;
	}
}
