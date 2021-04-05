package com.simulation;

import com.simulation.service.SimulationService;
import com.simulation.ui.UserInputHandling;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.simulation.model.City;
import com.simulation.model.ExcelData;
import com.simulation.model.HealthStatus;
import com.simulation.model.Person;

public class Main {

	public static void main(String[] args) {
		City salem;
		SimulationService simulationService;

		int[] seedList = defineSeedList();
		double[] probabilityList = defineProbabilityList();
		UserInputHandling ui = new UserInputHandling();
		ui.useDefaults();
		final int citySize = ui.userInputForCitySize();

		//Uncomment the line below to be able to set the probability of spreading yourself.
		// final double spreadingProbability = ui.userInputForSpreadingProbability();
		final double fatalProbability = ui.userInputForFatalProbability();
		final int minDays = ui.userInputForMinDays();
		final int maxDays = ui.userInputForMaxDays();
		ArrayList<ExcelData> excelData = new ArrayList<ExcelData>();
		final List<Person> initiallySick = ui.userInputForInitiallySick(citySize);

		for (int seed : seedList) {
			//Comment the line below to be able to use the user defined probability of spreading.
			for (double spreadingProbability : probabilityList) {
				System.out.println("New simulations starting!");
				for (Person person : initiallySick) {
					person.healthStatus = HealthStatus.SICK;
					person.sickDays = 0;
				}
				salem = new City(citySize, initiallySick);
				simulationService = new SimulationService(spreadingProbability, fatalProbability, minDays, maxDays,
						seed);
				runSimulationService(salem, simulationService, excelData);
		//Comment the line below to be able to use the user defined probability of spreading.
			}
		}
		// Write exceldata to excel.
		writeToExcel(excelData);
	}

	private static int[] defineSeedList() {
		int[] seedList = new int[5];
		seedList[0] = 7;
		seedList[1] = 2;
		seedList[2] = 3;
		seedList[3] = 4;
		seedList[4] = 5;
//		seedList[5] = 6;
//		seedList[6] = 7;
//		seedList[7] = 8;
//		seedList[8] = 9;

		return seedList;
	}

	private static double[] defineProbabilityList() {
		double[] seedList = new double[19];
		seedList[0] = 0.01;
		seedList[1] = 0.02;
		seedList[2] = 0.03;
		seedList[3] = 0.04;
		seedList[4] = 0.05;
		seedList[5] = 0.06;
		seedList[6] = 0.07;
		seedList[7] = 0.08;
		seedList[8] = 0.09;
		seedList[9] = 0.1;
		seedList[10] = 0.2;
		seedList[11] = 0.3;
		seedList[12] = 0.4;
		seedList[13] = 0.5;
		seedList[14] = 0.6;
		seedList[15] = 0.7;
		seedList[16] = 0.8;
		seedList[17] = 0.9;
		seedList[18] = 1;
		return seedList;
	}

	private static void runSimulationService(City salem, SimulationService simulationService,
			ArrayList<ExcelData> excelData) {
		try {
			do {
				simulationService.simulateADay(salem, excelData);
			} while (salem.hasSickPeople);
		} catch (RuntimeException rte) {
			System.out.println(rte.getMessage());
			rte.printStackTrace();
		}
	}

	private static void writeToExcel(ArrayList<ExcelData> excelData) {
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet(" SimulationData ");

		// Create row object
		XSSFRow row;

		row = spreadsheet.createRow(1);
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("RandomSeedId");
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("SpreadProbability");
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("FatalProbability");
		Cell cell4 = row.createCell(4);
		cell4.setCellValue("MinSickDays");
		Cell cell5 = row.createCell(5);
		cell5.setCellValue("MaxSickDays");
		Cell cell6 = row.createCell(6);
		cell6.setCellValue("CitySize");
		Cell cell7 = row.createCell(7);
		cell7.setCellValue("AverageSickPerDay");
		Cell cell8 = row.createCell(8);
		cell8.setCellValue("AverageCuredPerDay");
		Cell cell9 = row.createCell(9);
		cell9.setCellValue("AverageFatalCasesPerDay");
		Cell cell10 = row.createCell(10);
		cell10.setCellValue("TotalDays");
		Cell cell11 = row.createCell(11);
		cell11.setCellValue("InitiallySick");
		Cell cell12 = row.createCell(12);
		cell12.setCellValue("totalCured");
		Cell cell13 = row.createCell(13);
		cell13.setCellValue("totalSick");
		Cell cell14 = row.createCell(14);
		cell14.setCellValue("totalDead");

		// Iterate over data and write to sheet
		Map<String, ExcelData> dataFromAllTheRuns = dataToExcel(excelData);

		Set<String> keyid = dataFromAllTheRuns.keySet();
		int rowid = 2;

		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			ExcelData simulationData = dataFromAllTheRuns.get(key);

			cell1 = row.createCell(1);
			cell1.setCellValue(simulationData.seed);
			cell2 = row.createCell(2);
			cell2.setCellValue(simulationData.spreadProbability);
			cell3 = row.createCell(3);
			cell3.setCellValue(simulationData.fatalProbability);
			cell4 = row.createCell(4);
			cell4.setCellValue(simulationData.minDays);
			cell5 = row.createCell(5);
			cell5.setCellValue(simulationData.maxDays);
			cell6 = row.createCell(6);
			cell6.setCellValue(simulationData.citySize);
			cell7 = row.createCell(7);
			cell7.setCellValue(simulationData.averageSick);
			cell8 = row.createCell(8);
			cell8.setCellValue(simulationData.averageCured);
			cell9 = row.createCell(9);
			cell9.setCellValue(simulationData.averageFatal);
			cell10 = row.createCell(10);
			cell10.setCellValue(simulationData.simulationDays);
			cell11 = row.createCell(11);
			cell11.setCellValue(simulationData.getInitiallySickAsAString());
			cell12 = row.createCell(12);
			cell12.setCellValue(simulationData.totalCured);
			cell13 = row.createCell(13);
			cell13.setCellValue(simulationData.totalSick);
			cell14 = row.createCell(14);
			cell14.setCellValue(simulationData.totalDied);
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File("C:\\Users\\Public\\Documents\\SimulationData.xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("SimulationData.xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Map<String, ExcelData> dataToExcel(ArrayList<ExcelData> excelData) {
		Map<String, ExcelData> data = new TreeMap<String, ExcelData>();

		int rowCount = 1;
		for (ExcelData simulationRun : excelData) {
			rowCount++;
			data.put(String.valueOf(rowCount), simulationRun);
		}
		return data;
	}

}
