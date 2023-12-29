package application;

import javafx.application.Application;
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
		competitorStage.deserialize();
		Button btnAddCompetitor = new Button("Lägg till deltagare");
		btnAddCompetitor.setOnAction(e -> {
			competitorStage.AddCompetitorWindow();
		});

		primaryStage.setOnCloseRequest( e -> {
			e.consume();
			competitorStage.resetCompetitorAttributes();
			competitorStage.serialize();
			primaryStage.close();
		});

		Button btnCompetition = new Button("Tävlingsfönster");
		btnCompetition.setVisible(false); // gömer knappen tills användare tryckt på knappen för starttyp.
		btnCompetition.setOnAction(e -> {
			try {
				competitorStage.startCompetitorStage();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		Button btnContestType = new Button("Välj starttyp");
		btnContestType.setOnAction( e -> {
			btnCompetition.setVisible(true); // visar knappen för tävlingsfönster.
			String choice = choiceBox.choiceBox();
			competitorStage.setGamePlayStartType(choice);
		});

		Button btnQuit = new Button("Avsluta");
		btnQuit.setOnAction( e -> {
			competitorStage.resetCompetitorAttributes();
			competitorStage.serialize();
			primaryStage.close();
		});

		VBox vBox = new VBox();
		vBox.getChildren().addAll(btnAddCompetitor, btnContestType, btnCompetition, btnQuit);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(10));

		Scene scene = new Scene(vBox, 350, 400);
		scene.getStylesheets().add("application/application.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}