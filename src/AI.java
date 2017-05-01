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
	
	private ArrayList<Board> successors(Board state, Board.Color color) {
		ArrayList<Board> states = new ArrayList<>();
		
		if (state.checkCheck(this.color)) {
			Vec2 king = null;
			
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (state.getColor(j, i) == color && (state.getPiece(j, i) == '♚' || state.getPiece(j, i) == '♔')) {
						king = new Vec2(j, i);
					}
				}
			}
			
			for (Vec2 move : state.getMoves(king)) {
				Board newState = new Board(state);
				newState.move(king, move);
				states.add(newState);
			}
		} else {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (state.getColor(j, i) == color) {
						for (Vec2 move : state.getMoves(j, i)) {
							Board newState = new Board(state);
							newState.move(new Vec2(j, i), move);
							states.add(newState);
						}
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
		int v = Integer.MIN_VALUE;
		ArrayList<Object[]> states = new ArrayList<>();
		for (Board state : successors(board, this.color)) {
			int m = max(state, this.difficulty);
			if (v <= m) {
				v = m;
				states.add(new Object[]{m, state});
			}
		}
		
		ArrayList<Board> maxStates = new ArrayList<>();
		for (Object[] state : states) {
			if ((int)state[0] == v) {
				maxStates.add((Board)state[1]);
			}
		}
		
		Board state = maxStates.get(this.rand.nextInt(maxStates.size()));
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
		if (board.checkCheckmate(this.color)) return Integer.MIN_VALUE;
		if (board.checkCheckmate(Board.otherColor(this.color))) return Integer.MAX_VALUE;
		
		int v = Integer.MIN_VALUE;
		
		for (Board state : successors(board, this.color)) {
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
		if (depth <= 0) return board.utility(this.color);
		if (board.checkCheckmate(this.color)) return Integer.MIN_VALUE;
		if (board.checkCheckmate(Board.otherColor(this.color))) return Integer.MAX_VALUE;
		
		int v = Integer.MAX_VALUE;
		
		for (Board state : successors(board, Board.otherColor(this.color))) {
			v = Math.min(v, max(state, depth - 1));
		}
		
		return v;
	}
}
