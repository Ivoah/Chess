
public class Human extends Player {

	public Human(String name, Board.Color color) {
		super(name, color);
	}
	
	/**
	 * Somehow interface with the GUI
	 */
	public Board makeMove(Board board) {
		return new Board(board);
	}
}
