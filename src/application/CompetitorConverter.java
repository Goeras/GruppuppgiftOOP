package application;

import java.util.List;
import java.util.stream.Collectors;

    // Klass för att konvertera Competitor-objekt till DTO (Data Transfer Object) pga av tidigare strul med transient-attribut.
public class CompetitorConverter {

	// Före serialisering kopieras de attribut vi vill spara till DTO-objektet
	public static CompetitorDTO toDTO(Competitor competitor) {
        CompetitorDTO dto = new CompetitorDTO(); 
        dto.setName(competitor.getName());
        dto.setSkiTeam(competitor.getSkiTeam());
        dto.setFinishTime(competitor.getFinishTime());
        return dto;
    }

	// Vid deserialisering för varje objekt tas attributen från DTO-objekten och läggs in i Competitor-objekten.
    public static Competitor fromDTO(CompetitorDTO dto) {
        Competitor competitor = new Competitor(); 
        competitor.setName(dto.getName());
        competitor.setSkiTeam(dto.getSkiTeam());
        competitor.setFinishTime(dto.getFinishTime());
        // Kopiera över andra attribut som behövs
        return competitor;
    }
    
    // Skapar upp streams av alla objekt i competitors-listan och omvandlar varje objekt till en DTO och returnerar sedan en lista av DTO-objekt.
    public static List<CompetitorDTO> listToDTO(List<Competitor> competitors) {
        return competitors.stream().map(CompetitorConverter::toDTO).collect(Collectors.toList()); 
    }

    // Samma som ovan men i omvänd ordning. Dvs, från DTO-list till Competitor-list.
    public static List<Competitor> listFromDTO(List<CompetitorDTO> dtos) {
        return dtos.stream().map(CompetitorConverter::fromDTO).collect(Collectors.toList());
    }
	
}
