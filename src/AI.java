import java.util.*;

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
		Board b = new Board(board);
		ArrayList<Vec2> pieces = new ArrayList<>();
		Random rand = new Random();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.getColor(j, i) == color && board.getMoves(j, i).size() > 0) pieces.add(new Vec2(j, i));
			}
		}
		
		Vec2 piece = pieces.get(rand.nextInt(pieces.size()));
		Vec2 move = board.getMoves(piece).get(rand.nextInt(board.getMoves(piece).size()));
		
		b.move(piece, move);
		return b;
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
