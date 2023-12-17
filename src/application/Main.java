package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{
	
	CompetitorStage competitorStage = new CompetitorStage();


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Button btn = new Button("Tryck här");
		btn.setOnAction(e -> {
			try {
				competitorStage.startCompetitorStage();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		VBox vBox = new VBox();
		vBox.getChildren().add(btn);
		vBox.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(vBox, 200, 200);
		primaryStage.setScene(scene);
		
		System.out.println("starting stage");
		System.out.println("starting stage");

		
		primaryStage.show();
	}
}