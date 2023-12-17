package application;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CompetitorStage {
	ShowTimesStage showTimesStage = new ShowTimesStage();
	GamePlay gamePlay = new GamePlay();

	private Button startGame;
	private Button setTime;
	private Button showTime;
	private Button goBack;
	
	private String startType;
	private Competitor choice;
	private boolean gameStarted;
	public ObservableList<Competitor> competitors = FXCollections.observableArrayList();

	@SuppressWarnings("unchecked")
	public void startCompetitorStage() throws Exception {
		Stage stage =  new Stage();
		stage.setTitle("Tour de Ski");

		// För testningens skull..
		if (competitors.isEmpty()){
		Competitor c1 = new Competitor();
		c1.setName("Markus Göras");
		c1.setSkiTeam("Falu SK");
		c1.setTotalTime("01:45:25");
		competitors.add(c1);
		Competitor c2 = new Competitor();
		c2.setName("Hasse Alfredsson");
		c2.setSkiTeam("Malmö SK");
		c2.setTotalTime("00:32:48");
		competitors.add(c2);
		}

		// Label
		Label label = new Label();
		label.setText("Deltagare");

		// Knappar
		startGame = new Button("Starta tävlingen");
		startGame.setVisible(true);
		startGame.setOnAction( e -> {
			if (!gameStarted) {
			gamePlay.setStartType(startType);
			gamePlay.setCompetitorList(competitors);
			gamePlay.startTimers();
			gamePlay.setObjektLastTime();
			gameStarted = true;
			setTime.setVisible(true);
			showTime.setVisible(true);
			startGame.setVisible(false);
			}
		}); // Lägger till en ebjektets startTime i sin tidslista.

		setTime = new Button("Logga tiden");
		setTime.setVisible(false);
		setTime.setOnAction( e -> {
			if(gameStarted) {
				if(choice != null) {
					gamePlay.addTimeStamp(choice);
				}
			}
		});

		showTime = new Button("Visa senaste tid");
		showTime.setVisible(false);
		showTime.setOnAction( e -> {
			if(gameStarted) {
			gamePlay.setObjektLastTime();
			showTimesStage.displayTimes(competitors);
			}
		});

		goBack = new Button();
		goBack.setText("Avsluta tävling");
		goBack.setOnAction( e -> {
			gamePlay.setFinnishTimes();
			stage.close();
		});

		// Lista på deltagare, till rullistan..
		TableView<Competitor> table = new TableView<>();

		// Name column
		TableColumn<Competitor, String> nameColumn = new TableColumn<>("Namn"); // Columnnamnet som visas.
		nameColumn.setMinWidth(100);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // Attributet att leta efter (letar efter getter)

		// Time column
		TableColumn<Competitor, String> teamColumn = new TableColumn<>("Klubb"); // Columnnamnet som visas.
		teamColumn.setMinWidth(100);
		teamColumn.setCellValueFactory(new PropertyValueFactory<>("skiTeam")); // Attributet att leta efter (letar efter getter)

		// Startnumber column
		TableColumn<Competitor, AtomicInteger> finnishTimeColumn = new TableColumn<>("Senaste tävling");
		finnishTimeColumn.setMinWidth(100);
		finnishTimeColumn.setCellValueFactory(new PropertyValueFactory<>("finnishTime"));

		table.setItems(competitors);
		table.getColumns().addAll(nameColumn, teamColumn, finnishTimeColumn);
		table.setOnMouseClicked( e -> { // sätter choice till det klickade objectet i tableview..
			if(e.getClickCount() == 1) {
				choice = table.getSelectionModel().getSelectedItem();
			}
		});

		VBox vBoxLabel = new VBox(label); // behöver även rubriker till scrolllistan här?? "placering, Tid, Namn, StartNummer"
		vBoxLabel.setSpacing(10);
		vBoxLabel.setPadding(new Insets(10, 10, 10, 10));
		vBoxLabel.setAlignment(Pos.CENTER);

		VBox vBoxButtons = new VBox(30);
		vBoxButtons.setAlignment(Pos.CENTER);
		vBoxButtons.setSpacing(10);
		vBoxButtons.setPadding(new Insets(10, 10, 10, 10));
		vBoxButtons.getChildren().addAll(startGame, setTime, showTime, goBack);

		BorderPane borderPane = new BorderPane();
		//borderPane.setStyle("-fx-background-color: #f0ed37;");
		borderPane.setTop(vBoxLabel);
		borderPane.setCenter(table);
		borderPane.setBottom(vBoxButtons);

		Scene scene = new Scene(borderPane, 305, 450);
		stage.setScene(scene);
		stage.show();
	}

	// Lägger till en ny tid till den tävlandes lista med tider..
	public void setNewTime(Competitor c){
		System.out.println("Setting a new time..");
		c.addTime(LocalTime.now());
	}

	// Skriver ut alla tider ifrån den tävlandes lista med tider från första till sista..
	public void showAllTimes(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		for(Competitor c : competitors) {
			System.out.println("Tävlande: "+c.getName()+ ", Startnummer: "+c.getStartNumber());
			for(LocalTime t : c.getTimesList())
			{
				System.out.println(t.format(formatter));
			}
		}
	}

}
