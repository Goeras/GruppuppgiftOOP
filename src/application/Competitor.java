package application;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Competitor {

	private String name;
	private AtomicInteger partGoal = new AtomicInteger();
	private AtomicInteger startNumber = new AtomicInteger();
	private String skiTeam;
	private LocalTime startTime; // Satt starttid, tex 10:00:00
	private LocalTime lastPartTime; // sista deltid - starttid.
	private LocalTime finnishTime; // Måltid efter loppet. (mätt utifrån vinnande åkare som har finnishTime 0)
	private List<LocalTime> timesList = new ArrayList<>();
	private String totalTime;
	
	// Konstruktorer
	public Competitor() {
	}
	public Competitor(String name, String skiTeam, List<LocalTime> timesList) {
		this.name = name;
		this.skiTeam = skiTeam;
		this.timesList = timesList;
	}
	
	// Metoder
	public LocalTime durationBetweenTwoLocalTimes(LocalTime a, LocalTime b)
	{
		System.out.println("durationBetweenTwoLocalTimes called, "+LocalTime.now());
		long hours = a.until(b, java.time.temporal.ChronoUnit.HOURS);
		long minutes = a.until(b, java.time.temporal.ChronoUnit.MINUTES);
		long seconds = a.until(b, java.time.temporal.ChronoUnit.SECONDS);
		long nanos = a.until(b, java.time.temporal.ChronoUnit.NANOS);
		
		LocalTime diffTime = LocalTime.of((int)hours, (int)minutes, (int)seconds, (int)nanos);
		
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
		LocalTime startTime = timesList.get(0);
	    LocalTime lastTime = timesList.get(timesList.size() - 1);

	    long nanosDiff = ChronoUnit.NANOS.between(startTime, lastTime);
	    long hours = nanosDiff / 3_600_000_000_000L;
	    nanosDiff %= 3_600_000_000_000L;
	    long minutes = nanosDiff / 60_000_000_000L;
	    nanosDiff %= 60_000_000_000L;
	    long seconds = nanosDiff / 1_000_000_000L;
	    nanosDiff %= 1_000_000_000L;

	    LocalTime diffTime = LocalTime.of((int) hours, (int) minutes, (int) seconds, (int) nanosDiff);
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
