public class Column {
	private double rightBorder;
	private double centerX;
	private int placedPieces;
	
	//When numbering Columns start at 1, not 0
	public Column() {
		placedPieces = 0;
	}
	
	//All previous columns must be of equal size for centerX to correct
	public void setBorder(double borderValue) {
		rightBorder = borderValue;
	}
	
	public void setCenter(double previousBorder) {
		centerX = (previousBorder + rightBorder) / 2;
	}
	
	public void addPiece() {
		placedPieces++;
	}
	
	public double getBorder() {
		return rightBorder;
	}
	
	public double getCenter() {
		return centerX;
	}
	
	public int getPieces() {
		return placedPieces;
	}
	
	public void resetPieces() {
		placedPieces = 0;
	}
	
}
