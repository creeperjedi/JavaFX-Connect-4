import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.Random;

public class Connect_4 extends Application {	
	//JavaFX Hierarchy: Stage -> Scene -> (Grid)Pane
    Stage window;
    Scene mainMenu, gameScreen;
    
    Pane boardPane = new Pane();
    
    Label topLabel = new Label("Welcome to JavaFX Connect-4");
    
    ImageView boardOverlay;
    
    Column column1 = new Column();
    Column column2 = new Column();
    Column column3 = new Column();
    Column column4 = new Column();
    Column column5 = new Column();
    Column column6 = new Column();
    Column column7 = new Column();
    
    
    //Creates the Circles that will represent the game pieces
    /*Starts with A1 in the Top Left and F7 in the Bottom Right
     * A1, A2, A3, A4, A5, A6, A7
     * B1, B2, B3, B4, B5, B6, B7
     * C1, C2, C3, C4, C5, C6, C7
     * D1, D2, D3, D4, D5, D6, D7
     * E1, E2, E3, E4, E5, E6, E7
     * F1, F2, F3, F4, F5, F6, F7
     */
    Circle A1 = new Circle();
    Circle A2 = new Circle();
    Circle A3 = new Circle();
    Circle A4 = new Circle();
    Circle A5 = new Circle();
    Circle A6 = new Circle();
    Circle A7 = new Circle();
    Circle B1 = new Circle();
    Circle B2 = new Circle();
    Circle B3 = new Circle();
    Circle B4 = new Circle();
    Circle B5 = new Circle();
    Circle B6 = new Circle();
    Circle B7 = new Circle();
    Circle C1 = new Circle();
    Circle C2 = new Circle();
    Circle C3 = new Circle();
    Circle C4 = new Circle();
    Circle C5 = new Circle();
    Circle C6 = new Circle();
    Circle C7 = new Circle();
    Circle D1 = new Circle();
    Circle D2 = new Circle();
    Circle D3 = new Circle();
    Circle D4 = new Circle();
    Circle D5 = new Circle();
    Circle D6 = new Circle();
    Circle D7 = new Circle();
    Circle E1 = new Circle();
    Circle E2 = new Circle();
    Circle E3 = new Circle();
    Circle E4 = new Circle();
    Circle E5 = new Circle();
    Circle E6 = new Circle();
    Circle E7 = new Circle();
    Circle F1 = new Circle();
    Circle F2 = new Circle();
    Circle F3 = new Circle();
    Circle F4 = new Circle();
    Circle F5 = new Circle();
    Circle F6 = new Circle();
    Circle F7 = new Circle();

    AudioClip gameEnd = new AudioClip(this.getClass().getResource("Game.wav").toString());
    AudioClip backgroundMusic = new AudioClip(this.getClass().getResource("Background.wav").toString());
    
    String winner;
	double windowHeight;
	double windowWidth;
	double resizedWidth;
	double resizedHeight;
    int lastRow;
    int lastCol;
    int tempRow;
    int tempCol;
    int xMouse;
    int yMouse;
    int piecesOnBoard = 0;
    int completedGames = 0;
    char[][] backBoard = new char[6][7];
    boolean againstComputer = false;
    boolean playerHasWon = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	window = primaryStage;
    	
    	//Assigns the boardPane to the gameScreen Scene
        gameScreen = new Scene(boardPane);
        
        windowHeight = window.getHeight();
        windowWidth = window.getWidth();
        
        Image boardOverlayPNG = new Image("boardOverlay.png");
        boardOverlay = new ImageView(boardOverlayPNG);
        boardOverlay.setFitWidth(windowWidth);
        boardOverlay.setFitHeight(windowHeight);
        boardPane.getChildren().add(boardOverlay);
        
