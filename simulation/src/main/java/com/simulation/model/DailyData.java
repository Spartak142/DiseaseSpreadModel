package com.simulation.model;

public class DailyData {
	public int gotSickToday;
	public int gotCuredToday;
	public int diedToday;
	public int totalSickEndDay;

	public DailyData() {
		this.gotSickToday=0;
		this.gotCuredToday=0;
		this.diedToday=0;
		this.totalSickEndDay=0;
	}
	public void oneMoreCured() {
		this.gotCuredToday++;
	}
	public void oneMoreSick() {
		this.gotSickToday++;
	}
	public void oneMoreDead() {
		this.diedToday++;
	}
	
	@Override
	public String toString() {
		  return "DailyData{" +
	                "gotSickToday=" + gotSickToday +
	                ", gotCuredToday=" + gotCuredToday +
	                ", diedToday=" + diedToday +
	                ", totalSickEndDay=" + totalSickEndDay +
	                '}';
	    
	}
	
}
