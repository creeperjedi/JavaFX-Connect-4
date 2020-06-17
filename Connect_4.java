import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;

public class Connect_4 extends Application {	
	//JavaFX Hierarchy: Stage -> Scene -> (Grid)Pane. In layman's terms: window -> contents -> layout
    Stage window;
    Scene mainMenu, gameboardScene;
    
    Pane gameboardPane = new Pane();
    
    ToggleGroup difficulty;
    RadioButton randomDifficulty;
    RadioButton basicDifficulty;

	TextField connectField = new TextField("4");

    Label topLabel = new Label("Welcome to JavaFX Connect-4");
    
    ArrayList<Column> columnArray = new ArrayList<>();
    
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
    int rows = 6;
    int columns = 7;
    int piecesOnBoard = 0;
    int completedGames = 0;
    char[][] backBoard;
    boolean firstGame = true;
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

		//==========Game Screen==========

    	//Assigns gameboardPane to gameboardScene
        gameboardScene = new Scene(gameboardPane);
        gameboardPane.setBackground(new Background(new BackgroundFill(Color.rgb(30, 143, 255),
				CornerRadii.EMPTY, Insets.EMPTY)));

		//Assigns the setOnMouseClicked listener to gameboardPane.
        gameboardPane.setOnMouseClicked(e -> {
        	//Collects the X and Y position of the click and prints them
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
	    	    if (!prepareRestart && againstComputer) {
	    			if (previousRow != recentRow || previousCol != recentCol) {
	    				computerInputMethod();	
	    	    	    checkWinMethod();
	    			}
	    	    }
    	    }
        });
        
        /* Adds two Listeners, widthProperty and heightProperty, that are triggered during changes in the dimensions of
         * gameboardScene. When triggered, the GamePieces will be repositioned and resized
         */
        gameboardScene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
			System.out.println("Width: " + newSceneWidth);
			resizedWidth = (double) newSceneWidth;

			//Sets the rightBorder for each Column
			for (Column column : columnArray) {
				column.setRightBorder(resizedWidth / columns * column.getColumnNumber());
			}

			/* Sets the Center for each Column [this is calculated by dividing the width of
			 * the window by the number of columns (to divide it into even sections for each
			 * Column) then by multiplying that by the quantity of Column's column number - 0.5
			 * (to set the center of the column in the middle of the column since multiplying
			 * only by the Column's number would equal its border instead of its center)]
			 */
			for (Column column : columnArray) {
				column.setCenter(resizedWidth / columns * (column.getColumnNumber() - 0.5));
			}

			double circleRadius = columnArray.get(0).getRightBorder() / 2;

			//Sets the CenterX and Radius for each GamePiece in each Column
			for (Column column: columnArray) {
				for (GamePiece piece: column.getPieceArray()) {
					piece.setCenterX(column.getCenter());
					piece.setRadius(circleRadius);
				}
			}
		});
        gameboardScene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
			System.out.println("Height: " + newSceneHeight);
			resizedHeight = (double) newSceneHeight;

			/* Sets the Center for each GamePiece in each Column [this is calculated by
			 * dividing the height of the window by the number of rows (to divide it into
			 * even sections for each row) then by multiplying that by the quantity of
			 * GamePiece's column number - 0.5 (to set the center of the GamePiece in the
			 * middle of the row since multiplying only by the GamePiece's row would equal
			 * the row's border instead of its center)]
			 */
			for (Column column: columnArray) {
				for (GamePiece piece: column.getPieceArray()) {
						piece.setCenterY(resizedHeight / rows * (piece.getRow() - 0.5));
				}
			}
		});

		//==========Main Menu==========

		Label pickOpponent = new Label("Pick your opponent");

		Label pickDifficulty = new Label("Computer difficulty:");

		//Checks if the button is pressed and if so calls startGameMethod
		Button startGameHuman = new Button("Another human");
		startGameHuman.setOnAction(e -> startGameMethod());

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

		Label connectLabel = new Label("Connect:");

		//Makes connectField only accept integers (Ripped this from Stack Overflow. It works, I don't question it.)
		connectField.setTextFormatter(new TextFormatter<>(change ->
				(change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
		connectField.setPrefWidth(80);

		Button viewCredits = new Button("View credits");
		viewCredits.setOnAction(e -> CreditsWindow.display());
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        //Add Main Menu Objects to GridPane
        GridPane.setConstraints(topLabel, 1, 1, 3, 1);
        GridPane.setConstraints(pickOpponent, 1, 6, 3, 1);
        GridPane.setHalignment(pickOpponent, HPos.CENTER);
        
        GridPane.setConstraints(startGameHuman, 1, 7);
        GridPane.setConstraints(startGameComputer, 3, 7);
        GridPane.setHalignment(pickDifficulty, HPos.RIGHT);
        GridPane.setConstraints(pickDifficulty, 1, 9);
        GridPane.setConstraints(randomDifficulty, 3, 9);
        GridPane.setConstraints(basicDifficulty, 3, 11);
        GridPane.setConstraints(connectLabel, 1, 13);
        GridPane.setConstraints(connectField, 3, 13);
        GridPane.setConstraints(viewCredits, 1, 15, 3, 1);
        GridPane.setFillWidth(viewCredits, true);
        viewCredits.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.getChildren().addAll(topLabel, pickOpponent, startGameHuman, startGameComputer,
        		pickDifficulty, randomDifficulty, basicDifficulty, connectLabel, connectField, viewCredits);
        	
        //Main Menu Display
    	window.setTitle("JavaFX Connect 4");
        mainMenu = new Scene(grid, 350, 300);
        window.setScene(mainMenu);  
        window.show();
        window.centerOnScreen();
    }
    
    public void startGameMethod() {
    	if (firstGame) {
    	    //Creates the 2d-array that will be used to check for game wins
    	    /* Organized like a traditional co-ordinate plane with an inverted y-axis
    	     * 0, 1, 2, 3, 4, 5, 6 < [][columns] 
    	     * 1 
    	     * 2 
    	     * 3 
    	     * 4 
    	     * 5
    	     * ^ [rows][] 
    	     */
	    	backBoard = new char[rows][columns];
	    	
	    	int currentRow = 1;
	    	int currentCol = 1;
	    	
	    	//Populates columnArray with the number of specified Columns
	    	while(currentCol <= columns) {
	    		columnArray.add(new Column(currentCol++));
	    	}
	    	
	    	//Populates each Column in columnArray with GamePieces starting from the bottom (ex. F1 -> A1)
	    	for (Column column : columnArray) {
	        	while(currentRow <= rows) {
	        		column.addGamePiece(new GamePiece((char) ('A' + rows - currentRow++), column.getColumnNumber()));
	        	}
	        	currentRow = 1;
	    	}
	        /* The final board starts with A1 in the Top Left and F7 in the Bottom Right (for a normal sized board)
	         * A1, A2, A3, A4, A5, A6, A7
	         * B1, B2, B3, B4, B5, B6, B7
	         * C1, C2, C3, C4, C5, C6, C7
	         * D1, D2, D3, D4, D5, D6, D7
	         * E1, E2, E3, E4, E5, E6, E7
	         * F1, F2, F3, F4, F5, F6, F7
	         */
	    	
	    	/* Adds each GamePiece from each Column to the game board (gameboardPane)
	    	 * and sets their color to white to represent empty spaces on the board
	    	 */
			for (Column column: columnArray) {
		        for (GamePiece piece: column.getPieceArray()) {
		        	//The pieces are scaled down to give space between them
		        	piece.setScaleX(0.80);
		        	piece.setScaleY(0.80);
		        	piece.setFill(Color.WHITE);
		        	gameboardPane.getChildren().add(piece);
		        }
		    }
	    	firstGame = false;
    	}
    	System.out.println(backBoard.length + " Rows");
    	System.out.println(backBoard[0].length + " Columns");
    	//Saves the dimensions of the mainMenu window
		windowWidth = window.getWidth();
        windowHeight = window.getHeight();
        //Sets the Scene to gameboardScene
        window.setScene(gameboardScene);

		/* This code is required to prevent problems with scaling, such as instances with high DPI displays. If the
		 * scaling is set to 100% then this issue would not be present either way.
		 *
		 * Description of the issue: On all games after the first, the pieces will not be displayed correctly for the
		 * size of the window but will instead be sized and positioned for a window larger or smaller according to the
		 * scaling set. For example, if the scaling is set to 150% percent then the pieces will be arranged as if the
		 * window was a size 150% of what is actually is. This is because the scene is scaling itself on top of
		 * the scaling that the window itself did when the program started.
		 * While the Scene is still scaled at the start of the first game, it is not effected by this issue is because
		 * when the window's width and height are set (using .setWidth and .setHeight) the Scene's width and height are
		 * changed and its Listeners are triggered, resulting in the scaling and repositioning of the pieces to where
		 * they should be. On subsequent games, when the window's width and height are set, the Scene is not resized
		 * (idk why it works once but not again, I'm probably missing something). This results in the window being the
		 * same size but the Scene being the scaled size, ex. 150% of what is originally was, and its contents spilling
		 * over past the edge of the window.
		 *
		 * To solve this issue, the window is set to the size of the Scene (using .sizeToScene) but then immediately
		 * sized back to what it originally was, resizing the Scene and triggering the Listeners to correctly scale and
		 * position the pieces on the board.
		 *
		 * Note: To see this issue yourself, set your scaling to something other than 100% and comment out the
		 * "window.seizeToScene()" line.
		 */
		window.sizeToScene();
		//Applies the saved dimensions to keep the window sizes consistent between the main menu and the game board
		window.setWidth(windowWidth);
		window.setHeight(windowHeight);

		backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
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
		
		/* Checks if the x coordinate of the last click, xMouse, is inside of first column [this is tested by checking
		 * if xMouse is less than (to the left of) the right border of the column (retrieved by using .getBorder)]
		 * 
		 * If it is then it will check each piece starting from the bottom until it finds one that has not been placed
		 * 
		 * If the click is not within the the first column then it will check again with the next
		 * column and so on until there are no columns left
		 */
		for (Column column: columnArray) {
			if (xMouse <= column.getRightBorder() && column.getPieces() < rows) {
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
		piecesOnBoard = 0;
		for (Column column: columnArray) {
			 piecesOnBoard += column.getPieces();
	    }
        System.out.println("Number of pieces on the board: " + piecesOnBoard);
	}
	
	public void computerInputMethod() {
		Random rand = new Random();
		do {
			if (basicDifficulty.isSelected()) {
				placementChosen = false;
				System.out.println("Checks have begun");
				//Checks if the last piece's color is equal to the ...
				//2 to the Left
				if (recentCol - 2 >= 0)
					if (backBoard[recentRow][recentCol - 2] == backBoard[recentRow][recentCol]
							&& backBoard[recentRow][recentCol - 1] == backBoard[recentRow][recentCol]) {
						//Tries to place a piece to the Left of the row of 3: ▪ ⚪ ⚪ ⚫
						backBoardCalculator(-3);
						//Tries to place a piece to the Right of the row of 3: ⚪ ⚪ ⚫ ▪️
						if (!placementChosen)
							backBoardCalculator(+1);
					}
				//2 to the Right
				if (recentCol + 2 <= (columns - 1) && !placementChosen)
					if (backBoard[recentRow][recentCol + 1] == backBoard[recentRow][recentCol]
							&& backBoard[recentRow][recentCol + 2] == backBoard[recentRow][recentCol]) {
						//Tries to place a piece to the Right of the row of 3: ⚫ ⚪ ⚪ ▪️
						backBoardCalculator(+3);
						//Tries to place a piece to the Left of the row of 3: ▪ ⚫ ⚪ ⚪
						if (!placementChosen)
							backBoardCalculator(-1);
					}
				//2 Down
				if (recentRow + 2 <= (rows - 1) && !placementChosen)
					if (backBoard[recentRow + 1][recentCol] == backBoard[recentRow][recentCol]
							&& backBoard[recentRow + 2][recentCol] == backBoard[recentRow][recentCol]) {
						placementChosen = true;
					}
				System.out.println("Choice made: " + placementChosen);
			}
			if (!placementChosen) {
				/* The limit for the Random is set to the right border of the last column, ensuring that it will be
				 * within the bounds of the board since the minimum is 0
				 */
				xMouse = rand.nextInt((int) columnArray.get(columnArray.size() - 1).getRightBorder());
			}
			previousRow = recentRow;
			previousCol = recentCol;
			/* The computer places pieces by manipulating the xMouse value to select a column and then
			 * calls the humanInputMethod() to mimic the process of a mouse click on that column
			 */
			humanInputMethod();
		} while (previousRow == recentRow && previousCol == recentCol);
	}

	/* Checks if the space "distance" columns away from recentCol and on the same row as recentRow
	 * is empty and if so sets xMouse to the centerX of that space's column
	 */
	public void backBoardCalculator(int distance) {
		//Checks if the column being tested is valid
		if (recentCol+distance >= 0 && recentCol+distance <= (columns - 1))
			//Checks if the space being tested is empty
			if (backBoard[recentRow][recentCol+distance] == 0)
				//Checks if the row being tested is the bottom-most row
				if (recentRow == (rows - 1)) {
					xMouse = (int) columnArray.get(recentCol+distance).getCenter();
					placementChosen = true;
				}
				//Checks if the space below the space being tested is occupied
				//(to ensure that the piece will land where it is placed)
				else if (backBoard[recentRow+1][recentCol+distance] != 0) {
						xMouse = (int) columnArray.get(recentCol+distance).getCenter();
						placementChosen = true;
				}
	}

	//Returns true if the requested row and column pair is a valid location on the board
	public boolean isValidPlace(int row, int column) {
		return (row >= 0) && (row <= backBoard.length - 1) && (column >= 0) && (column <= backBoard[0].length - 1);
	}

	public void checkWinMethod() {
		if (piecesOnBoard == (rows * columns)) {
			winner = "It's a Tie!";
			playerHasWon = true;
		}
		else if (backBoard[recentRow][recentCol] == 'Y')
			winner = "Yellow Wins!";
		else
			winner = "Red Wins!";

		/* ========== How wins are checked ==========
		 * Wins are checked by establishing a 'search area' and checking if every piece
		 * in that area is equal to the piece just placed. While very complicated, it 
		 * no longer limits the game to only connect 4.
		 * ===== Examples =====
		 * Here we will be checking the pieces horizontally (though everything here
		 * also applies to vertically and diagonally) and we'll be assuming that
		 * that the game is set to connect 4 (this is controlled through the
		 * "connect" variable)
		 * === Diagram ===
		 * ⚪ ⚪ ⚪ ⚫ ⚪ ⚪ ⚪	Leftmost ⚪ = firstIndex(-3)	⚫ = lastIndex (0)	
		 * The numbers in parenthesis indicate the variable's value which represent how
		 * many columns away they are from the last placed piece and form the borders
		 * of the search area
		 * === Explanation ===
		 * A search area of 4 pieces long will be created and "checkIndex" will be set
		 * to firstIndex to start. Every piece in this search area will be checked to see
		 * if they are equal to the last piece placed.
		 * <⚪>⚪ ⚪ ⚫ ⚪ ⚪ ⚪  | ⚪<⚪>⚪ ⚫ ⚪ ⚪ ⚪  | ⚪ ⚪<⚪>⚫ ⚪ ⚪ ⚪  | ⚪ ⚪ ⚪<⚫>⚪ ⚪ ⚪ 
		 * The search area is the minimum 
		 * number of pieces that must be equal or else the needed number of connections
		 * ("connect") will not be met. Because of this fact, if any piece is not equal
		 * then the search area will stop being checked and the search area will be moved
		 * by 1 to the right and checked again. This cycle will continue until the
		 * search area goes past the right boundary of the board, a connection is found, 
		 * or the maximum number of check areas have been searched. The maximum number 
		 * of search areas is the same as the value of "connect".
		 * :⚪ ⚪ ⚪ ⚫:⚪ ⚪ ⚪  | ⚪:⚪ ⚪ ⚫ ⚪:⚪ ⚪  | ⚪ ⚪:⚪ ⚫ ⚪ ⚪:⚪  | ⚪ ⚪ ⚪:⚫ ⚪ ⚪ ⚪: 
		 * As shown, when "connect" is set to 4, there are 4 possible search areas
		 */
		int connect = (Integer.parseInt(connectField.getText()));
		int firstIndex;
		int lastIndex;
		int checkIndex;
		int searchAreaCycle;
		int numberOfConnections;
		int currentWinDirection = 0;
		int rowManipulation;
		int columnManipulation;
		String[] winDirections = {"horizontal", "vertical", "top left to bottom right", "bottom left to top right"};

		while (currentWinDirection <= winDirections.length - 1) {
			rowManipulation = 1;
			columnManipulation = 1;
			firstIndex = (connect - 1) * -1; //-3 from the piece in connect 4
			lastIndex = firstIndex + (connect - 1); //0 from the piece connect 4
			searchAreaCycle = 1;
			switch (winDirections[currentWinDirection]) {
				case "horizontal":
					rowManipulation = 0;
					break;
				case "vertical":
					lastIndex = firstIndex * -1;
					firstIndex = 0;
					columnManipulation = 0;
					break;
				case "bottom left to top right":
					rowManipulation = -1;
					break;
			}

			//Cycles through search areas until the maximum number of them has been reached
			while (searchAreaCycle <= connect) {
				//Checks the search area from left (firstIndex) to right
				checkIndex = firstIndex;
				//For every search area the numberOfConnections is set back to 0
				numberOfConnections = 0;
				/* Checks if the search area is not within the right bounds of the board and if so ends
				 * searching because the search area moves to the right and once the search area is out
				 * of bounds it will not return
				 */
				if (!isValidPlace(recentRow + lastIndex*rowManipulation,
						recentCol + lastIndex*columnManipulation)){
					break;
				}
				//Checks if the search area is within the left and top bounds and if so checks the search area
				if (isValidPlace(recentRow + firstIndex*rowManipulation,
						recentCol + firstIndex*columnManipulation)) {
					//Checks every piece in the current search area
					while (checkIndex <= lastIndex) {
						/* Checks if the piece being checked if not the same and if so breaks (therefore exiting
						 * the loop and causing the search area to shift), because there is no point to keep
						 * checking the search area since it already isn't all the same
						 */
						if (backBoard[recentRow + checkIndex*rowManipulation][recentCol + checkIndex*columnManipulation]
								!= backBoard[recentRow][recentCol]) {
							break;
						}
						/* If the piece being checked is the same then that is recorded in numberOfConnections and
						 * the loop continues until the whole search area is completed
						 */
						else {
							numberOfConnections++;
						}
						//Increases checkIndex to check the next piece
						checkIndex++;
					}
				}
				if (numberOfConnections == connect) {
					System.out.println("Player has won by " + winDirections[currentWinDirection]);
					playerHasWon = true;
					break;
				}
				//Increases the searchAreaCycle and shifts the search area to the right
				searchAreaCycle++;
				firstIndex++;
				lastIndex++;
			}
			currentWinDirection++;
		}

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
		
		for (Column column : columnArray) {
			column.resetPieces();
		}
		
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
		backBoard = new char[rows][columns];
		
		completedGames++;
		againstComputer = false;
		playerHasWon = false;
		prepareRestart = false;
		restartGame = false;
	}
}
