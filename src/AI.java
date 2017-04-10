public class AI extends Player {
	
	private int difficulty;
	
	public AI(String name, Board.Color color, int difficulty) {
		super(name, color);
		this.difficulty = difficulty;
	}
	
	/**
	 * Implement Minimax algorithm
	 */
	public Board makeMove(Board board) {
		return new Board(board);
	}
	
	/**
	 * Find the most valuable state
	 * @param board
	 * @return
	 */
	private int max(Board board) {
		return 0;
	}
	
	/**
	 * Find the least valuable state
	 * @param board
	 * @return
	 */
	private int min(Board board) {
		return 0;
	}
}
