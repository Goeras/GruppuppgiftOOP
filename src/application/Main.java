package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{
	
	CompetitorStage competitorStage = new CompetitorStage();
	BoxForChoice choiceBox = new BoxForChoice(); 


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		CompetitorStage competitorWindow = new CompetitorStage();
		ObservableList<Competitor> competitors = FXCollections.observableArrayList();
		Button btnAddCompetitor = new Button("Lägg till deltagare");
		btnAddCompetitor.setOnAction(e -> {
			competitorStage.AddCompetitorWindow();
		});

		
		
		Button btnCompetition = new Button("Tävlingsfönster");
		btnCompetition.setVisible(false); // gömer knappen tills användare tryckt på knappen för starttyp.
		btnCompetition.setOnAction(e -> {
			try {
				competitorStage.startCompetitorStage();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		Button btnContestType = new Button("Välj starttyp");
		btnContestType.setOnAction( e -> {
			btnCompetition.setVisible(true); // visar knappen för tävlingsfönster.
			String choice = choiceBox.choiceBox();
			competitorStage.setGamePlayStartType(choice);
			// valbara alternativ: String startType = "Masstart", "Jaktstart", "Individuellstart"
			//competitorStage.setGamePlayStartType(startType); // tänkbart problem: objektet är ej skapat förens i CompetitorStage klassen.
		});
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(btnAddCompetitor, btnContestType, btnCompetition);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(10));
		
		Scene scene = new Scene(vBox, 200, 200);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}