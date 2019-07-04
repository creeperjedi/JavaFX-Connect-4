import javafx.scene.shape.Circle;

public class GamePiece extends Circle{
	String name;
	int col;
	int row;
	
	//Rows and columns start at 1, not 0
	public GamePiece(char letter, int number){
		super();
		name = Character.toString(letter) + number;
		row = letter - 'A' + 1;
		col = number;		
	}
	
	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
}