		//Triggers the humanInputMethod() and prints the X and Y coordinate for every mouse click on the board
        boardPane.setOnMouseClicked(e -> {
    	    xMouse=(int) e.getX();
    	    yMouse=(int) e.getY();
    	    System.out.println(xMouse+","+yMouse);//these co-ords are relative to the component
    	    tempRow = lastRow;
    	    tempCol = lastCol;
    	    humanInputMethod();
    	    if (againstComputer) {
    			if (tempRow != lastRow || tempCol != lastCol)
    				computerInputMethod();
    	    }
        });
        
    	//**Main Menu**
        Label pickOponent = new Label("Pick your opponent");
        
        //Checks for mouse click on the button and starts the game
        Button startGameHuman = new Button("Another human");
        startGameHuman.setOnAction(e -> {
        	//Saves the dimensions of the mainMenu window and sets the Scene to gameScreen
            windowHeight = window.getHeight();
            windowWidth = window.getWidth();
            window.setScene(gameScreen);
            
            /* Applies the saved dimensions to the new window to keep the sizes consistent
             * Adds or subtracts to trigger the Listener so that the boardOverlay can be properly sized
             * Alternates between adding or subtracting every round to prevent compounding changes in the size of the board
             */
            if (completedGames % 2 == 0) {
	            window.setWidth(windowWidth + 1);
	            window.setHeight(windowHeight + 1);
            }
            else {
                window.setWidth(windowWidth - 1);
                window.setHeight(windowHeight - 1);
            }
            
            backgroundMusic.play();
            
        	//window.setFullScreen(true);
        	window.centerOnScreen();
        	});
        Button startGameComputer = new Button("The computer");
        startGameComputer.setOnAction(e -> {
            windowHeight = window.getHeight();
            windowWidth = window.getWidth();
            window.setScene(gameScreen);
            
            if (completedGames % 2 == 0) {
	            window.setWidth(windowWidth + 1);
	            window.setHeight(windowHeight + 1);
            }
            else {
                window.setWidth(windowWidth - 1);
                window.setHeight(windowHeight - 1);
            }
            
            againstComputer = true;
            
            backgroundMusic.play();
            
        	//window.setFullScreen(true);
        	window.centerOnScreen();
        	});
        
        
        /* Listens for changes in the dimensions of the Scene, and resizes the boardOverlay accordingly
         * Also repositions the Circles so that they stay in their respective positions
         */
        gameScreen.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                boardOverlay.setFitWidth((double) newSceneWidth);
                resizedWidth = (double) newSceneWidth;
                
                //Sets the rightBorder for each Column
        		column1.setBorder(resizedWidth / 7);
        		column2.setBorder(resizedWidth / 7 * 2);
        		column3.setBorder(resizedWidth / 7 * 3);
        		column4.setBorder(resizedWidth / 7 * 4);     		
        		column5.setBorder(resizedWidth / 7 * 5);        		
        		column6.setBorder(resizedWidth / 7 * 6);       		
        		column7.setBorder(resizedWidth / 7 * 7);
        		
        		//Sets the Center for each Column
        		column1.setCenter(0);
        		column2.setCenter(column1.getBorder());
        		column3.setCenter(column2.getBorder());        		
        		column4.setCenter(column3.getBorder());
        		column5.setCenter(column4.getBorder());
        		column6.setCenter(column5.getBorder());
        		column7.setCenter(column6.getBorder());
        		
        		//Sets the CenterX for each Circle
        		A1.setCenterX(column1.getCenter());	B1.setCenterX(column1.getCenter());	C1.setCenterX(column1.getCenter());
        		D1.setCenterX(column1.getCenter());	E1.setCenterX(column1.getCenter());	F1.setCenterX(column1.getCenter());
        		
        		A2.setCenterX(column2.getCenter());	B2.setCenterX(column2.getCenter());	C2.setCenterX(column2.getCenter());
        		D2.setCenterX(column2.getCenter());	E2.setCenterX(column2.getCenter());	F2.setCenterX(column2.getCenter());
        		
        		A3.setCenterX(column3.getCenter());	B3.setCenterX(column3.getCenter());	C3.setCenterX(column3.getCenter());
        		D3.setCenterX(column3.getCenter());	E3.setCenterX(column3.getCenter());	F3.setCenterX(column3.getCenter());
        		
