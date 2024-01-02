package application;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowFinishTimesStage {

	@SuppressWarnings("unchecked") // Varningen gäller blandningen av de olika datatyperna i TableView table.
	public void displayFinishTimes(ObservableList<Competitor> competitors)
	{
		Stage stage = new Stage();
		stage.setTitle("Sluttider");
		stage.initModality(Modality.APPLICATION_MODAL);

		Button btnExit = new Button("Tillbaka");
		btnExit.setOnAction( e -> {
			stage.close();
		});

		ObservableList<Competitor> sortedCompetitors = sortedByTime(competitors);
		TableView<Competitor> table = new TableView<>();

		// Namnkolumn
		TableColumn<Competitor, String> nameColumn = new TableColumn<>("Namn"); // Columnnamnet som visas.
		nameColumn.setMinWidth(100);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // Attributet att leta efter (letar efter getter: getName() i detta fall)


		// Senaste deltidskolumn
		TableColumn<Competitor, LocalTime> lastTimeColumn = new TableColumn<>("Sluttid"); 
		lastTimeColumn.setMaxWidth(70);
		lastTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timeAfterWinner")); 

		// Skidlagskolumn
		TableColumn<Competitor, String> skiTeamColumn = new TableColumn<>("Skidlag"); 
		skiTeamColumn.setMaxWidth(70);
		skiTeamColumn.setCellValueFactory(new PropertyValueFactory<>("skiTeam")); 

		// Startnummerkolumn
		TableColumn<Competitor, AtomicInteger> startNumberColumn = new TableColumn<>("Start #"); 
		//startNumberColumn.setMaxWidth(85);
		startNumberColumn.setCellValueFactory(new PropertyValueFactory<>("startNumber")); 

		TableColumn<Competitor, Number> numberColumn = new TableColumn<>("Placering");
        numberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(table.getItems().indexOf(cellData.getValue()) + 1));

		table.setItems(sortedCompetitors);
		table.getColumns().addAll(numberColumn, nameColumn, skiTeamColumn, startNumberColumn, lastTimeColumn);

		VBox vBox = new VBox();
		vBox.getChildren().addAll(table, btnExit);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(10, 10, 10, 10));
		vBox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vBox, 400, 400);
		scene.getStylesheets().add("application/application.css");
		stage.setScene(scene);
		stage.show();

	}

	
	public ObservableList<Competitor> sortedByTime(ObservableList<Competitor> competitors)
	{ 
		ObservableList<Competitor> sortedCompetitors = FXCollections.observableArrayList(); // Tom lista för att lägga till målgångar.
		
		for (Competitor c : competitors) {
			if(c.isFinished()) {
				if(c.isWinner()) {
					c.setTimeAfterWinner(LocalTime.MIDNIGHT); // Lägger till tiden 00.00 till vinnarens "tid efter vinnare" för att visa 00.00 placeringsfönstret.
					
				}
				sortedCompetitors.add(c); // lägger till målgående deltagare till listan.
			}
		}
		
		Collections.sort(sortedCompetitors, Comparator.comparing(Competitor::getFinishTime)); // Sorterar listan efter finishtime i stigande ordning.

		if(sortedCompetitors.size()>1) {
			LocalTime winnerTime = sortedCompetitors.get(0).getFinishTime();
			for (Competitor c : sortedCompetitors) {
				if(c.getFinishTime().isAfter(winnerTime)) {
					c.setTimeAfterWinner(durationBetweenTwoLocalTimes(winnerTime, c.getFinishTime()));
				}
			}
		}

		return sortedCompetitors; // returnerar en sorterad lista på de deltagare som gått i mål.
	}

	public LocalTime durationBetweenTwoLocalTimes(LocalTime a, LocalTime b) {

		Duration duration = Duration.between(a, b); // jämför skillnaden

		long hours = duration.toHours(); // Delar upp skillnaden i timmar, minuter, sekunder och nano.
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();
		long nanos = duration.toNanosPart();

		LocalTime diffTime = LocalTime.of((int) hours, (int) minutes, (int) seconds, (int) nanos); // skapar en LocalTime av tidigare värden ur int-parametrar. Castar från long till int.

		return diffTime;
	}
}
