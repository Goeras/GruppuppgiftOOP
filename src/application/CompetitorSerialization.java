package application;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class CompetitorSerialization {

	// Skriver DTO-objekten till XML.
	public void serialize(List<CompetitorDTO> competitorDTOList) {
		try {
			FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(new File("./competitors.xml"));
			XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);
			xmlEncoder.setPersistenceDelegate(LocalTime.class, new LocalTimePersistenceDelegate());
			xmlEncoder.writeObject(competitorDTOList);
			xmlEncoder.close();
			fileOutputStream.close();
			
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Läser in DTO-objekten från XML och returnerar dem i en List.
	@SuppressWarnings("unchecked") // Objekten som läses in kommer alltid att vara av typen Competitor i detta program. Generisk metod som säkerställer objekttypen behövs därför inte.
	public List<CompetitorDTO> deserialize(List<CompetitorDTO> competitorList){
		try {
			FileInputStream fileInputStream = new FileInputStream(new File("./competitors.xml"));
			XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
			competitorList = (List<CompetitorDTO>)xmlDecoder.readObject();
			xmlDecoder.close();
			fileInputStream.close();
			
		} catch (IOException e) {
			System.out.println("File is not created yet.");
			e.printStackTrace();
		}
		return competitorList;
	}
	
}
