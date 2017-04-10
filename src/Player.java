
public abstract class Player {
	private String name;
	private Board.Color color;
	
	public Player(String name, Board.Color color) {
		this.name = name;
		this.color = color;
	}
	
	public String getName() { return name; }
	
	/**
	 * Have the player make a move, and return the new board with that move
	 * @param board The board the player will move on
	 * @return the new board after the player has moved
	 */
	public abstract Board makeMove(Board board);
}
