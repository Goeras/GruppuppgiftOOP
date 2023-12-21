package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BoxForChoice{

	String choice;
	public String choiceBox()
	{
		
		Stage stage = new Stage();
		stage.setTitle("Gör ditt val:");
		stage.initModality(Modality.APPLICATION_MODAL);
		
		Button btnChoice = new Button("Välj");
		
		ChoiceBox<String> choiceBox = new ChoiceBox<>();
		choiceBox.getStyleClass().add("choice-box");
		choiceBox.getItems().addAll("Masstart", "Jaktstart 15sek","Jaktstart 30sek" , "Individuellstart");
		choiceBox.setValue("Masstart");
        
        btnChoice.setOnAction( e-> {
        	getChoice(choiceBox);
        	stage.close();
        	});
        
        //kryssar man så väljs förifylld starttyp
        stage.setOnCloseRequest(event -> {
            getChoice(choiceBox);
            stage.close();
        });
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(choiceBox, btnChoice);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vBox, 350, 400);
        scene.getStylesheets().add("application/application.css");
        stage.setScene(scene);
        stage.showAndWait();
        
        
        return choice;
	}

	public void getChoice(ChoiceBox<String> choiceBox)
	{
		choice = choiceBox.getValue();
		
	}
	
}
