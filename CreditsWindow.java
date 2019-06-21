import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class CreditsWindow {

	 public static void display() {
        Stage window = new Stage();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        window.setTitle("Credits");
        window.setMinWidth(219);
        Label backgroundCredit = new Label("Background Music: "
        		+ "\n"
        		+ "\n"
        		+ "\"Being Cool Doesn`t Make Me Fool\" by RGA-GT licensed"
        		+ " CC-BY 4.0, CC-BY 3.0, CC-BY-SA 4.0, CC-BY-SA 3.0, GPL 3.0, GPL 2.0, OGA-BY 3.0, or CC0:"
        		+ " https://opengameart.org/content/the-best-of-rga-gt-music-pack");
        backgroundCredit.setWrapText(true);
        Label gameEndClipCredit = new Label("Game End Clip: "
        		+ "\n"
        		+ "\n"
        		+ "\"Game Over Voice Deep\" by Anton Porsche"
        		+ " licensed CC-BY 3.0 https://opengameart.org/content/game-over-voice-deep-2-styles");
        gameEndClipCredit.setWrapText(true);
        Label codeCredit = new Label("Code: "
        		+ "\n"
        		+ "\n"
        		+ "JavaFX-Connect-4 by creeperjedi"
        		+ " licensed MIT https://github.com/creeperjedi/JavaFX-Connect-4");
        codeCredit.setWrapText(true);
        
        Button closeButton = new Button("Return to main menu");
        closeButton.setOnAction(e -> window.close());

        GridPane.setConstraints(backgroundCredit, 1, 1);
        GridPane.setConstraints(gameEndClipCredit, 1, 2);
        GridPane.setConstraints(codeCredit, 1, 3);
        GridPane.setConstraints(closeButton, 1, 4);
        GridPane.setHalignment(closeButton, HPos.CENTER);
        grid.getChildren().addAll(backgroundCredit, gameEndClipCredit, codeCredit, closeButton);

        Scene scene = new Scene(grid, 430, 328);
        window.setScene(scene);
        window.showAndWait();
    }

}