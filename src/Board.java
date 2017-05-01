import java.util.*;

/**
 * Board class
 * Holds the state of a board and handles moving
 */
public class Board {

	public static enum Color {White, Black, EMPTY};

	private static final char[][] start = new char[][] {
		{'♜', '♞', '♝', '♛', '♚', '♝', '♞', '♜'},
		{'♟', '♟', '♟', '♟', '♟', '♟', '♟', '♟'},
		{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
		{'♙', '♙', '♙', '♙', '♙', '♙', '♙', '♙'},
		{'♖', '♘', '♗', '♕', '♔', '♗', '♘', '♖'}
	};

	char[][] board = new char[8][8];
	boolean[] whiteCastle = new boolean[3];
	boolean[] blackCastle = new boolean[3];
	List<String> history = new ArrayList<>();
	
	public Board() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = start[i][j];
				//prev[i][j] = board[i][j];
			}
		}
	}

	/**
	 * Copy constructor
	 * Does a deep copy
	 * @param Board to copy
	 */
	public Board(Board init) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = init.board[i][j];
				//prev[i][j] = board[i][j];
			}
		}
		history = new ArrayList<>(init.history);
	}

	public Character getPiece(Vec2 pos) {
		return board[pos.getY()][pos.getX()];
	}

	public Character getPiece(int x, int y) {
		return board[y][x];
	}
	
	public boolean checkDraw(Color color) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getColor(j, i) == color) {
					if (getMoves(j, i).size() > 0) return false;
				}
			}
		}
	
		return !checkCheck(color);
	}
	
	public boolean checkCheckmate(Color color) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getColor(j, i) == color) {
					if (getMoves(j, i).size() > 0) return false;
				}
			}
		}
	
		return checkCheck(color);
	}
	
	/**
	 * Checks to see if a color's king is in check for this board
	 * @param color Color of the player to check for
	 * @return boolean if king is in check
	 */
	public boolean checkCheck(Color color) {
		Vec2 king = null;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getColor(j, i) == color && (board[i][j] == '♚' || board[i][j] == '♔')) {
					king = new Vec2(j, i);
				}
			}
		}
				
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getColor(j, i) != color && getColor(j, i) != Color.EMPTY) {
					if (board[i][j] == '♚' || board[i][j] == '♔') {
						if (getColor(j, i) != color) {
							for (int[] move : new int[][]{{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, {1, 0}, {-1, -1}, {0, -1}, {1, -1}}) {
								int nx = j + move[0];
								int ny = i + move[1];
								if (king.equals(nx, ny)) return true;
							}
						}
					} else {
						if (getMoves(j, i, true).contains(king)) return true;
					}
				}
			}
		}
		return false;
	}
	
	public ArrayList<Vec2> getMoves(Vec2 pos) {
		return getMoves(pos.getX(), pos.getY());
	}

	/**
	 * Gets the possible moves for a piece at (x, y)
	 * @param x x position of the piece
	 * @param y y position of the piece
	 * @return ArrayList of of possible moves
	 */
	
	public ArrayList<Vec2> getMoves(int x, int y) {
		return getMoves(x, y, false);
	}
	
	public ArrayList<Vec2> getMoves(int x, int y, boolean checkingCheck) {
		ArrayList<Vec2> moves = new ArrayList<>();
		Color col = getColor(x, y);
		switch (board[y][x]) {
		case '♛':
		case '♕':
		case '♜':
		case '♖':
			for (int i = 1; y + i < 8; i++) {
				Vec2 p = new Vec2(x, y + i);
				if (getColor(p) != col)
					moves.add(p);
				if (getColor(p) != Color.EMPTY)
					break;
			} for (int i = -1; y + i >= 0; i--) {
				Vec2 p = new Vec2(x, y + i);
				if (getColor(p) != col)
					moves.add(p);
				if (getColor(p) != Color.EMPTY)
					break;
			} for (int i = 1; x + i < 8; i++) {
				Vec2 p = new Vec2(x + i, y);
				if (getColor(p) != col)
					moves.add(p);
				if (getColor(p) != Color.EMPTY)
					break;
			} for (int i = -1; x + i >= 0; i--) {
				Vec2 p = new Vec2(x + i, y);
				if (getColor(p) != col)
					moves.add(p);
				if (getColor(p) != Color.EMPTY)
					break;
			}
			if (board[y][x] == '♜' || board[y][x] == '♖') break;
		case '♝':
		case '♗':
			for (int i = 1; x + i < 8 && y + i < 8; i++) {
				Vec2 p = new Vec2(x + i, y + i);
				if (getColor(p) != col)
					moves.add(p);
				if (getColor(p) != Color.EMPTY)
					break;
			} for (int i = 1; x - i >= 0 && y + i < 8; i++) {
				Vec2 p = new Vec2(x - i, y + i);
				if (getColor(p) != col)
					moves.add(p);
				if (getColor(p) != Color.EMPTY)
					break;
			} for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
				Vec2 p = new Vec2(x - i, y - i);
				if (getColor(p) != col)
					moves.add(p);
				if (getColor(p) != Color.EMPTY)
					break;
			} for (int i = 1; x + i < 8 && y - i >= 0; i++) {
				Vec2 p = new Vec2(x + i, y - i);
				if (getColor(p) != col)
					moves.add(p);
				if (getColor(p) != Color.EMPTY)
					break;
			}
			break;
		case '♞':
		case '♘':
			for (int[] move : new int[][]{{1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}}) {
				int nx = x + move[0];
				int ny = y + move[1];
				if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && getColor(nx, ny) != col) {
					moves.add(new Vec2(nx, ny));
				}
			}
			break;
		case '♚':
		case '♔':
			if (getColor(x, y) == Color.Black) {
				if (!blackCastle[0] && !blackCastle[1]) {
					if (getColor(1,0) == Color.EMPTY && getColor(2,0) == Color.EMPTY && getColor(3,0) == Color.EMPTY){
						Board testBoard = new Board(this);
						testBoard.move(new Vec2(x, y), new Vec2(2, 0));
						if (!testBoard.checkCheck(col))
							moves.add(new Vec2(2, 0));
					}	
				}
				if (!blackCastle[1] && !blackCastle[2]){
					if (getColor(6,0) == Color.EMPTY && getColor(7,0) == Color.EMPTY){
						Board testBoard = new Board(this);
						testBoard.move(new Vec2(x, y), new Vec2(6, 0));
						if (!testBoard.checkCheck(col))
							moves.add(new Vec2(6,0));
					}	
				}
			} else if (getColor(x, y) == Color.White) {
				if (!whiteCastle[0] && !whiteCastle[1]) {
					if (getColor(1,7) == Color.EMPTY && getColor(2,7) == Color.EMPTY && getColor(3,7) == Color.EMPTY){
						Board testBoard = new Board(this);
						testBoard.move(new Vec2(x, y), new Vec2(2, 7));
						if (!testBoard.checkCheck(col))
							moves.add(new Vec2(2, 7));
					}	
				}
				if (!whiteCastle[1] && !whiteCastle[2]){
					if (getColor(6,7) == Color.EMPTY && getColor(5,7) == Color.EMPTY){
						Board testBoard = new Board(this);
						testBoard.move(new Vec2(x ,y), new Vec2(6, 7));
						if (!testBoard.checkCheck(col))
							moves.add(new Vec2(6, 7));
					}	
				}
			}
			for (int[] move : new int[][]{{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, {1, 0}, {-1, -1}, {0, -1}, {1, -1}}) {
				int nx = x + move[0];
				int ny = y + move[1];
				if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && getColor(nx, ny) != col) {
					moves.add(new Vec2(nx, ny));
				}
			}
			break;
		case '♟':
			if (y == 7) break;
			if (getColor(x, y + 1) == Color.EMPTY) moves.add(new Vec2(x, y + 1));
			if (y == 1 && getColor(x, 2) == Color.EMPTY) {
				if (getColor(x, y + 2) == Color.EMPTY) moves.add(new Vec2(x, y + 2));
			}
			
			if (x > 0 && getColor(x - 1, y + 1) != col && getColor(x - 1, y + 1) != Color.EMPTY) moves.add(new Vec2(x - 1, y + 1));
			if (x < 7 && getColor(x + 1, y + 1) != col && getColor(x + 1, y + 1) != Color.EMPTY) moves.add(new Vec2(x + 1, y + 1));
			break;
		case '♙':
			if (y == 0) break;
			if (getColor(x, y - 1) == Color.EMPTY) moves.add(new Vec2(x, y - 1));
			if (y == 6  && getColor(x, 5) == Color.EMPTY) {
				if (getColor(x, y - 2) == Color.EMPTY) moves.add(new Vec2(x, y - 2));
			}
			
			if (x > 0 && getColor(x - 1, y - 1) != col && getColor(x - 1, y - 1) != Color.EMPTY) moves.add(new Vec2(x - 1, y - 1));
			if (x < 7 && getColor(x + 1, y - 1) != col && getColor(x + 1, y - 1) != Color.EMPTY) moves.add(new Vec2(x + 1, y - 1));
			break;
		}
		
		if (!checkingCheck) {
			ArrayList<Vec2> realMoves = new ArrayList<>();
			
			for (Vec2 move : moves) {
				Board testBoard = new Board(this);
				testBoard.move(new Vec2(x, y), move);
				if (!testBoard.checkCheck(col))
					realMoves.add(move);
			}
			
			return realMoves;
		} else {
			return moves;
		}
	}

	public Color getColor(Vec2 pos) {
		return getColor(pos.getX(), pos.getY());
	}

	/**
	 * Get the color of the piece at (x, y)
	 * @param x
	 * @param y
	 * @return the piece's color
	 */
	public Color getColor(int x, int y) {
		switch (board[y][x]) {
		case '♖':
		case '♘':
		case '♗':
		case '♕':
		case '♔':
		case '♙':
			return Color.White;
		case '♜':
		case '♞':
		case '♝':
		case '♛':
		case '♚':
		case '♟':
			return Color.Black;
		default:
			return Color.EMPTY;
		}
	}
	
	/**
	 * Make a move
	 * @param from
	 * @param to
	 */
	public void move(Vec2 from, Vec2 to) {
		if (from.getX() == 4 && from.getY() == 0){
			blackCastle[1] = true;
		}
		if (from.getX() == 0 && from.getY() == 0){
			blackCastle[0] = true;
		}
		if (from.getX() == 7 && from.getY() == 0){
			blackCastle[2] = true;
		}
		
		if (from.getX() == 4 && from.getY() == 7){
			whiteCastle[1] = true;
		}
		if (from.getX() == 0 && from.getY() == 7){
			whiteCastle[0] = true;
		}
		if (from.getX() == 7 && from.getY() == 7){
			whiteCastle[2] = true;
		}
		if (from.getX() == 4 && from.getY() == 0){
			blackCastle[1] = true;
		}
		if (from.getX() == 0 && from.getY() == 0){
			blackCastle[0] = true;
		}
		if (from.getX() == 7 && from.getY() == 0){
			blackCastle[2] = true;
		}
		
		if (from.getX() == 4 && from.getY() == 7){
			whiteCastle[1] = true;
		}
		if (from.getX() == 0 && from.getY() == 7){
			whiteCastle[0] = true;
		}
		if (from.getX() == 7 && from.getY() == 7){
			whiteCastle[2] = true;
		}
		
		if (board[0][4] == '♔'){
			if (from.getX() == 4 && from.getY() == 0 && to.getX() == 2 && from.getY() == 0){
				move(new Vec2(0,0), new Vec2(0,3));
			}
			if (from.getX() == 4 && from.getY() == 0 && to.getX() == 6 && from.getY() == 0){
				move(new Vec2(7,0), new Vec2(0,5));
			}
		}
		if (board[0][4] == '♚'){
			if (from.getX() == 4 && from.getY() == 7 && to.getX() == 2 && from.getY() == 7){
				move(new Vec2(7,0), new Vec2(3,7));
			}
			if (from.getX() == 4 && from.getY() == 7 && to.getX() == 6 && from.getY() == 7){
				move(new Vec2(7,7), new Vec2(5,7));
			}
		}
		board[to.getY()][to.getX()] = board[from.getY()][from.getX()];
		board[from.getY()][from.getX()] = ' ';
		history.add(from + " to " + to);
	}
	
	public void move(String move) {
		String[] s = move.split(" ");
		move(new Vec2(s[0]), new Vec2(s[2]));
	}
	
	private int pieceValue(char piece) {
		switch (piece) {
		case '♖':
		case '♜':
			return 5;
		case '♘':
		case '♞':
			return 3;
		case '♗':
		case '♝':
			return 3;
		case '♕':
		case '♛':
			return 9;
		case '♙':
		case '♟':
			return 1;
		default:
			return 0;
		}
	}
	
	/**
	 * Calculate the utility of the board for a given color
	 * @param color Color of player
	 * @return utility of board
	 */
	public int utility(Color color) {
		int total = 0;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getColor(j, i) == color) total += pieceValue(board[i][j]);
				else total -= pieceValue(board[i][j]);
			}
		}
		
		return total;
	}
	
	public List<String> getHistory() {
		return history;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				sb.append(board[i][j] + "|");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static Color otherColor(Color color) {
		if (color == Color.White) return Color.Black;
		else return Color.White;
	}
}
