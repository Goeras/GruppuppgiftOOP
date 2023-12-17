package application;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GamePlay {

	// Attribut
	String startTime; // Huvudsaklig starttid. (masstart eller första startande)
	String startType;
	List<Competitor> competitorList = new ArrayList<>();
	
	// Konstruktorer
	public GamePlay() {
	}
	public GamePlay(String startType, List<Competitor> competitorList) {
		this.startType = startType;
		this.competitorList = competitorList;
	}
	
	// Metoder
	public void setFinnishTimes()
	{
		for(Competitor c : competitorList)
		{
			c.setFinnishTime(c.getLastPartTime());
		}
	}
	
	public void startTimers() // Anropade aldrig objektets metod, fungerar att anropa direkt i main istället.
	{
		int startNumber = 1;
		for (Competitor c : competitorList)
		{
			c.setStartNumber(startNumber);
			startNumber++;
			c.setStartTime(LocalTime.now());
			//c.addTime(c.getStartTime()); // Lägger till objektets starttid i objektets timesList.
			//c.addTimeStamp(); // lägger till en LocalTime.now() för varje objekt.
		}
	}
	
	public void setObjektLastTime()
	{
		for(Competitor c : competitorList)
		{
			c.setLastPartTime();
		}
	}
	
	public void addTimeStamp(Competitor competitor)
	{
		competitor.addTimeStamp(); // Lägger till LocalTime.now() till objektets timesList.
	}
	
	public void setStartTime(LocalTime startTime, int diffTime) // Sätter starttid utifrån startyp och val av difftid (0, 15 eller 30 sekunder)
	{
		if (startType.equals("Masstart")) {
			for(Competitor c : competitorList)
			{
				c.setStartTime(startTime);
			}
		}
		// Jaktstart, sätter varje deltagares starttid till 15/30sek efter den tidigare startande.
		else if(startType.equals("Jaktstart")) { 
			LocalTime plusTime = startTime;
			for(Competitor c : competitorList)
			{
				plusTime.plusSeconds(diffTime);
				c.setStartTime(plusTime);
			}
		}
		else if(startType.equals("Individuell")) {
			for(Competitor c : competitorList)
			{
				LocalTime timeToAdd = c.getFinnishTime();
				int hours = timeToAdd.getHour();
				int minutes = timeToAdd.getMinute();
				int seconds = timeToAdd.getSecond();
				int nanoSeconds = timeToAdd.getNano();
				
				c.setStartTime(startTime.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds).plusNanos(nanoSeconds));
			}
		}
	}
	
	// Getters & Setters
	public String getStartType() {
		return startType;
	}
	public void setStartType(String startType) {
		this.startType = startType;
	}
	public List<Competitor> getCompetitorList() {
		return competitorList;
	}
	public void setCompetitorList(List<Competitor> competitorList) {
		this.competitorList = competitorList;
	}
	public void addCompetitor(Competitor competitor)
	{
		this.competitorList.add(competitor);
	}
	
}
