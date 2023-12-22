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

	public void serialize(List<Competitor> competitorList) {
		try {
			FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(new File("./competitors.xml"));
			XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);
			xmlEncoder.setPersistenceDelegate(LocalTime.class, new LocalTimePersistenceDelegate());
			xmlEncoder.writeObject(competitorList);
			xmlEncoder.close();
			fileOutputStream.close();
			
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Competitor> deserialize(List<Competitor> competitorList){
		try {
			FileInputStream fileInputStream = new FileInputStream(new File("./competitors.xml"));
			XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
			competitorList = (List<Competitor>)xmlDecoder.readObject();
			xmlDecoder.close();
			fileInputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return competitorList;
	}
	
}
