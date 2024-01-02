package application;

import java.time.LocalTime;

  // DTO-klass (Data Transfer Object) för att spara endast nödvändiga attribut till xml, då tidigare serialisering tog med flertal attribut från Competitor-objekten trots "transient"
public class CompetitorDTO {
	
	private String name;
    private String skiTeam;
    private LocalTime finishTime;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSkiTeam() {
		return skiTeam;
	}
	public void setSkiTeam(String skiTeam) {
		this.skiTeam = skiTeam;
	}
	public LocalTime getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
	}
}
