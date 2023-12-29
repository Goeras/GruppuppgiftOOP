package application;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Competitor implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2214931996410813223L;
	private String name;
	private transient AtomicInteger partGoal = new AtomicInteger();
	private transient AtomicInteger startNumber = new AtomicInteger();
	private String skiTeam;
	private transient LocalTime startTime; // Satt starttid, tex 10:00:00
	private transient LocalTime lastPartTime; // sista deltid - starttid.
	private LocalTime finnishTime; // Måltid efter loppet. (mätt utifrån vinnande åkare som har finnishTime 0)
	private transient List<LocalTime> timesList = new ArrayList<>();
	private transient String totalTime;
	private transient boolean finished = false; // Boolean för att se om åkaren gått i mål.
	private transient LocalTime timeAfterWinner; // Skillnaden mellan målgångtiden mellan winnaren och detta objekt
	
	// Konstruktorer
	public Competitor() {
	}
	public Competitor(String name, String skiTeam) {
		this.name = name;
		this.skiTeam = skiTeam;
	}
	
	// Metoder
	public LocalTime durationBetweenTwoLocalTimes(LocalTime a, LocalTime b) {
	    
	    Duration duration = Duration.between(a, b); // jämför skillnaden

	    long hours = duration.toHours(); // Delar upp skillnaden i timmar, minuter, sekunder och nano.
	    long minutes = duration.toMinutesPart();
	    long seconds = duration.toSecondsPart();
	    long nanos = duration.toNanosPart();

	    LocalTime diffTime = LocalTime.of((int) hours, (int) minutes, (int) seconds, (int) nanos); // skapar en LocalTime av tidigare värden ur int-parametrar. Castar från long till int.

	    return diffTime;
	}
	
	public String localTimeToString(LocalTime localTime)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:NN");
		
		return localTime.format(formatter);
	}
	
	public LocalTime stringToLocalTime(String inputString)
	{
		LocalTime converted = LocalTime.parse(inputString);
		
		return converted;
	}
	
	public String getLastPartTimeAsString()
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SS");
		
		return lastPartTime.format(formatter);
	}
	public LocalTime getLastPartTime()
	{
		return lastPartTime;
	}
	
	public String getFormattedTime() {
        LocalTime firstTime = timesList.isEmpty() ? null : timesList.get(0);
        return firstTime != null ? firstTime.toString() : "";
    }
	public void setLastPartTime() {
		LocalTime startTime = timesList.get(0); // hämtar första tiden i Listan
	    LocalTime lastTime = timesList.get(timesList.size() - 1); // hämtar sista tiden i Listan

	    long nanosDiff = ChronoUnit.NANOS.between(startTime, lastTime); // räknar ut diff mellan första och sista tiden i nanosekunder.
	    long hours = nanosDiff / 3_600_000_000_000L; // Räknar ut antalet timmat utifrån "nanosDiff".
	    nanosDiff %= 3_600_000_000_000L; // Uppaterar "nanosDiff" med resterande antalet nanosekunder efter att timmarna dragits bort.
	    long minutes = nanosDiff / 60_000_000_000L; // Räknar ut antalet minuter utifrån "nanosDiff".
	    nanosDiff %= 60_000_000_000L; // Uppdaterar "nanosDiff" - minutes.
	    long seconds = nanosDiff / 1_000_000_000L; // Räknar ut antalet minuter utifrån "nanosDiff".
	    nanosDiff %= 1_000_000_000L; // Uppdaterar "nanosDiff" - seconds.

	    LocalTime diffTime = LocalTime.of((int) hours, (int) minutes, (int) seconds, (int) nanosDiff); // Sätter ihop en LocalTime från ovanstående variabel-värden, castar från long till int.
		this.lastPartTime = diffTime;
	}
	
	public void addTimeStamp()
	{
		timesList.add(LocalTime.now());
		this.partGoal.incrementAndGet();
	}
	
	// Getters & Setters
	public String getPartGoal()
	{
		return String.valueOf(partGoal);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		timesList.add(startTime);
		this.startTime = startTime;
	}
	public LocalTime getFinnishTime() {
		return finnishTime;
	}
	public void setFinnishTime(LocalTime finnishTime) {
		this.finnishTime = finnishTime;
	}
	public AtomicInteger getStartNumber() {
		return startNumber;
	}
	public void setStartNumber(int startNumber) {
		this.startNumber.set(startNumber);
	}
	public String getSkiTeam() {
		return skiTeam;
	}
	public void setSkiTeam(String skiTeam) {
		this.skiTeam = skiTeam;
	}
	public LocalTime getIndexOfTimesList(int i)
	{
		return timesList.get(i);
	}
	public List<LocalTime> getTimesList() {
		return timesList;
	}
	public void setTimesList(List<LocalTime> timesList) {
		this.timesList = timesList;
	}
	public void addTime(LocalTime time){
		this.timesList.add(time);
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		
		this.totalTime = totalTime;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public LocalTime getTimeAfterWinner() {
		return timeAfterWinner;
	}
	public void setTimeAfterWinner(LocalTime timeAfterWinner) {
		this.timeAfterWinner = timeAfterWinner;
	}
	@Override
	public String toString() { // Behöver sorteras efter ledare med position, samt tiden mellan start och senast registrerade (sista i listan)
		String returnTime;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		if(timesList.size()==0)
		{
			returnTime = LocalTime.now().format(formatter);
;		}
		else
		{
			returnTime = timesList.get(timesList.size()-1).format(formatter);
		}
			
		return "    1    "+name+ "      "+startNumber+ "      "+returnTime;
		
	}
	
}