        		A4.setCenterX(column4.getCenter());	B4.setCenterX(column4.getCenter()); C4.setCenterX(column4.getCenter());
        		D4.setCenterX(column4.getCenter()); E4.setCenterX(column4.getCenter());	F4.setCenterX(column4.getCenter());
        		
        		A5.setCenterX(column5.getCenter());	B5.setCenterX(column5.getCenter()); C5.setCenterX(column5.getCenter());
        		D5.setCenterX(column5.getCenter()); E5.setCenterX(column5.getCenter());	F5.setCenterX(column5.getCenter());
        		
        		A6.setCenterX(column6.getCenter());	B6.setCenterX(column6.getCenter()); C6.setCenterX(column6.getCenter());
        		D6.setCenterX(column6.getCenter()); E6.setCenterX(column6.getCenter());	F6.setCenterX(column6.getCenter());
        		
        		A7.setCenterX(column7.getCenter());	B7.setCenterX(column7.getCenter()); C7.setCenterX(column7.getCenter());
        		D7.setCenterX(column7.getCenter()); E7.setCenterX(column7.getCenter());	F7.setCenterX(column7.getCenter());
        		
        		//Sets the Radius for each Circle
        		double circleRadius = column1.getBorder() / 2;
        		A1.setRadius(circleRadius);	B1.setRadius(circleRadius);   C1.setRadius(circleRadius);
        		D1.setRadius(circleRadius);   E1.setRadius(circleRadius);	F1.setRadius(circleRadius);
        		
        		A2.setRadius(circleRadius);	B2.setRadius(circleRadius);   C2.setRadius(circleRadius);
        		D2.setRadius(circleRadius);   E2.setRadius(circleRadius);	F2.setRadius(circleRadius);
        		
        		A3.setRadius(circleRadius);	B3.setRadius(circleRadius);   C3.setRadius(circleRadius);
        		D3.setRadius(circleRadius);   E3.setRadius(circleRadius);	F3.setRadius(circleRadius);
        		
        		A4.setRadius(circleRadius);	B4.setRadius(circleRadius);   C4.setRadius(circleRadius);
        		D4.setRadius(circleRadius);   E4.setRadius(circleRadius);	F4.setRadius(circleRadius);
        		
        		A5.setRadius(circleRadius);	B5.setRadius(circleRadius);   C5.setRadius(circleRadius);
        		D5.setRadius(circleRadius);   E5.setRadius(circleRadius);	F5.setRadius(circleRadius);
        		
        		A6.setRadius(circleRadius);	B6.setRadius(circleRadius);   C6.setRadius(circleRadius);
        		D6.setRadius(circleRadius);   E6.setRadius(circleRadius);	F6.setRadius(circleRadius);
        		
