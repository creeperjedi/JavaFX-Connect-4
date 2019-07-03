import javafx.scene.shape.Circle;

public class GamePiece extends Circle{
	int col;
	int row;
	
	public GamePiece(char letter, int number){
		super();
		col = 'A' - letter + 1;
		row = number;		
	}
	
	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
}
