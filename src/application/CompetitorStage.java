package application;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CompetitorStage {
	ShowTimesStage showTimesStage = new ShowTimesStage();
	GamePlay gamePlay = new GamePlay();
	CompetitorSerialization competitorSerialization = new CompetitorSerialization();

	private Button startGame;
	private Button setTime;
	private Button showTime;
	private Button goBack;
	private Competitor choice;
	private boolean gameStarted;
	public ObservableList<Competitor> competitors = FXCollections.observableArrayList(); // Här skall Competitor-objekten läggas till efter skapande.

	@SuppressWarnings("unchecked") // Varningen gäller blandningen av datatyperna String och AtomicInteger i TableView table.
	public void startCompetitorStage() throws Exception {
		Stage stage =  new Stage();
		stage.setTitle("Tour de Ski");
		stage.initModality(Modality.APPLICATION_MODAL);

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
			gamePlay.setCompetitorList(competitors);
			gamePlay.startTimers();
			gamePlay.setObjektLastTime();
			gameStarted = true;
			setTime.setVisible(true);
			showTime.setVisible(true);
			startGame.setVisible(false);
			}
		}); // Lägger till objektets startTime i sin tidslista.

		setTime = new Button("Logga delmål");
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
		goBack.setText("Tillbaka");
		goBack.setOnAction( e -> {
			gamePlay.setFinnishTimes();
			gameStarted = false;
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
		
		// "Avsluta tävling" aktiveras när man kryssar ner fönstret
        stage.setOnCloseRequest(event -> {
        	gamePlay.setFinnishTimes();
            gameStarted = false;
            stage.close();
        });

		Scene scene = new Scene(borderPane, 305, 450);
		scene.getStylesheets().add("application/application.css");
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}

	// Lägger till en ny tid till den tävlandes lista med tider..
	public void setNewTime(Competitor c){
		c.addTime(LocalTime.now());
	}

	// Skriver ut alla tider ifrån den tävlandes lista med tider från första till sista..
	public void showAllTimes(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		for(Competitor c : competitors) {
			for(LocalTime t : c.getTimesList())
			{
				System.out.println(t.format(formatter)); // Funktionen ej färdig.. endast i testsfyte.
			}
		}
	}
	
	public void setGamePlayStartType(String startType)
	{
		gamePlay.setStartType(startType);
	}
	
    public void AddCompetitorWindow() {
        Stage newStage = new Stage();
        VBox newRoot = new VBox(10);
        TextField nameTextField = new TextField();
        TextField skiTeamTextField = new TextField();
        Button newBtn = new Button("Lägg till");

        newBtn.setOnAction(e -> {
            String name = nameTextField.getText();
            String skiTeam = skiTeamTextField.getText();
            newStage.close();

            // Create a new Competitor instance and set the values
            Competitor newCompetitor = new Competitor();
            newCompetitor.setName(name);
            newCompetitor.setSkiTeam(skiTeam);

            // Bekräftelsemeddelande i main layouten
            System.out.println("Tävlandes namn: " + newCompetitor.getName());
            System.out.println("Skidlag: " + newCompetitor.getSkiTeam());
            
            competitors.add(newCompetitor);

            // Clear text fields for the next entry
            nameTextField.clear();
            skiTeamTextField.clear();
        });

        newRoot.getChildren().addAll(new TextField("Ange namnet nedan"), nameTextField,
                                      new TextField("Ange skidlag nedan"), skiTeamTextField, newBtn);
        Scene newScene = new Scene(newRoot, 300, 200);
        newScene.getStylesheets().add("application/application.css");
        newStage.setScene(newScene);
        newStage.setTitle("New Competitor Window");
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.show();
    }
    public void serialize() {
    	List<Competitor> CompetitorList = new ArrayList<>();
		CompetitorList.addAll(competitors);
	    competitorSerialization.serialize(CompetitorList);
    }
    
    public void deserialize(){
    	
    	List<Competitor> competitorList = new ArrayList<>();
    	competitorList = competitorSerialization.deserialize(competitorList);
    	competitors.addAll(competitorList);
    }
}
