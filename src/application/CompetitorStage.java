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
import javafx.scene.control.ChoiceBox;
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
	ShowFinishTimesStage showFinishTimesStage = new ShowFinishTimesStage();

	private Button startGame;
	private Button setTime;
	private Button setFinishTime;
	private Button showTime;
	private Button showFinishTimes;
	private Button goBack;
	private Competitor choice;
	private boolean gameStarted;
	private boolean winner = false;
	public ObservableList<Competitor> competitors = FXCollections.observableArrayList();

	@SuppressWarnings("unchecked") // Varningen gäller blandningen av datatyperna String och AtomicInteger i TableView table.
	public void startCompetitorStage() throws Exception {
		Stage stage =  new Stage();
		stage.setTitle("Tour de Ski");
		stage.initModality(Modality.APPLICATION_MODAL);

		// Dummie-åkare skapas om användaren ej lagt till egna åkare.
		if (competitors.isEmpty()){
		addBotCompetitors();
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
			setFinishTime.setVisible(true);
			startGame.setVisible(false);
			}
		}); 

		setTime = new Button("Logga delmål");
		setTime.setVisible(false);
		setTime.setOnAction( e -> {
			if(gameStarted) {
				if(choice != null) {
					gamePlay.addTimeStamp(choice);
				}
			}
		});
		
		setFinishTime = new Button("Logga Målgång");
		setFinishTime.setVisible(false);
		setFinishTime.setOnAction( e -> {
			if(gameStarted) {
				if(choice != null) {
					if (!winner) {
						choice.setWinner(true);
						winner = true;
					}
					gamePlay.addTimeStamp(choice);
					choice.setFinished(true);
					choice.setFinishTime(LocalTime.now());
					showFinishTimes.setVisible(true);
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
		
		showFinishTimes = new Button("Visa Målgångstider");
		showFinishTimes.setVisible(false);
		showFinishTimes.setOnAction( e -> {
			if(gameStarted) {
			showFinishTimesStage.displayFinishTimes(competitors);
			}
		});

		goBack = new Button();
		goBack.setText("Tillbaka");
		goBack.setOnAction( e -> {
			gamePlay.setFinishTimes();
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
		TableColumn<Competitor, AtomicInteger> finishTimeColumn = new TableColumn<>("Senaste tävling");
		finishTimeColumn.setMinWidth(100);
		finishTimeColumn.setCellValueFactory(new PropertyValueFactory<>("finishTime"));

		table.setItems(competitors);
		table.getColumns().addAll(nameColumn, teamColumn, finishTimeColumn);
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
		vBoxButtons.getChildren().addAll(startGame, setTime, setFinishTime, showTime, showFinishTimes, goBack);

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(vBoxLabel);
		borderPane.setCenter(table);
		borderPane.setBottom(vBoxButtons);
		
		// "Avsluta tävling" aktiveras när man kryssar ner fönstret
        stage.setOnCloseRequest(event -> {
        	gamePlay.setFinishTimes();
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
	
    public void addCompetitorWindow() {
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
    
    public void removeCompetitorWindow() {
    	   Stage removeStage = new Stage();
           VBox removeRoot = new VBox(10);
           Button removeBtn = new Button("Remove Competitor");

           // Create a ChoiceBox to select the competitor to remove
           ChoiceBox<String> competitorChoiceBox = new ChoiceBox<>();
           updateChoiceBox(competitorChoiceBox);

           removeBtn.setOnAction(e -> {
               // Remove the selected competitor
               String selectedCompetitor = competitorChoiceBox.getValue();
               removeCompetitor(selectedCompetitor);

               // Update the ChoiceBox with the latest competitors
               updateChoiceBox(competitorChoiceBox);

               removeStage.close();
           });

           removeRoot.getChildren().addAll(competitorChoiceBox, removeBtn);
           Scene removeScene = new Scene(removeRoot, 300, 200);
           removeScene.getStylesheets().add("application/application.css");
           removeStage.setScene(removeScene);
           removeStage.setTitle("Remove Competitor Window");
           removeStage.initModality(Modality.APPLICATION_MODAL);
           removeStage.show();
    }
    
    private void removeCompetitor(String competitorName) {
        if (!competitors.isEmpty()) {
            // Remove the selected competitor from the list
            competitors.removeIf(competitor -> competitor.getName().equals(competitorName));
        }
    }
    
    private void updateChoiceBox(ChoiceBox<String> choiceBox) {
        // Update the ChoiceBox with the latest list of competitors
        choiceBox.getItems().clear();
        for (Competitor competitor : competitors) {
            choiceBox.getItems().add(competitor.getName());
        }
        choiceBox.setValue(null); // Clear the selection
    }
    
 // Konverterar Competitor-objekten till DTO. (Data Transfer Object), och anropar metod för att skriva till XML.
    public void serialize() {
        List<CompetitorDTO> competitorDTOList = CompetitorConverter.listToDTO(competitors);
        competitorSerialization.serialize(competitorDTOList);
    }

 // Anropar metod för att läsa in DTO-objekten, konverterar sedan om till Competitor-objekt igen och lägger in i listan över deltagare.
    public void deserialize() {
        List<CompetitorDTO> competitorDTOList = competitorSerialization.deserialize(new ArrayList<>()); 
        competitors.setAll(CompetitorConverter.listFromDTO(competitorDTOList));
    }
    public void resetCompetitorAttributes() {
    	for (Competitor c : competitors) {
    		c.setFinished(false);
    	}
    }
    
    public void addBotCompetitors()
    {
    	Competitor c1 = new Competitor();
		c1.setName("Evert Ljusberg");
		c1.setSkiTeam("Östersund SK");
		
		Competitor c2 = new Competitor();
		c2.setName("Hasse Alfredsson");
		c2.setSkiTeam("Malmö SK");
		
		Competitor c3 = new Competitor();
		c3.setName("Thorsten Flinck");
		c3.setSkiTeam("Solna SK");
		
		Competitor c4 = new Competitor();
		c4.setName("Kenta Gustafsson");
		c4.setSkiTeam("Nacka SK");
		
		Competitor c5 = new Competitor();
		c5.setName("Karl Moraeus");
		c5.setSkiTeam("Orsa SK");
		
		competitors.addAll(c1,c2,c3,c4,c5);
    }
}
