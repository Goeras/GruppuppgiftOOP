package application;

import java.util.concurrent.atomic.AtomicInteger;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowTimesStage {
	
	@SuppressWarnings("unchecked")
	public void displayTimes(ObservableList<Competitor> competitors)
	{
		Stage stage = new Stage();
		stage.setTitle("Deltagare");
		
		Button btnExit = new Button("Tillbaka");
		btnExit.setOnAction( e -> {
			stage.close();
		});
		
		TableView<Competitor> table = new TableView<>();
		
		// Namnkolumn
		TableColumn<Competitor, String> nameColumn = new TableColumn<>("Namn"); // Columnnamnet som visas.
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // Attributet att leta efter (letar efter getter: getName() i detta fall)
        
        // Delmålskolumn
        TableColumn<Competitor, String> partGoalColumn = new TableColumn<>("Delmål"); 
        partGoalColumn.setMaxWidth(50);
        partGoalColumn.setCellValueFactory(new PropertyValueFactory<>("partGoal")); 
        
        // Senaste deltidskolumn
        TableColumn<Competitor, String> lastTimeColumn = new TableColumn<>("Senaste deltid"); 
        lastTimeColumn.setMinWidth(100);
        lastTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lastPartTimeAsString")); 
        
        // Startnummerkolumn
        TableColumn<Competitor, AtomicInteger> startNumberColumn = new TableColumn<>("Startnummer"); 
        startNumberColumn.setMaxWidth(85);
        startNumberColumn.setCellValueFactory(new PropertyValueFactory<>("startNumber")); 
        
        table.setItems(competitors);
        table.getColumns().addAll(startNumberColumn, nameColumn, partGoalColumn, lastTimeColumn);
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, btnExit);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vBox, 400, 400);
        stage.setScene(scene);
        stage.show();
	}
	
	
}