        		A7.setRadius(circleRadius);	B7.setRadius(circleRadius);   C7.setRadius(circleRadius);
        		D7.setRadius(circleRadius);   E7.setRadius(circleRadius);	F7.setRadius(circleRadius);
        		
            }
        });
        gameScreen.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
                boardOverlay.setFitHeight((double) newSceneHeight);
                resizedHeight = (double) newSceneHeight;
                A1.setCenterY(resizedHeight / 6 / 2);	A2.setCenterY(resizedHeight / 6 / 2);	A3.setCenterY(resizedHeight / 6 / 2);
                A4.setCenterY(resizedHeight / 6 / 2);	A5.setCenterY(resizedHeight / 6 / 2);
                A6.setCenterY(resizedHeight / 6 / 2);   A7.setCenterY(resizedHeight / 6 / 2);
                
                B1.setCenterY(resizedHeight / 6 / 2 * 3);	B2.setCenterY(resizedHeight / 6 / 2 * 3);	B3.setCenterY(resizedHeight / 6 / 2 * 3);
                B4.setCenterY(resizedHeight / 6 / 2 * 3);	B5.setCenterY(resizedHeight / 6 / 2 * 3);
                B6.setCenterY(resizedHeight / 6 / 2 * 3);   B7.setCenterY(resizedHeight / 6 / 2 * 3);
                
                C1.setCenterY(resizedHeight / 6 / 2 * 5);	C2.setCenterY(resizedHeight / 6 / 2 * 5);	C3.setCenterY(resizedHeight / 6 / 2 * 5);
                C4.setCenterY(resizedHeight / 6 / 2 * 5);	C5.setCenterY(resizedHeight / 6 / 2 * 5);
                C6.setCenterY(resizedHeight / 6 / 2 * 5);   C7.setCenterY(resizedHeight / 6 / 2 * 5);
                
                D1.setCenterY(resizedHeight / 6 / 2 * 7);	D2.setCenterY(resizedHeight / 6 / 2 * 7);	D3.setCenterY(resizedHeight / 6 / 2 * 7);
                D4.setCenterY(resizedHeight / 6 / 2 * 7);	D5.setCenterY(resizedHeight / 6 / 2 * 7);
                D6.setCenterY(resizedHeight / 6 / 2 * 7);   D7.setCenterY(resizedHeight / 6 / 2 * 7);
                
                E1.setCenterY(resizedHeight / 6 / 2 * 9);	E2.setCenterY(resizedHeight / 6 / 2 * 9);	E3.setCenterY(resizedHeight / 6 / 2 * 9);
                E4.setCenterY(resizedHeight / 6 / 2 * 9);	E5.setCenterY(resizedHeight / 6 / 2 * 9);
                E6.setCenterY(resizedHeight / 6 / 2 * 9);   E7.setCenterY(resizedHeight / 6 / 2 * 9);
                
                F1.setCenterY(resizedHeight / 6 / 2 * 11);	F2.setCenterY(resizedHeight / 6 / 2 * 11);	F3.setCenterY(resizedHeight / 6 / 2 * 11);
                F4.setCenterY(resizedHeight / 6 / 2 * 11);	F5.setCenterY(resizedHeight / 6 / 2 * 11);
                F6.setCenterY(resizedHeight / 6 / 2 * 11);   F7.setCenterY(resizedHeight / 6 / 2 * 11);
            }
        });
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        //Add Main Menu Objects to GridPane
        GridPane.setConstraints(topLabel, 1, 1, 3, 1);
        GridPane.setConstraints(pickOponent, 1, 6, 3, 1);
        GridPane.setHalignment(pickOponent, HPos.CENTER);
        
        GridPane.setConstraints(startGameHuman, 1, 7);
        GridPane.setConstraints(startGameComputer, 3, 7);
        
        grid.getChildren().addAll(topLabel, /*blueThickLabel, redThickLabel, blueThin, blueThick, redThin, redThick, redSolid, */
        		pickOponent, startGameHuman, startGameComputer);
        	
        //Main Menu Display
    	window.setTitle("JavaFX Connect 4");
        mainMenu = new Scene(grid, 300, 250);
        window.setScene(mainMenu);  
        window.show();
        window.centerOnScreen();
    }

    public void humanInputMethod() {
		Color currentColor;
		char currentChar;
		if (piecesOnBoard % 2 == 0) {
			currentColor = Color.YELLOW;
			currentChar = 'Y';
		}
		else {
			currentColor = Color.RED;
			currentChar = 'R';
		}
			
        if (xMouse < column1.getBorder() && column1.getPieces() < 6) {
        	System.out.println("column1");
        	if (!boardPane.getChildren().contains(F1)) {
        		F1.setFill(currentColor);
        		backBoard[5][0] = currentChar;
        		lastRow = 5;
        		lastCol = 0;
        		boardPane.getChildren().add(F1);
        	}
        	else if (!boardPane.getChildren().contains(E1)) {
        		E1.setFill(currentColor);
        		backBoard[4][0] = currentChar;
        		lastRow = 4;
        		lastCol = 0;
        		boardPane.getChildren().add(E1);
        	}
        	else if (!boardPane.getChildren().contains(D1)) {
        		D1.setFill(currentColor);
        		backBoard[3][0] = currentChar;
        		lastRow = 3;
        		lastCol = 0;
        		boardPane.getChildren().add(D1);
        	}
        	else if (!boardPane.getChildren().contains(C1)) {
        		C1.setFill(currentColor);
        		backBoard[2][0] = currentChar;
        		lastRow = 2;
        		lastCol = 0;
        		boardPane.getChildren().add(C1);
        	}
        	else if (!boardPane.getChildren().contains(B1)) {
        		B1.setFill(currentColor);
        		backBoard[1][0] = currentChar;
        		lastRow = 1;
        		lastCol = 0;
        		boardPane.getChildren().add(B1);
        	}
        	else if (!boardPane.getChildren().contains(A1)) {
        		A1.setFill(currentColor);
        		backBoard[0][0] = currentChar;
        		lastRow = 0;
        		lastCol = 0;
        		boardPane.getChildren().add(A1);
        	}
        	column1.addPiece();
        }
        
        else if (xMouse >= column1.getBorder() && xMouse < column2.getBorder() && column2.getPieces() < 6) {
        	System.out.println("column2");
        	if (!boardPane.getChildren().contains(F2)) {
        		F2.setFill(currentColor);
        		backBoard[5][1] = currentChar;
        		lastRow = 5;
        		lastCol = 1;
        		boardPane.getChildren().add(F2);
        	}
        	else if (!boardPane.getChildren().contains(E2)) {
        		E2.setFill(currentColor);
        		backBoard[4][1] = currentChar;
        		lastRow = 4;
        		lastCol = 1;
        		boardPane.getChildren().add(E2);
        	}
        	else if (!boardPane.getChildren().contains(D2)) {
        		D2.setFill(currentColor);
        		backBoard[3][1] = currentChar;
        		lastRow = 3;
        		lastCol = 1;
        		boardPane.getChildren().add(D2);
        	}
        	else if (!boardPane.getChildren().contains(C2)) {
        		C2.setFill(currentColor);
        		backBoard[2][1] = currentChar;
        		lastRow = 2;
        		lastCol = 1;
        		boardPane.getChildren().add(C2);
        	}
        	else if (!boardPane.getChildren().contains(B2)) {
        		B2.setFill(currentColor);
        		backBoard[1][1] = currentChar;
        		lastRow = 1;
        		lastCol = 1;
        		boardPane.getChildren().add(B2);
        	}
        	else if (!boardPane.getChildren().contains(A2)) {
        		A2.setFill(currentColor);
        		backBoard[0][1] = currentChar;
        		lastRow = 0;
        		lastCol = 1;
        		boardPane.getChildren().add(A2);
        	}
        	column2.addPiece();
        }
        
        else if (xMouse >= column2.getBorder() && xMouse < column3.getBorder() && column3.getPieces() < 6) {
        	System.out.println("column3");
        	if (!boardPane.getChildren().contains(F3)) {
        		F3.setFill(currentColor);
        		backBoard[5][2] = currentChar;
        		lastRow = 5;
        		lastCol = 2;
        		boardPane.getChildren().add(F3);
        	}
        	else if (!boardPane.getChildren().contains(E3)) {
        		E3.setFill(currentColor);
        		backBoard[4][2] = currentChar;
        		lastRow = 4;
        		lastCol = 2;
        		boardPane.getChildren().add(E3);
        	}
        	else if (!boardPane.getChildren().contains(D3)) {
        		D3.setFill(currentColor);
        		backBoard[3][2] = currentChar;
        		lastRow = 3;
        		lastCol = 2;
        		boardPane.getChildren().add(D3);
        	}
        	else if (!boardPane.getChildren().contains(C3)) {
        		C3.setFill(currentColor);
        		backBoard[2][2] = currentChar;
        		lastRow = 2;
        		lastCol = 2;
        		boardPane.getChildren().add(C3);
        	}
        	else if (!boardPane.getChildren().contains(B3)) {
        		B3.setFill(currentColor);
        		backBoard[1][2] = currentChar;
        		lastRow = 1;
        		lastCol = 2;
        		boardPane.getChildren().add(B3);
        	}
        	else if (!boardPane.getChildren().contains(A3)) {
        		A3.setFill(currentColor);
        		backBoard[0][2] = currentChar;
        		lastRow = 0;
        		lastCol = 2;
        		boardPane.getChildren().add(A3);
        	}
        	column3.addPiece();
        }
        
        else if (xMouse >= column3.getBorder() && xMouse < column4.getBorder() && column4.getPieces() < 6) {
        	System.out.println("column4");
        	if (!boardPane.getChildren().contains(F4)) {
        		F4.setFill(currentColor);
        		backBoard[5][3] = currentChar;
        		lastRow = 5;
        		lastCol = 3;
        		boardPane.getChildren().add(F4);
        	}
        	else if (!boardPane.getChildren().contains(E4)) {
        		E4.setFill(currentColor);
        		backBoard[4][3] = currentChar;
        		lastRow = 4;
        		lastCol = 3;
        		boardPane.getChildren().add(E4);
        	}
        	else if (!boardPane.getChildren().contains(D4)) {
        		D4.setFill(currentColor);
        		backBoard[3][3] = currentChar;
        		lastRow = 3;
        		lastCol = 3;
        		boardPane.getChildren().add(D4);
        	}
        	else if (!boardPane.getChildren().contains(C4)) {
        		C4.setFill(currentColor);
        		backBoard[2][3] = currentChar;
        		lastRow = 2;
        		lastCol = 3;
        		boardPane.getChildren().add(C4);
        	}
        	else if (!boardPane.getChildren().contains(B4)) {
        		B4.setFill(currentColor);
        		backBoard[1][3] = currentChar;
        		lastRow = 1;
        		lastCol = 3;
        		boardPane.getChildren().add(B4);
        	}
        	else if (!boardPane.getChildren().contains(A4)) {
        		A4.setFill(currentColor);
        		backBoard[0][3] = currentChar;
        		lastRow = 0;
        		lastCol = 3;
        		boardPane.getChildren().add(A4);
        	}
        	column4.addPiece();
        }
        
        else if (xMouse >= column4.getBorder() && xMouse < column5.getBorder() && column5.getPieces() < 6) {
        	System.out.println("column5");
        	if (!boardPane.getChildren().contains(F5)) {
        		F5.setFill(currentColor);
        		backBoard[5][4] = currentChar;
        		lastRow = 5;
        		lastCol = 4;
        		boardPane.getChildren().add(F5);
        	}
        	else if (!boardPane.getChildren().contains(E5)) {
        		E5.setFill(currentColor);
        		backBoard[4][4] = currentChar;
        		lastRow = 4;
        		lastCol = 4;
        		boardPane.getChildren().add(E5);
        	}
        	else if (!boardPane.getChildren().contains(D5)) {
        		D5.setFill(currentColor);
        		backBoard[3][4] = currentChar;
        		lastRow = 3;
        		lastCol = 4;
        		boardPane.getChildren().add(D5);
        	}
        	else if (!boardPane.getChildren().contains(C5)) {
        		C5.setFill(currentColor);
        		backBoard[2][4] = currentChar;
        		lastRow = 2;
        		lastCol = 4;
        		boardPane.getChildren().add(C5);
        	}
        	else if (!boardPane.getChildren().contains(B5)) {
        		B5.setFill(currentColor);
        		backBoard[1][4] = currentChar;
        		lastRow = 1;
        		lastCol = 4;
        		boardPane.getChildren().add(B5);
        	}
        	else if (!boardPane.getChildren().contains(A5)) {
        		A5.setFill(currentColor);
        		backBoard[0][4] = currentChar;
        		lastRow = 0;
        		lastCol = 4;
        		boardPane.getChildren().add(A5);
        	}
        	column5.addPiece();
        }
        
        else if (xMouse >= column5.getBorder() && xMouse < column6.getBorder() && column6.getPieces() < 6) {
        	System.out.println("column6");
        	if (!boardPane.getChildren().contains(F6)) {
        		F6.setFill(currentColor);
        		backBoard[5][5] = currentChar;
        		lastRow = 5;
        		lastCol = 5;
        		boardPane.getChildren().add(F6);
        	}
        	else if (!boardPane.getChildren().contains(E6)) {
        		E6.setFill(currentColor);
        		backBoard[4][5] = currentChar;
        		lastRow = 4;
        		lastCol = 5;
        		boardPane.getChildren().add(E6);
        	}
        	else if (!boardPane.getChildren().contains(D6)) {
        		D6.setFill(currentColor);
        		backBoard[3][5] = currentChar;
        		lastRow = 3;
        		lastCol = 5;
        		boardPane.getChildren().add(D6);
        	}
        	else if (!boardPane.getChildren().contains(C6)) {
        		C6.setFill(currentColor);
        		backBoard[2][5] = currentChar;
        		lastRow = 2;
        		lastCol = 5;
        		boardPane.getChildren().add(C6);
        	}
        	else if (!boardPane.getChildren().contains(B6)) {
        		B6.setFill(currentColor);
        		backBoard[1][5] = currentChar;
        		lastRow = 1;
        		lastCol = 5;
        		boardPane.getChildren().add(B6);
        	}
        	else if (!boardPane.getChildren().contains(A6)) {
        		A6.setFill(currentColor);
        		backBoard[0][5] = currentChar;
        		lastRow = 0;
        		lastCol = 5;
        		boardPane.getChildren().add(A6);
        	}
        	column6.addPiece();
        }
        
        else if (xMouse >= column6.getBorder() && xMouse < column7.getBorder() && column7.getPieces() < 6) {
        	System.out.println("column7");
        	if (!boardPane.getChildren().contains(F7)) {
        		F7.setFill(currentColor);
        		backBoard[5][6] = currentChar;
        		lastRow = 5;
        		lastCol = 6;
        		boardPane.getChildren().add(F7);
        	}
        	else if (!boardPane.getChildren().contains(E7)) {
        		E7.setFill(currentColor);
        		backBoard[4][6] = currentChar;
        		lastRow = 4;
        		lastCol = 6;
        		boardPane.getChildren().add(E7);
        	}
        	else if (!boardPane.getChildren().contains(D7)) {
        		D7.setFill(currentColor);
        		backBoard[3][6] = currentChar;
        		lastRow = 3;
        		lastCol = 6;
        		boardPane.getChildren().add(D7);
        	}
        	else if (!boardPane.getChildren().contains(C7)) {
        		C7.setFill(currentColor);
        		backBoard[2][6] = currentChar;
        		lastRow = 2;
        		lastCol = 6;
        		boardPane.getChildren().add(C7);
        	}
        	else if (!boardPane.getChildren().contains(B7)) {
        		B7.setFill(currentColor);
        		backBoard[1][6] = currentChar;
        		lastRow = 1;
        		lastCol = 6;
        		boardPane.getChildren().add(B7);
        	}
        	else if (!boardPane.getChildren().contains(A7)) {
        		A7.setFill(currentColor);
        		backBoard[0][6] = currentChar;
        		lastRow = 0;
        		lastCol = 6;
        		boardPane.getChildren().add(A7);
        	}
        	column7.addPiece();
        }
        
        piecesOnBoard = column1.getPieces() + column2.getPieces() + column3.getPieces()
        		+ column4.getPieces() + column5.getPieces() + column6.getPieces() + column7.getPieces();
        System.out.println("Number of pieces on the board: " + piecesOnBoard);
    	boardPane.getChildren().remove(boardOverlay);
    	boardPane.getChildren().add(boardOverlay);
    	checkWinMethod();
	}
	
	public void computerInputMethod() {
		Random rand = new Random();
		while (true) {
			xMouse = rand.nextInt((int) column7.getBorder());
    	    tempRow = lastRow;
    	    tempCol = lastCol;
    	    humanInputMethod();
    		if (tempRow != lastRow || tempCol != lastCol) {
    				break;
    		}
			//Breaks if there is one spot left
			else if (piecesOnBoard == 42)
				break;
		}
		
	}

	public void checkWinMethod() {
		if (piecesOnBoard == 42) 
			winner = "It's a Tie!";
		else if (backBoard[lastRow][lastCol] == 'Y')
			winner = "Yellow Wins!";
		else
			winner = "Red Wins!";
		//Checks if the last piece's color is equal to the ...
		//3 to the Left
		if (lastCol-3 >= 0)
			if (backBoard[lastRow][lastCol-3] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow][lastCol-2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow][lastCol-1] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		//2 to the Left and 1 to the Right
		if (lastCol-2 >= 0 && lastCol+1 <=6)
			if (backBoard[lastRow][lastCol-2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow][lastCol-1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow][lastCol+1] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		//1 to the Left and 2 to the Right
		if (lastCol-1 >= 0 && lastCol+2 <=6)
			if (backBoard[lastRow][lastCol-1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow][lastCol+1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow][lastCol+2] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		//3 to the Right
		if (lastCol+3 <=6)
			if (backBoard[lastRow][lastCol+1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow][lastCol+2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow][lastCol+3] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		//3 Down
		if (lastRow+3 <=5)
			if (backBoard[lastRow+1][lastCol] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+2][lastCol] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+3][lastCol] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		//3 to the bottom Left
		if (lastCol-3 >= 0 && lastRow+3 <=5)
			if (backBoard[lastRow+1][lastCol-1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+2][lastCol-2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+3][lastCol-3] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		//2 to the bottom Left and 1 to the top Right
		if (lastCol - 2 >= 0 && lastRow + 2 <= 5 && lastCol + 1 <= 6 && lastRow - 1 >= 0)
			if (backBoard[lastRow-1][lastCol+1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+1][lastCol-1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+2][lastCol-2] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		//1 to the bottom Left and 2 to the top Right
		if (lastCol - 1 >= 0 && lastRow + 1 <= 5 && lastCol + 2 <= 6 && lastRow - 2 >= 0)
			if (backBoard[lastRow-2][lastCol+2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow-1][lastCol+1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+1][lastCol-1] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		//3 to the bottom Right
		if (lastCol+3 <=6 && lastRow+3 <=5)
			if (backBoard[lastRow+1][lastCol+1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+2][lastCol+2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+3][lastCol+3] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		
		//2 to the bottom Right and 1 to the top Left
		if (lastCol - 1 >= 0 && lastRow + 2 <= 5 && lastCol + 2 <= 6 && lastRow - 1 >= 0)
			if (backBoard[lastRow-1][lastCol-1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+1][lastCol+1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+2][lastCol+2] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		
		//1 to the bottom Right and 2 to the top Left
		if (lastCol - 2 >= 0 && lastRow + 1 <= 5 && lastCol + 1 <= 6 && lastRow - 2 >= 0)
			if (backBoard[lastRow-2][lastCol-2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow-1][lastCol-1] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow+1][lastCol+1] == backBoard[lastRow][lastCol])
				playerHasWon = true;
		
		if (playerHasWon)
			restartGameMethod();
		
	}
	
	public void restartGameMethod() {
		backgroundMusic.stop();
		gameEnd.play();
		column1.resetPieces(); column2.resetPieces(); column3.resetPieces(); column4.resetPieces();
		column5.resetPieces(); column6.resetPieces(); column7.resetPieces(); 
		
		piecesOnBoard = 0;
		
		topLabel.setText(winner);
		topLabel.setTextFill(Color.GREEN);
		window.setScene(mainMenu);
		window.centerOnScreen();
		
		boardPane.getChildren().clear();
		boardPane.getChildren().add(boardOverlay);
		backBoard = new char[6][7];
		
		completedGames++;
		againstComputer = false;
		playerHasWon = false;
	}
}