package application;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GamePlay {

	// Attribut
	String startTime; // Huvudsaklig starttid. (masstart eller första startande)
	String startType = "Jaktstart";
	int diffTime = 0;
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
	
	public void startTimers() 
	{
		int startNumber = 1;
		if(startType.equals("Masstart"))// Alla startar samtidigt.
		{
			for (Competitor c : competitorList)
			{
				c.setStartNumber(startNumber);
				startNumber++;
				c.setStartTime(LocalTime.now());
				//c.addTime(c.getStartTime()); // Lägger till objektets starttid i objektets timesList.
				//c.addTimeStamp(); // lägger till en LocalTime.now() för varje objekt.
			}
		}
		else if(startType.equals("Jaktstart")) // Start med 15/30 sekunders mellanrum.
		{ 
			LocalTime plusTime = LocalTime.now();
			for(Competitor c : competitorList)
			{
				c.setStartNumber(startNumber); // Sätter startnummer.
				startNumber++;
				System.out.println("Jakt startid, "+c.getName()+", "+ plusTime.toString());
				//plusTime.plusSeconds(diffTime); // lägger till starttid.
				c.setStartTime(plusTime);
				plusTime = plusTime.plusSeconds(30);
			}
		}
		else if(startType.equals("Individuellstart")) // Start baserat på tidigare finnishTime. Behöver sorterar efter finnishTime.
		{
			List<Competitor> sortedByFinnishTime = new ArrayList<>();
			sortedByFinnishTime.addAll(competitorList);
			
			Comparator <Competitor> nameComparator = (c1, c2) -> c1.getFinnishTime().compareTo(c2.getFinnishTime()); // Comparator för att jämföra ebjektens finnishTime
			Collections.sort(sortedByFinnishTime, nameComparator); // sorterad efter finnishTime
			
			Competitor lastWinner = sortedByFinnishTime.get(0); // Vinnaren från förra tävlingen.
			for(Competitor c : sortedByFinnishTime)
			{
				LocalTime newStartTime = LocalTime.now(); // Första starttid = NU. Andra starttid = NU+åkarens finnishTime mellanskillnad mot vinnaren.
				LocalTime timeToAddLocalTime = c.durationBetweenTwoLocalTimes(lastWinner.getFinnishTime(), c.getFinnishTime()); // lägger till mellanskillnad
				
				Duration timeToAddDuration = Duration.between(LocalTime.MIDNIGHT, timeToAddLocalTime); // gör om en LocalTime till en Duration då man inte kan plussa en LocalTime med en annan LocalTime.
				newStartTime = newStartTime.plus(timeToAddDuration); // Lägger till ovan Duration till en LocalTime.now().
				c.setStartTime(newStartTime); // Sätter starttiden för objektet = LocalTime.now() + mellanskillnad från tidigare lopp mellan winnaren och detta objekt.
				
				c.setStartNumber(startNumber); // Sätter startnummer till objektet
				startNumber++; // Räknar upp startnummer med 1 till nästa åkare.
			}
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
	
	/*
	 * public void setStartTime(LocalTime startTime, int diffTime) // Sätter
	 * starttid utifrån startyp och val av difftid (0, 15 eller 30 sekunder) { if
	 * (startType.equals("Masstart")) { for(Competitor c : competitorList) {
	 * c.setStartTime(startTime); } } // Jaktstart, sätter varje deltagares starttid
	 * till 15/30sek efter den tidigare startande. else
	 * if(startType.equals("Jaktstart")) { LocalTime plusTime = startTime;
	 * for(Competitor c : competitorList) { plusTime.plusSeconds(diffTime);
	 * c.setStartTime(plusTime); } } else if(startType.equals("Individuell")) {
	 * for(Competitor c : competitorList) { LocalTime timeToAdd =
	 * c.getFinnishTime(); int hours = timeToAdd.getHour(); int minutes =
	 * timeToAdd.getMinute(); int seconds = timeToAdd.getSecond(); int nanoSeconds =
	 * timeToAdd.getNano();
	 * 
	 * c.setStartTime(startTime.plusHours(hours).plusMinutes(minutes).plusSeconds(
	 * seconds).plusNanos(nanoSeconds)); } } }
	 */
	
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
