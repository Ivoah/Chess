import java.util.*;

public class Board {

	private static enum Color {WHITE, BLACK, EMPTY};

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

	public ArrayList<Vec2> getMoves(Vec2 pos) {
		return getMoves(pos.getX(), pos.getY());
	}

	public ArrayList<Vec2> getMoves(int x, int y) {
		ArrayList<Vec2> moves = new ArrayList<>();
		Color col = getColor(x, y);
		moves.add(new Vec2(x, y));
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
			break;
		case '♟':
			moves.add(new Vec2(5, 5));
			break;
		case '♙':
			break;
		}
		return moves;
	}

	private Color getColor(Vec2 pos) {
		return getColor(pos.getX(), pos.getY());
	}

	private Color getColor(int x, int y) {
		switch (board[y][x]) {
		case '♖':
		case '♘':
		case '♗':
		case '♕':
		case '♔':
		case '♙':
			return Color.WHITE;
		case '♜':
		case '♞':
		case '♝':
		case '♛':
		case '♚':
		case '♟':
			return Color.BLACK;
		default:
			return Color.EMPTY;
		}
	}
	
	public void move(Vec2 from, Vec2 to) {
		board[to.getY()][to.getX()] = board[from.getY()][from.getX()];
		board[from.getY()][from.getX()] = ' ';
		history.add(from + " to " + to);
	}
}
