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
    GamePiece A1 = new GamePiece('A', 1);
    GamePiece A2 = new GamePiece('A', 2);
    GamePiece A3 = new GamePiece('A', 3);
    GamePiece A4 = new GamePiece('A', 4);
    GamePiece A5 = new GamePiece('A', 5);
    GamePiece A6 = new GamePiece('A', 6);
    GamePiece A7 = new GamePiece('A', 7);
    GamePiece B1 = new GamePiece('B', 1);
    GamePiece B2 = new GamePiece('B', 2);
    GamePiece B3 = new GamePiece('B', 3);
    GamePiece B4 = new GamePiece('B', 4);
    GamePiece B5 = new GamePiece('B', 5);
    GamePiece B6 = new GamePiece('B', 6);
    GamePiece B7 = new GamePiece('B', 7);
    GamePiece C1 = new GamePiece('C', 1);
    GamePiece C2 = new GamePiece('C', 2);
    GamePiece C3 = new GamePiece('C', 3);
    GamePiece C4 = new GamePiece('C', 4);
    GamePiece C5 = new GamePiece('C', 5);
    GamePiece C6 = new GamePiece('C', 6);
    GamePiece C7 = new GamePiece('C', 7);
    GamePiece D1 = new GamePiece('D', 1);
    GamePiece D2 = new GamePiece('D', 2);
    GamePiece D3 = new GamePiece('D', 3);
    GamePiece D4 = new GamePiece('D', 4);
    GamePiece D5 = new GamePiece('D', 5);
    GamePiece D6 = new GamePiece('D', 6);
    GamePiece D7 = new GamePiece('D', 7);
    GamePiece E1 = new GamePiece('E', 1);
    GamePiece E2 = new GamePiece('E', 2);
    GamePiece E3 = new GamePiece('E', 3);
    GamePiece E4 = new GamePiece('E', 4);
    GamePiece E5 = new GamePiece('E', 5);
    GamePiece E6 = new GamePiece('E', 6);
    GamePiece E7 = new GamePiece('E', 7);
    GamePiece F1 = new GamePiece('F', 1);
    GamePiece F2 = new GamePiece('F', 2);
    GamePiece F3 = new GamePiece('F', 3);
    GamePiece F4 = new GamePiece('F', 4);
    GamePiece F5 = new GamePiece('F', 5);
    GamePiece F6 = new GamePiece('F', 6);
    GamePiece F7 = new GamePiece('F', 7);
    GamePiece[] col1GamePieces = new GamePiece[]{F1, E1, D1, C1, B1, A1};
    GamePiece[] col2GamePieces = new GamePiece[]{F2, E2, D2, C2, B2, A2};
    GamePiece[] col3GamePieces = new GamePiece[]{F3, E3, D3, C3, B3, A3};
    GamePiece[] col4GamePieces = new GamePiece[]{F4, E4, D4, C4, B4, A4};
    GamePiece[] col5GamePieces = new GamePiece[]{F5, E5, D5, C5, B5, A5};
    GamePiece[] col6GamePieces = new GamePiece[]{F6, E6, D6, C6, B6, A6};
    GamePiece[] col7GamePieces = new GamePiece[]{F7, E7, D7, C7, B7, A7};
    
    AudioClip gameEnd = new AudioClip(this.getClass().getResource("/res/gameOver.wav").toString());
    AudioClip backgroundMusic = new AudioClip(this.getClass().getResource("/res/backgroundMusic.mp3").toString());
    
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
        
        Image boardOverlayPNG = new Image("/res/boardOverlay.png");
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
        
        Button viewCredits = new Button("View credits");
        viewCredits.setOnAction(e -> {
        	CreditsWindow.display();
        	});
        
        /* Listens for changes in the dimensions of the Scene, and resizes the boardOverlay accordingly
         * Also repositions the Circles so that they stay in their respective positions
         */
        gameScreen.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                boardOverlay.setFitWidth((double) newSceneWidth);
                resizedWidth = (double) newSceneWidth;
                double circleRadius = column1.getBorder() / 2;
                
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
        		
        		//Sets the CenterX and Radius for each Circle
        		for(GamePiece piece: col1GamePieces) {
        			piece.setCenterX(column1.getCenter());
        			piece.setRadius(circleRadius);
        		}
        		
        		for(GamePiece piece: col2GamePieces) {
        			piece.setCenterX(column2.getCenter());
        			piece.setRadius(circleRadius);
        		}
        		
        		for(GamePiece piece: col3GamePieces) {
        			piece.setCenterX(column3.getCenter());
        			piece.setRadius(circleRadius);
        		}
        		
        		for(GamePiece piece: col4GamePieces) {
        			piece.setCenterX(column4.getCenter());
        			piece.setRadius(circleRadius);
        		}
        		
        		for(GamePiece piece: col5GamePieces) {
        			piece.setCenterX(column5.getCenter());
        			piece.setRadius(circleRadius);
        		}
        		
        		for(GamePiece piece: col6GamePieces) {
        			piece.setCenterX(column6.getCenter());
        			piece.setRadius(circleRadius);
        		}
        		
        		for(GamePiece piece: col7GamePieces) {
        			piece.setCenterX(column7.getCenter());
        			piece.setRadius(circleRadius);
        		}        		
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
        GridPane.setConstraints(viewCredits, 1, 9, 3, 1);
        GridPane.setFillWidth(viewCredits, true);
        viewCredits.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.getChildren().addAll(topLabel, pickOponent, startGameHuman, startGameComputer, viewCredits);
        	
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
        	for(GamePiece piece: col1GamePieces) {
            	if (!boardPane.getChildren().contains(piece)) {
            		piece.setFill(currentColor);
            		lastRow = piece.getRow() - 1;
            		lastCol = piece.getCol() - 1;
            		backBoard[lastRow][lastCol] = currentChar;
            		boardPane.getChildren().add(piece);
            		break;
            	}
        	}
        	column1.addPiece();
        }
        
        else if (xMouse >= column1.getBorder() && xMouse < column2.getBorder() && column2.getPieces() < 6) {
        	System.out.println("column2");
        	for(GamePiece piece: col2GamePieces) {
            	if (!boardPane.getChildren().contains(piece)) {
            		piece.setFill(currentColor);
            		lastRow = piece.getRow() - 1;
            		lastCol = piece.getCol() - 1;
            		backBoard[lastRow][lastCol] = currentChar;
            		boardPane.getChildren().add(piece);
            		break;
            	}
        	}
        	column2.addPiece();
        }
        
        else if (xMouse >= column2.getBorder() && xMouse < column3.getBorder() && column3.getPieces() < 6) {
        	System.out.println("column3");
        	for(GamePiece piece: col3GamePieces) {
            	if (!boardPane.getChildren().contains(piece)) {
            		piece.setFill(currentColor);
            		lastRow = piece.getRow() - 1;
            		lastCol = piece.getCol() - 1;
            		backBoard[lastRow][lastCol] = currentChar;
            		boardPane.getChildren().add(piece);
            		break;
            	}
        	}
        	column3.addPiece();
        }
        
        else if (xMouse >= column3.getBorder() && xMouse < column4.getBorder() && column4.getPieces() < 6) {
        	System.out.println("column4");
        	for(GamePiece piece: col4GamePieces) {
            	if (!boardPane.getChildren().contains(piece)) {
            		piece.setFill(currentColor);
            		lastRow = piece.getRow() - 1;
            		lastCol = piece.getCol() - 1;
            		backBoard[lastRow][lastCol] = currentChar;
            		boardPane.getChildren().add(piece);
            		break;
            	}
        	}
        	column4.addPiece();
        }
        
        else if (xMouse >= column4.getBorder() && xMouse < column5.getBorder() && column5.getPieces() < 6) {
        	System.out.println("column5");
        	for(GamePiece piece: col5GamePieces) {
            	if (!boardPane.getChildren().contains(piece)) {
            		piece.setFill(currentColor);
            		lastRow = piece.getRow() - 1;
            		lastCol = piece.getCol() - 1;
            		backBoard[lastRow][lastCol] = currentChar;
            		boardPane.getChildren().add(piece);
            		break;
            	}
        	}
        	column5.addPiece();
        }
        
        else if (xMouse >= column5.getBorder() && xMouse < column6.getBorder() && column6.getPieces() < 6) {
        	System.out.println("column6");
        	for(GamePiece piece: col6GamePieces) {
            	if (!boardPane.getChildren().contains(piece)) {
            		piece.setFill(currentColor);
            		lastRow = piece.getRow() - 1;
            		lastCol = piece.getCol() - 1;
            		backBoard[lastRow][lastCol] = currentChar;
            		boardPane.getChildren().add(piece);
            		break;
            	}
        	}
        	column6.addPiece();
        }
        
        else if (xMouse >= column6.getBorder() && xMouse < column7.getBorder() && column7.getPieces() < 6) {
        	System.out.println("column7");
        	for(GamePiece piece: col7GamePieces) {
            	if (!boardPane.getChildren().contains(piece)) {
            		piece.setFill(currentColor);
            		lastRow = piece.getRow() - 1;
            		lastCol = piece.getCol() - 1;
            		backBoard[lastRow][lastCol] = currentChar;
            		boardPane.getChildren().add(piece);
            		break;
            	}
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
		//3 to the top Right
		if (lastCol + 3 <= 6 && lastRow - 3 >= 0)
			if (backBoard[lastRow-3][lastCol+3] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow-2][lastCol+2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow-1][lastCol+1] == backBoard[lastRow][lastCol])
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
		//3 to the top Left
		if (lastCol - 3 >= 0 && lastRow - 3 >= 0)
			if (backBoard[lastRow-3][lastCol-3] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow-2][lastCol-2] == backBoard[lastRow][lastCol]
					&& backBoard[lastRow-1][lastCol-1] == backBoard[lastRow][lastCol])
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
