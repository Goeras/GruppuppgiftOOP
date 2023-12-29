package application;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GamePlay {

	// Attribut
	String startTime; // Huvudsaklig starttid. (masstart eller första startande)
	String startType; // "Massstart" / "Jaktstart" / "Individuellstart". sätts av användaren.
	int diffTime = 0;
	List<Competitor> competitorList = new ArrayList<>(); // Lista över deltagare - Competitor
	
	// Konstruktorer
	public GamePlay() {
	}
	public GamePlay(String startType, List<Competitor> competitorList) {
		this.startType = startType;
		this.competitorList = competitorList;
	}
	
	// Metoder
	public void setFinishTimes() // Sätter sluttiden från senaste Tid i objektets lista.
	{
		for(Competitor c : competitorList)
		{
			c.setFinishTime(c.getLastPartTime());
		}
	}
	
	public void startTimers()  // Sätter starttiden baserat på Starttyp. 
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
		else if(startType.equals("Jaktstart 15sek")) // Start med 30 sekunders mellanrum.
		{ 
			LocalTime plusTime = LocalTime.now(); // variabel för starttid. första åkare = LocalTIme.now(), åkare nr2 = localTime.now()+30 sekunder osv..
			for(Competitor c : competitorList)
			{
				c.setStartNumber(startNumber); // Sätter startnummer.
				startNumber++;
				
				c.setStartTime(plusTime); // sätter starttiden hos objektet.
				plusTime = plusTime.plusSeconds(15); // Lägger till 30 sekunder på starttiden för varje åkare.
			}
		}
		else if(startType.equals("Jaktstart 30sek")) // Start med 30 sekunders mellanrum.
		{ 
			LocalTime plusTime = LocalTime.now(); // variabel för starttid. första åkare = LocalTIme.now(), åkare nr2 = localTime.now()+30 sekunder osv..
			for(Competitor c : competitorList)
			{
				c.setStartNumber(startNumber); // Sätter startnummer.
				startNumber++;
				
				c.setStartTime(plusTime); // sätter starttiden hos objektet.
				plusTime = plusTime.plusSeconds(30); // Lägger till 30 sekunder på starttiden för varje åkare.
			}
		}
		else if(startType.equals("Individuellstart")) // Start baserat på tidigare finishTime.
		{
			List<Competitor> sortedByFinishTime = new ArrayList<>();
			sortedByFinishTime.addAll(competitorList); // kopia på listan över tävlande so skall sorteras efter senaste åktid.
			
			Comparator <Competitor> nameComparator = (c1, c2) -> c1.getFinishTime().compareTo(c2.getFinishTime()); // Comparator för att jämföra ebjektens finishTime
			Collections.sort(sortedByFinishTime, nameComparator); // sorterad efter finishTime
			
			Competitor lastWinner = sortedByFinishTime.get(0); // Vinnaren från förra tävlingen.
			for(Competitor c : sortedByFinishTime)
			{
				LocalTime newStartTime = LocalTime.now(); // Första starttid = NU. Andra starttid = NU+åkarens finishTime mellanskillnad mot vinnaren.
				LocalTime timeToAddLocalTime = c.durationBetweenTwoLocalTimes(lastWinner.getFinishTime(), c.getFinishTime()); // lägger till mellanskillnad
				
				//Duration timeToAddDuration = Duration.between(LocalTime.MIDNIGHT, timeToAddLocalTime); // gör om en LocalTime till en Duration då man inte kan plussa en LocalTime med en annan LocalTime.
				
				newStartTime = newStartTime.plusHours(timeToAddLocalTime.getHour()).plusMinutes(timeToAddLocalTime.getMinute()).plusSeconds(timeToAddLocalTime.getSecond()); // Lägger till ovan Duration till en LocalTime.now().
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
