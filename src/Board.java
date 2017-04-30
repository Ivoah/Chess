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
	char[][] prev = new char[8][8];
	List<String> history = new ArrayList<>();

	
	public Board() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = start[i][j];
				prev[i][j] = board[i][j];
			}
		}
	}

	/**
	 * Copy contstructor
	 * Does a deep copy
	 * @param Board to copy
	 */
	public Board(Board init) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = init.board[i][j];
				prev[i][j] = board[i][j];
			}
		}
		history = new ArrayList<>(init.history);
	}

	public char getPiece(Vec2 pos) {
		return board[pos.getY()][pos.getX()];
	}

	public char getPiece(int x, int y) {
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
		Vec2 king = null;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getColor(j, i) == color && (board[i][j] == '♚' || board[i][j] == '♔')) {
					king = new Vec2(j, i);
				}
			}
		}
		
		return checkCheck(color) && getMoves(king).size() == 0;
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
						if (getMoves(j, i).contains(king)) return true;
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
			for (int[] move : new int[][]{{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, {1, 0}, {-1, -1}, {0, -1}, {1, -1}}) {
				int nx = x + move[0];
				int ny = y + move[1];
				if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && getColor(nx, ny) != col) {
					Board testBoard = new Board(this);
					testBoard.move(new Vec2(x, y), new Vec2(nx, ny));
					if (!testBoard.checkCheck(col))
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
		return moves;
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
		prev[to.getY()][to.getX()] = board[to.getY()][to.getX()];
		board[to.getY()][to.getX()] = board[from.getY()][from.getX()];
		board[from.getY()][from.getX()] = ' ';
		history.add(from + " to " + to);
	}
	
	/**
	 * Calculate the utility of the board for a given color
	 * @param color Color of player
	 * @return utility of baord
	 */
	public int utility(Color color) {
		return 0;
	}
}
