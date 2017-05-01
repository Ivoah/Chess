import java.util.*;

public class AI {
	
	private Board.Color color;
	private int difficulty;
	private Random rand;
	
	public AI(Board.Color color, int difficulty) {
		this.color = color;
		this.difficulty = difficulty;
		this.rand = new Random();
	}
	
	private ArrayList<Board> successors(Board state) {
		ArrayList<Board> states = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (state.getColor(j, i) == this.color) {
					for (Vec2 move : state.getMoves(j, i)) {
						Board newState = new Board(state);
						newState.move(new Vec2(j, i), move);
						states.add(newState);
					}
				}
			}
		}
		return states;
	}
	
	public Board.Color getColor() {
		return this.color;
	}
	
	public Board makeMove(Board board) {
		int v = max(board, this.difficulty);
		ArrayList<Board> states = new ArrayList<>();
		for (Board state : successors(board)) {
			if (state.utility(this.color) == v) {
				states.add(state);
			}
		}
		
		Board state = states.get(this.rand.nextInt(states.size()));
		System.out.println(state.history.get(state.history.size() - 1));
		return state;
	}
	
	/**
	 * Find the most valuable state
	 * @param board
	 * @return
	 */
	private int max(Board board, int depth) {
		if (depth <= 0) return board.utility(this.color);
		if (board.checkCheckmate(this.color)) return 0;
		if (board.checkCheckmate(Chess.otherColor(this.color))) return Integer.MAX_VALUE;
		
		int v = Integer.MIN_VALUE;
		
		for (Board state : successors(board)) {
			v = Math.max(v, min(state, depth - 1));
		}
		
		return v;
	}
	
	/**
	 * Find the least valuable state
	 * @param board
	 * @return
	 */
	private int min(Board board, int depth) {
		if (depth <= 0) return board.utility(Chess.otherColor(this.color));
		if (board.checkCheckmate(this.color)) return 0;
		if (board.checkCheckmate(Chess.otherColor(this.color))) return Integer.MAX_VALUE;
		
		int v = Integer.MAX_VALUE;
		
		for (Board state : successors(board)) {
			v = Math.min(v, max(state, depth - 1));
		}
		
		return v;
	}
}
