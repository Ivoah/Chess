import java.util.*;

public class Board {
	private static final String[][] start = new String[][] {
		{"♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜"},
		{"♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟"},
		{"",  "",  "",  "",  "",  "",  "",  "" },
		{"",  "",  "",  "",  "",  "",  "",  "" },
		{"",  "",  "",  "",  "",  "",  "",  "" },
		{"",  "",  "",  "",  "",  "",  "",  "" },
		{"♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙"},
		{"♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"}
	};
	
	String[][] board = new String[8][8];
	List<String> history = new ArrayList<>();
	
	public Board() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = start[i][j];
			}
		}
	}
	
	public Board(Board init) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = init.board[i][j];
			}
		}
	}
}
