import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
    
    ToggleGroup difficulty;
    RadioButton randomDifficulty;
    RadioButton basicDifficulty;
    
    Label topLabel = new Label("Welcome to JavaFX Connect-4"); 
    
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
    Column column1 = new Column(new GamePiece[]{F1, E1, D1, C1, B1, A1});
    Column column2 = new Column(new GamePiece[]{F2, E2, D2, C2, B2, A2});
    Column column3 = new Column(new GamePiece[]{F3, E3, D3, C3, B3, A3});
    Column column4 = new Column(new GamePiece[]{F4, E4, D4, C4, B4, A4});
    Column column5 = new Column(new GamePiece[]{F5, E5, D5, C5, B5, A5});
    Column column6 = new Column(new GamePiece[]{F6, E6, D6, C6, B6, A6});
    Column column7 = new Column(new GamePiece[]{F7, E7, D7, C7, B7, A7});
    Column[] columnArray = new Column[] {column1, column2, column3, column4, column5, column6, column7};
    
    AudioClip gameEnd = new AudioClip(this.getClass().getResource("/res/gameOver.wav").toString());
    AudioClip backgroundMusic = new AudioClip(this.getClass().getResource("/res/backgroundMusic.mp3").toString());
    
    String winner;
	double windowHeight;
	double windowWidth;
	double resizedWidth;
	double resizedHeight;
    int recentRow;
    int recentCol;
    int previousRow;
    int previousCol;
    int xMouse;
    int yMouse;
    int piecesOnBoard = 0;
    int completedGames = 0;
    //Creates the 2d-array that will be used to check for game wins
    /* Organized like a traditional co-ordinate plane with an inverted y-axis
     * 0, 1, 2, 3, 4, 5, 6 < [][Columns] 
     * 1 
     * 2 
     * 3 
     * 4 
     * 5
     * ^ [Rows][] 
     */
    char[][] backBoard = new char[6][7];
    boolean againstComputer = false;
    boolean placementChosen = false;
    boolean playerHasWon = false;
    boolean prepareRestart = false;
    boolean restartGame = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	window = primaryStage;
    	//Assigns the boardPane Pane to the gameScreen Scene
        gameScreen = new Scene(boardPane);
        boardPane.setBackground(new Background(new BackgroundFill(Color.rgb(30, 143, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        
        windowHeight = window.getHeight();
        windowWidth = window.getWidth();
        
		//Triggers the humanInputMethod() and prints the X and Y coordinate for every mouse click on the board
        boardPane.setOnMouseClicked(e -> {
    	    xMouse=(int) e.getX();
    	    yMouse=(int) e.getY();
    	    System.out.println(xMouse+","+yMouse);//these co-ords are relative to the component
    	    if (prepareRestart)
    	    	restartGameMethod();
    	    else {
	    	    previousRow = recentRow;
	    	    previousCol = recentCol;
	    	    humanInputMethod();
	    	    checkWinMethod();
	    	    if (prepareRestart == false && againstComputer) {
	    			if (previousRow != recentRow || previousCol != recentCol) {
	    				computerInputMethod();	
	    	    	    checkWinMethod();
	    			}
	    	    }
    	    }
        });
        
    	//**Main Menu**
        Label pickOponent = new Label("Pick your opponent");
        
        Label pickDifficulty = new Label("Computer difficulty:");
        
        //Checks for mouse click on the button and starts the game
        Button startGameHuman = new Button("Another human");
        startGameHuman.setOnAction(e -> {
        	startGameMethod();
        	});
        Button startGameComputer = new Button("The computer");
        startGameComputer.setOnAction(e -> {
        	againstComputer = true;
        	startGameMethod();
        	});
        
        difficulty = new ToggleGroup();
        randomDifficulty = new RadioButton("Random");
        randomDifficulty.setToggleGroup(difficulty);
        randomDifficulty.setSelected(true);
        basicDifficulty = new RadioButton("Basic");
        basicDifficulty.setToggleGroup(difficulty);
        
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
        		
        		double circleRadius = column1.getBorder() / 2;
        		
        		//Sets the CenterX and Radius for each Circle in each Column
        		for (Column column: columnArray) {
        			for (GamePiece piece: column.getPieceArray()) {
            			piece.setCenterX(column.getCenter());
            			piece.setRadius(circleRadius);
        			}
        		}    		
            }
        });
        gameScreen.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
                resizedHeight = (double) newSceneHeight;
                
                /* Sets the y coordinate, centerY, for each GamePiece in each Column [this is calculated by 
                 * dividing the current height by 6 (for each row) then by 2 (to get the center of that row)
                 * the by multiplying by the transform variable which changes the centerY so that the GamePiece
                 * is in its appropriate row
                 */
                int transform = 11;
        		for (Column column: columnArray) {
        	        for (GamePiece piece: column.getPieceArray()) {
        	        		System.out.println(piece.name);
        	            	piece.setCenterY(resizedHeight / 6 / 2 * transform);
        	            	transform -= 2;
        	        }
        	        transform = 11;
        		}                
            }
        });
        
        //Adds every piece to the game board (boardPane) and sets their color to white to represent empty spaces in the board
		for (Column column: columnArray) {
	        for (GamePiece piece: column.getPieceArray()) {
	        	piece.setScaleX(0.80);
	        	piece.setScaleY(0.80);
	        	piece.setFill(Color.WHITE);
	        	boardPane.getChildren().add(piece);
	        }
	    }
        
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
        GridPane.setHalignment(pickDifficulty, HPos.RIGHT);
        GridPane.setConstraints(pickDifficulty, 1, 9);
        GridPane.setConstraints(randomDifficulty, 3, 9);
        GridPane.setConstraints(basicDifficulty, 3, 11);
        GridPane.setConstraints(viewCredits, 1, 13, 3, 1);
        GridPane.setFillWidth(viewCredits, true);
        viewCredits.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.getChildren().addAll(topLabel, pickOponent, startGameHuman, startGameComputer,
        		pickDifficulty, randomDifficulty, basicDifficulty, viewCredits);
        	
        //Main Menu Display
    	window.setTitle("JavaFX Connect 4");
        mainMenu = new Scene(grid, 300, 250);
        window.setScene(mainMenu);  
        window.show();
        window.centerOnScreen();
    }
    
    public void startGameMethod() {
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
		
		/* Checks if the x coordinate of the last click, xMouse, is inside of first column [this is tested by checking if
		 * xMouse is less than (to the left of) the right border of the column (retrieved by using .getBorder)]
		 * 
		 * If it is then it will check each piece starting from the bottom until it finds one that has not been placed
		 * 
		 * If the click is not within the the first column then it will check again with the next
		 * column and so on until there are no columns left
		 */
		for (Column column: columnArray) {
			if (xMouse <= column.getBorder() && column.getPieces() < 6) {
	        	for (GamePiece piece: column.getPieceArray()) {
	            	if (piece.getFill().equals(Color.WHITE)) {
	            		piece.setFill(currentColor);
	            		recentRow = piece.getRow() - 1;
	            		recentCol = piece.getCol() - 1;
	            		backBoard[recentRow][recentCol] = currentChar;
	            		break;
	            	}
	        	}
	        	column.addPiece();
	        	break;
			}
		}
		
        piecesOnBoard = column1.getPieces() + column2.getPieces() + column3.getPieces()
        		+ column4.getPieces() + column5.getPieces() + column6.getPieces() + column7.getPieces();
        System.out.println("Number of pieces on the board: " + piecesOnBoard);
	}
	
	public void computerInputMethod() {
		Random rand = new Random();
		while (true) {
			if (basicDifficulty.isSelected() == true) {
				placementChosen = false;
				System.out.println("Checks have begun");
				//Checks if the last piece's color is equal to the ...
				//2 to the Left
				if (recentCol-2 >= 0)
					if (backBoard[recentRow][recentCol-2] == backBoard[recentRow][recentCol]
							&& backBoard[recentRow][recentCol-1] == backBoard[recentRow][recentCol]) {
						//Tries to place a piece to the Left of the row of 3: ▪️ ⚪ ⚪ ⚫ 
						backBoardCalculator(-3);
						//Tries to place a piece to the Right of the row of 3:   ⚪ ⚪ ⚫ ▪️ 
						if (placementChosen == false)
							backBoardCalculator(+1);
					}
				//2 to the Right
				if (recentCol+2 <= 6 && placementChosen == false)
					if (backBoard[recentRow][recentCol+1] == backBoard[recentRow][recentCol]
							&& backBoard[recentRow][recentCol+2] == backBoard[recentRow][recentCol]) {
						//Tries to place a piece to the Right of the row of 3:   ⚫ ⚪ ⚪ ▪️ 
						backBoardCalculator(+3);
						//Tries to place a piece to the Left of the row of 3: ▪️ ⚫ ⚪ ⚪ 
						if (placementChosen == false)
							backBoardCalculator(-1);
					}
				//2 Down
				if (recentRow+2 <=5 && placementChosen == false)
					if (backBoard[recentRow+1][recentCol] == backBoard[recentRow][recentCol]
							&& backBoard[recentRow+2][recentCol] == backBoard[recentRow][recentCol]) {
						placementChosen = true;
					}
				System.out.println("Choice made: " + placementChosen);
			}
			if (!placementChosen) {
				xMouse = rand.nextInt((int) column7.getBorder());
			}
    	    previousRow = recentRow;
    	    previousCol = recentCol;
    	    humanInputMethod();
    		if (previousRow != recentRow || previousCol != recentCol) {
    				break;
    		}
		}
	}

	public void backBoardCalculator(int distance) {
		//Checks if the column being tested is valid
		if (recentCol+distance >= 0 && recentCol+distance <= 6)
			//Checks if the space being tested is empty
			if (backBoard[recentRow][recentCol+distance] == 0)
				//Checks if the row being tested is the bottom-most row
				if (recentRow == 5) {
					xMouse = (int) columnArray[recentCol+distance].getCenter();
					placementChosen = true;
				}
				//Checks if the space below the space being tested is occupied
				//(to ensure that the piece will land where it is placed)
				else if (backBoard[recentRow+1][recentCol+distance] != 0) {
						xMouse = (int) columnArray[recentCol+distance].getCenter();
						placementChosen = true;
				}
	}
	
	public void checkWinMethod() {
		if (piecesOnBoard == 42) {
			winner = "It's a Tie!";
			playerHasWon = true;
		}
		else if (backBoard[recentRow][recentCol] == 'Y')
			winner = "Yellow Wins!";
		else
			winner = "Red Wins!";
		//Checks if the last piece's color is equal to the ...
		//3 to the Left
		if (recentCol-3 >= 0)
			if (backBoard[recentRow][recentCol-3] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow][recentCol-2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow][recentCol-1] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//2 to the Left and 1 to the Right
		if (recentCol-2 >= 0 && recentCol+1 <= 6)
			if (backBoard[recentRow][recentCol-2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow][recentCol-1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow][recentCol+1] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//1 to the Left and 2 to the Right
		if (recentCol-1 >= 0 && recentCol+2 <= 6)
			if (backBoard[recentRow][recentCol-1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow][recentCol+1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow][recentCol+2] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//3 to the Right
		if (recentCol+3 <= 6)
			if (backBoard[recentRow][recentCol+1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow][recentCol+2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow][recentCol+3] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//3 Down
		if (recentRow+3 <= 5)
			if (backBoard[recentRow+1][recentCol] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+2][recentCol] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+3][recentCol] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//3 to the bottom Left
		if (recentCol-3 >= 0 && recentRow+3 <= 5)
			if (backBoard[recentRow+1][recentCol-1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+2][recentCol-2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+3][recentCol-3] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//2 to the bottom Left and 1 to the top Right
		if (recentCol - 2 >= 0 && recentRow + 2 <= 5 && recentCol + 1 <= 6 && recentRow - 1 >= 0)
			if (backBoard[recentRow-1][recentCol+1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+1][recentCol-1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+2][recentCol-2] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//1 to the bottom Left and 2 to the top Right
		if (recentCol - 1 >= 0 && recentRow + 1 <= 5 && recentCol + 2 <= 6 && recentRow - 2 >= 0)
			if (backBoard[recentRow-2][recentCol+2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow-1][recentCol+1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+1][recentCol-1] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//3 to the top Right
		if (recentCol + 3 <= 6 && recentRow - 3 >= 0)
			if (backBoard[recentRow-3][recentCol+3] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow-2][recentCol+2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow-1][recentCol+1] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//3 to the bottom Right
		if (recentCol+3 <=6 && recentRow+3 <=5)
			if (backBoard[recentRow+1][recentCol+1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+2][recentCol+2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+3][recentCol+3] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		
		//2 to the bottom Right and 1 to the top Left
		if (recentCol - 1 >= 0 && recentRow + 2 <= 5 && recentCol + 2 <= 6 && recentRow - 1 >= 0)
			if (backBoard[recentRow-1][recentCol-1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+1][recentCol+1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+2][recentCol+2] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		
		//1 to the bottom Right and 2 to the top Left
		if (recentCol - 2 >= 0 && recentRow + 1 <= 5 && recentCol + 1 <= 6 && recentRow - 2 >= 0)
			if (backBoard[recentRow-2][recentCol-2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow-1][recentCol-1] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow+1][recentCol+1] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		//3 to the top Left
		if (recentCol - 3 >= 0 && recentRow - 3 >= 0)
			if (backBoard[recentRow-3][recentCol-3] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow-2][recentCol-2] == backBoard[recentRow][recentCol]
					&& backBoard[recentRow-1][recentCol-1] == backBoard[recentRow][recentCol])
				playerHasWon = true;
		if (playerHasWon) {
			backgroundMusic.stop();
			gameEnd.play();
			prepareRestart = true;
		}
	}
	
	public void restartGameMethod() {
		if (!prepareRestart) {
			backgroundMusic.stop();
			gameEnd.play();
		}
		
		column1.resetPieces(); column2.resetPieces(); column3.resetPieces(); column4.resetPieces();
		column5.resetPieces(); column6.resetPieces(); column7.resetPieces(); 
		
		piecesOnBoard = 0;
		
		topLabel.setText(winner);
		topLabel.setTextFill(Color.GREEN);
		window.setScene(mainMenu);
		window.centerOnScreen();
		
		for (Column column: columnArray) {
	        for (GamePiece piece: column.getPieceArray()) {
	        	piece.setFill(Color.WHITE);
	        }
	    }
		backBoard = new char[6][7];
		
		completedGames++;
		againstComputer = false;
		playerHasWon = false;
		prepareRestart = false;
		restartGame = false;
	}
}